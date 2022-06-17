package de.pandalogic.elevator;

import de.pandalogic.elevator.database.ElevatorFloorDTO;
import de.pandalogic.elevator.events.ElevatorGateCloseEvent;
import de.pandalogic.elevator.events.ElevatorGateOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;

public class PLFloor implements Floor {

    private final ElevatorFloorDTO floorEntity;
    private final List<Location> entrance;
    private final Location button;
    private final Elevator elevator;

    private boolean isOpen;

    public PLFloor(ElevatorFloorDTO floorEntity, String world, Elevator elevator) {
        this.floorEntity = floorEntity;
        this.elevator = elevator;
        this.entrance = floorEntity.getEntranceLocation(world);
        this.isOpen = false;
        this.button = new Location(Bukkit.getWorld(world), floorEntity.getButtonX(),
                floorEntity.getButtonY(), floorEntity.getButtonZ());
    }

    @Override
    public BlockFace getOutputDirection() {
        return this.floorEntity.getOutputDirection();
    }

    @Override
    public int getNumber() {
        return this.floorEntity.getFloorNumber();
    }

    @Override
    public Location getButton() {
        return this.button;
    }

    @Override
    public List<Location> getEntrance() {
        return this.entrance;
    }

    @Override
    public int getY() {
        return this.floorEntity.getBaseY();
    }

    @Override
    public void close(List<Player> passengers) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Location location : this.entrance) {
                if (player.getLocation().distance(location) < 2) {
                    BlockFace currentBlockFace = this.getOutputDirection();

                    if (passengers.contains(player)) {
                        currentBlockFace = this.getOutputDirection().getOppositeFace();
                    }

                    Vector vector = new Vector(currentBlockFace.getModX(),
                            currentBlockFace.getModY(),
                            currentBlockFace.getModZ())
                            .multiply(1.5);
                    player.setVelocity(vector);
                }
            }
        }

        this.entrance.forEach(e -> e.getBlock().setType(Material.IRON_BARS));
        this.isOpen = false;
        Bukkit.getPluginManager().callEvent(new ElevatorGateCloseEvent(this.elevator, this));
    }

    @Override
    public void open() {
        this.entrance.forEach(l -> l.getBlock().setType(Material.AIR));
        this.isOpen = true;
        Bukkit.getPluginManager().callEvent(new ElevatorGateOpenEvent(this.elevator, this));
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PLFloor that = (PLFloor) o;
        return getNumber() == that.getNumber();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }

    @Override
    public int compareTo(Floor o) {
        return this.getNumber() - o.getNumber();
    }

}
