package de.pandalogic.elevator;

import de.pandalogic.elevator.database.ElevatorDAO;
import de.pandalogic.elevator.database.ElevatorDTO;
import de.pandalogic.elevator.database.ElevatorFloorDTO;
import de.pandalogic.elevator.database.ElevatorSettingsDTO;
import de.pandalogic.elevator.events.*;
import de.pandalogic.elevator.exceptions.FloorNotRegisteredException;
import de.pandalogic.elevator.utils.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class PLElevator implements Elevator {

    @Getter
    private final List<Floor> floors;

    private final ElevatorDTO elevatorDTO;
    private final ElevatorMovement movement;
    private final Deque<Floor> nextStops;

    private volatile boolean isMoving;
    private volatile boolean tooHeavyLock;

    @Getter
    private Floor currentFloor;

    private Floor targetFloor;
    private int movingTask;
    private int playerTask;

    public PLElevator(ElevatorDTO elevatorDTO) {
        this.elevatorDTO = elevatorDTO;
        this.movement = new ElevatorMovement(elevatorDTO.getElevatorLocations(), elevatorDTO.getCurrentY());
        this.nextStops = new ArrayDeque<>();
        this.floors = new ArrayList<>();
        this.isMoving = false;
        this.tooHeavyLock = false;

        elevatorDTO.getFloors(this).forEach(this::registerFloor);
    }

    @Override
    public void start() {
        if (this.tooHeavyLock) {
            return;
        }

        if (this.targetFloor == null) {
            this.targetFloor = this.nextStops.pop();

            if (this.targetFloor == null) {
                return;
            }
        }

        if (this.isElevatorTooHeavy()) {
            this.tooHeavyLock = true;
            this.movement.passengers.forEach(p -> p.sendMessage(Message.ELEVATOR_TOO_HEAVY.getMessage()));
            Bukkit.getPluginManager().callEvent(new ElevatorTooHeavyEvent(this, this.movement.passengers));
            Bukkit.getScheduler().scheduleSyncDelayedTask(ElevatorPlugin.getInstance(), () -> {
                this.tooHeavyLock = false;
                this.start();
            }, this.elevatorDTO.getSettings().getNextStopCheck());
            return;
        }

        if (this.currentFloor != null) {
            this.currentFloor.close(this.movement.passengers);
        }

        this.isMoving = true;
        Bukkit.getPluginManager().callEvent(new ElevatorStartEvent(this, this.currentFloor, this.targetFloor));
        this.playerPosition();
        this.move();

        for (Player current : this.movement.passengers) {
            Bukkit.getPluginManager().callEvent(new PlayerUseElevatorEvent(this, current, this.currentFloor));
        }
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.playerTask);
        Bukkit.getScheduler().cancelTask(this.movingTask);
        this.movement.passengers.clear();
        this.isMoving = false;

        if (this.currentFloor != null) {
            this.nextStops.removeIf(f -> f.equals(this.currentFloor));
            this.currentFloor.open();
        }

        Bukkit.getPluginManager().callEvent(new ElevatorStopEvent(this, this.currentFloor));
        Bukkit.getScheduler().scheduleSyncDelayedTask(ElevatorPlugin.getInstance(), () -> {
            if (!this.isMoving && (!this.nextStops.isEmpty() || this.targetFloor != null)) {
                this.start();
            }
        }, this.elevatorDTO.getSettings().getNextStopCheck());
    }

    @Override
    public void terminate() {
        Bukkit.getScheduler().cancelTask(this.movingTask);
        Bukkit.getScheduler().cancelTask(this.playerTask);
        this.targetFloor = null;
        this.nextStops.clear();
        this.movement.passengers.clear();
        this.isMoving = false;
        ElevatorDAO.updateElevatorOnTerminate(this.elevatorDTO);
    }

    @Override
    public void registerFloor(ElevatorFloorDTO dto) {
        this.registerFloor(new PLFloor(dto, this.elevatorDTO.getWorldName(), this));
    }

    @Override
    public void registerFloor(Floor floor) {
        this.floors.add(floor);
        this.currentFloor = this.getFloorByY(this.movement.currentY);

        if (this.currentFloor.equals(floor)) {
            this.currentFloor.open();
        } else {
            this.currentFloor.close(this.movement.passengers);
        }

        ElevatorInventory.deleteInventory(this.getId());
    }

    @Override
    public void unregisterFloor(int floorNumber) {
        if (this.isMoving) {
            this.stop();
        }

        this.nextStops.removeIf(f -> f.getNumber() == floorNumber);

        if (this.targetFloor.getNumber() == floorNumber) {
            this.targetFloor = this.nextStops.pop();
        }

        if (this.currentFloor != null && this.currentFloor.getNumber() == floorNumber) {
            this.currentFloor = null;
        }

        this.floors.removeIf(f -> f.getNumber() == floorNumber);
    }

    @Override
    public int getId() {
        return this.elevatorDTO.getElevatorId();
    }

    @Override
    public void addStop(Floor floor) {
        if (this.nextStops.contains(floor)) {
            return;
        }

        if (floor.equals(this.currentFloor)) {
            return;
        }

        this.nextStops.addLast(floor);
        Bukkit.getPluginManager().callEvent(new ElevatorReceiveCallEvent(this, floor));

        if (!this.isMoving) {
            this.start();
        }
    }

    @Override
    public void addStop(int floorNumber) {
        this.addStop(this.getFloorByNumber(floorNumber));
    }

    @Override
    public void addStop(Block button) {
        Floor floor = this.getFloorForButton(button);

        if (floor == null) {
            return;
        }

        this.addStop(floor);
    }

    @Override
    public Floor getFloorForButton(Block button) {
        return this.floors.stream()
                .filter(f -> f.getButton().equals(button.getLocation()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Floor getTargetFloor() {
        return this.targetFloor;
    }

    @Override
    public Deque<Floor> getNextStops() {
        return this.nextStops;
    }

    @Override
    public boolean isPlayerInElevator(Player player) {
        if (this.isMoving) {
            return this.movement.passengers.contains(player);
        }

        List<Location> elevator = this.movement.locations;

        if (elevator.isEmpty()) {
            return false;
        }

        Location playerLocation = player.getLocation();
        double playerX = playerLocation.getX();
        double playerY = playerLocation.getY();
        double playerZ = playerLocation.getZ();

        Location leftBottomLocation = elevator.get(0);
        int leftBottomX = leftBottomLocation.getBlockX();
        int leftBottomY = leftBottomLocation.getBlockY();
        int leftBottomZ = leftBottomLocation.getBlockZ();

        Location rightTopLocation = elevator.get(elevator.size() - 1);
        int rightTopX = rightTopLocation.getBlockX();
        int rightTopY = rightTopLocation.getBlockY();
        int rightTopZ = rightTopLocation.getBlockZ();

        double offset = 1.3;

        if (!(playerX > (Math.min(leftBottomX, rightTopX) - offset))) {
            return false;
        }

        if (!(playerX < (Math.max(leftBottomX, rightTopX) + offset))) {
            return false;
        }

        if (!(playerY > (Math.min(leftBottomY, rightTopY) - offset))) {
            return false;
        }

        if (!(playerY < (Math.max(leftBottomY, rightTopY) + offset))) {
            return false;
        }

        if (!(playerZ > (Math.min(leftBottomZ, rightTopZ) - offset))) {
            return false;
        }

        return playerZ < (Math.max(leftBottomZ, rightTopZ) + offset);
    }

    @Override
    public int getCurrentY() {
        return this.movement.currentY;
    }

    @Override
    public ElevatorSettingsDTO getSettings() {
        return this.elevatorDTO.getSettings();
    }

    @Override
    public void onPlayerQuit(Player player) {
        this.movement.passengers.removeIf(p -> p.getUniqueId().equals(player.getUniqueId()));
    }

    @Override
    public World getWorld() {
        return Bukkit.getWorld(this.elevatorDTO.getWorldName());
    }

    private boolean isElevatorTooHeavy() {
        this.searchForPassengers();
        return this.movement.passengers.size() > this.elevatorDTO.getSettings().getMaxPlayer();
    }

    private Floor getFloorByNumber(int number) {
        return this.floors.stream()
                .filter(f -> f.getNumber() == number)
                .findFirst()
                .orElseThrow(FloorNotRegisteredException::new);
    }

    private Floor getFloorByY(int y) {
        return this.floors.stream()
                .filter(f -> f.getY() == y)
                .findFirst()
                .orElse(null);
    }

    private void searchForPassengers() {
        this.movement.passengers.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.isPlayerInElevator(player)) {
                this.movement.passengers.add(player);
            }
        }
    }

    private void move() {
        this.movingTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(ElevatorPlugin.getInstance(), () -> {
            if (this.movement.currentY < this.targetFloor.getY()) {
                this.currentFloor = null;
                this.movement.move(true);
                this.movement.currentY++;
                this.elevatorDTO.increaseY();
                Bukkit.getPluginManager().callEvent(new ElevatorMoveEvent(this, BlockFace.UP));
            } else if (this.movement.currentY > this.targetFloor.getY()) {
                this.currentFloor = null;
                this.movement.move(false);
                this.movement.currentY--;
                this.elevatorDTO.decreaseY();
                Bukkit.getPluginManager().callEvent(new ElevatorMoveEvent(this, BlockFace.DOWN));
            } else {
                this.currentFloor = this.targetFloor;
                this.targetFloor = null;
                this.stop();
                return;
            }

            Floor optionalFloor = this.getFloorByY(this.movement.currentY);

            if (optionalFloor == null) {
                return;
            }

            if (optionalFloor.equals(this.targetFloor)) {
                return;
            }

            if (!this.nextStops.contains(optionalFloor)) {
                return;
            }

            this.currentFloor = optionalFloor;
            this.stop();
        }, this.elevatorDTO.getSettings().getStartDelay(), this.elevatorDTO.getSettings().getSpeed());
    }

    private void playerPosition() {
        this.playerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(ElevatorPlugin.getInstance(), () -> {
            A:
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!this.movement.passengers.contains(player)) {
                    continue;
                }

                // Push player back, if he runs into the iron gate while the elevator is moving
                for (Floor current : this.floors.stream().filter(Floor::isClosed).collect(Collectors.toList())) {
                    for (Location location : current.getEntrance()) {
                        if (location.distance(player.getLocation()) <= 0.5) {
                            BlockFace direction = current.getOutputDirection().getOppositeFace();
                            player.setVelocity(new Vector(direction.getModX(), direction.getModY(), direction.getModZ())
                                    .divide(new Vector(2, 2, 2)));
                            continue A;
                        }
                    }
                }

                // Teleport player back into the elevator, if the player glitches outside
                Location center = this.movement.getCenter();

                if (player.getLocation().distance(center) > 3) {
                    player.teleport(center);
                }
            }
        }, 0, this.elevatorDTO.getSettings().getPlayerPositionCheck());
    }

    private static class ElevatorMovement {

        private final List<Player> passengers;
        private final List<Location> locations;
        private final World world;
        private final int centerX;
        private final int centerZ;

        private SortType sortType;
        private int currentY;

        public ElevatorMovement(List<Location> locations, int currentY) {
            this.locations = locations;
            this.currentY = currentY;
            this.passengers = new ArrayList<>();
            this.sortType = SortType.NONE;

            Location center = this.locateCenter();
            this.world = center.getWorld();
            this.centerX = center.getBlockX();
            this.centerZ = center.getBlockZ();
        }

        public Location getCenter() {
            return new Location(this.world, this.centerX, this.currentY + 2, this.centerZ - 1);
        }

        private Location locateCenter() {
            Location max = null;

            for (Location current : this.locations) {
                if (max == null) {
                    max = current;
                    continue;
                }

                if (max.getBlockX() < current.getBlockX() || max.getBlockZ() < current.getBlockZ()) {
                    max = current;
                } else if (max.getBlockX() == current.getBlockX() && max.getBlockZ() == current.getBlockZ()) {
                    if (max.getBlockY() > current.getBlockY()) {
                        max = current;
                    }
                }
            }

            return Objects.requireNonNull(max);
        }

        private void move(boolean up) {
            if (up && this.sortType != SortType.UP) {
                this.locations.sort((l1, l2) -> l2.getBlockY() - l1.getBlockY());
                this.sortType = SortType.UP;
            } else if (!up && this.sortType != SortType.DOWN) {
                this.locations.sort(Comparator.comparingInt(Location::getBlockY));
                this.sortType = SortType.DOWN;
            }

            if (up) {
                this.passengers.forEach(p -> p.setVelocity(new Vector(0, 1, 0)));
            }

            for (Location location : new ArrayList<>(this.locations)) {
                Block current = location.getBlock();
                Block target = location.add(0, up ? 1 : -1, 0).getBlock();

                target.setType(current.getType());
                target.setBlockData(current.getBlockData());
                current.setType(Material.AIR);
            }
        }

        private enum SortType {
            UP,
            DOWN,
            NONE
        }

    }

}
