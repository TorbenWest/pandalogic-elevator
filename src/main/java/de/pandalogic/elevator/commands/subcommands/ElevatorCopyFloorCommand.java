package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

public class ElevatorCopyFloorCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Copies an existing floor at a certain height difference.";
    }

    @Override
    public String getSyntax() {
        return "/elevator copyFloor <ElevatorId> <CopyFloorNumber> <FloorNumber> <Difference>";
    }

    @Override
    public String[] getNames() {
        return this.getArray("copyFloor", "duplicateFloor");
    }

    @Override
    public int arguments() {
        return 4;
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            int elevatorId = Integer.parseInt(args[1]);
            var elevator = PLElevatorManager.findElevatorById(elevatorId);

            if (elevator == null) {
                player.sendMessage(Message.ELEVATOR_NOT_EXISTS.getMessage());
                return;
            }

            int copyFloorNumber = Integer.parseInt(args[2]);

            if (elevator.getFloors().stream().noneMatch(f -> f.getNumber() == copyFloorNumber)) {
                player.sendMessage(Message.ELEVATOR_FLOOR_NOT_EXISTS.replace(elevatorId)
                        .replace(copyFloorNumber).getMessage());
                return;
            }

            int floorNumber = Integer.parseInt(args[3]);
            var floor = elevator.getFloors().stream()
                    .filter(f -> f.getNumber() == copyFloorNumber)
                    .findFirst()
                    .orElse(null);

            if (floor == null) {
                player.sendMessage(Message.INVALID_FLOOR_NUMBER.getMessage());
                return;
            }

            int difference = Integer.parseInt(args[4]);
            int baseY = floor.getY() + difference;
            int buttonY = ((int) floor.getButton().getY()) + difference;
            int gateBottomY = ((int) floor.getGateBottomLeft().getY()) + difference;
            int gateTopY = ((int) floor.getGateTopRight().getY()) + difference;

            String command = "elevator createFloor " +
                    elevatorId +
                    " " +
                    floorNumber +
                    " " +
                    baseY +
                    " " +
                    ((int) floor.getButton().getX()) +
                    " " +
                    buttonY +
                    " " +
                    ((int) floor.getButton().getZ()) +
                    " " +
                    ((int) floor.getGateBottomLeft().getX()) +
                    " " +
                    gateBottomY +
                    " " +
                    ((int) floor.getGateBottomLeft().getZ()) +
                    " " +
                    ((int) floor.getGateTopRight().getX()) +
                    " " +
                    gateTopY +
                    " " +
                    ((int) floor.getGateTopRight().getZ()) +
                    " " +
                    floor.getOutputDirection().toString();


            player.performCommand(command);
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INTEGER_PARAMETER.getMessage());
        }
    }

}
