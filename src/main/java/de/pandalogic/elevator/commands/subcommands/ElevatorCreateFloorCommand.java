package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.database.ElevatorFloorDAO;
import de.pandalogic.elevator.database.ElevatorFloorDTO;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class ElevatorCreateFloorCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Creates a floor for an elevator.";
    }

    @Override
    public String getSyntax() {
        return "/elevator createFloor <ElevatorId> <FloorNumber> <BaseY> <BX> <BY> <BZ> <GateBLX> <GateBLY> <GateBLZ>" +
                " <GateTRX> <GateTRY> <GateTRZ> <OutputDirection>";
    }

    @Override
    public String[] getNames() {
        return this.getArray("createFloor", "addFloor");
    }

    @Override
    public int arguments() {
        return 13;
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            int elevatorId = Integer.parseInt(args[1]);
            Elevator elevator = PLElevatorManager.findElevatorById(elevatorId);

            if (elevator == null) {
                player.sendMessage(Message.ELEVATOR_NOT_EXISTS.getMessage());
                return;
            }

            int floorNumber = Integer.parseInt(args[2]);

            if (elevator.getFloors().stream().anyMatch(f -> f.getNumber() == floorNumber)) {
                player.sendMessage(Message.INVALID_FLOOR_NUMBER.getMessage());
                return;
            }

            int baseY = Integer.parseInt(args[3]);
            int bX = Integer.parseInt(args[4]);
            int bY = Integer.parseInt(args[5]);
            int bZ = Integer.parseInt(args[6]);
            int gblX = Integer.parseInt(args[7]);
            int gblY = Integer.parseInt(args[8]);
            int gblZ = Integer.parseInt(args[9]);
            int gtrX = Integer.parseInt(args[10]);
            int gtrY = Integer.parseInt(args[11]);
            int gtrZ = Integer.parseInt(args[12]);

            BlockFace blockFace = BlockFace.valueOf(args[13].toUpperCase());

            if (blockFace != BlockFace.NORTH && blockFace != BlockFace.EAST &&
                    blockFace != BlockFace.SOUTH && blockFace != BlockFace.WEST) {
                player.sendMessage(Message.INVALID_BLOCKFACE.getMessage());
                return;
            }

            ElevatorFloorDAO.addFloor(new ElevatorFloorDTO(elevatorId, floorNumber, baseY, bX, bY, bZ,
                    gblX, gblY, gblZ, gtrX, gtrY, gtrZ, blockFace));
            player.sendMessage(Message.ELEVATOR_FLOOR_CREATED.replace(floorNumber).replace(elevatorId).getMessage());
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INTEGER_PARAMETER.getMessage());
        }
    }

}