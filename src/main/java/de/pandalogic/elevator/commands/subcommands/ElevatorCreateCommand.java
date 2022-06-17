package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.database.ElevatorDAO;
import de.pandalogic.elevator.database.ElevatorDTO;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ElevatorCreateCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Creates an elevator.";
    }

    @Override
    public String getSyntax() {
        return "/elevator create <CurrentY> <BLX> <BLY> <BLZ> <TRX> <TRY> <TRZ>";
    }

    @Override
    public String[] getNames() {
        return this.getArray("create", "add");
    }

    @Override
    public int arguments() {
        return 7;
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            int currentY = Integer.parseInt(args[1]);
            int blX = Integer.parseInt(args[2]);
            int blY = Integer.parseInt(args[3]);
            int blZ = Integer.parseInt(args[4]);
            int trX = Integer.parseInt(args[5]);
            int trY = Integer.parseInt(args[6]);
            int trZ = Integer.parseInt(args[7]);

            int id = ElevatorDAO.addElevator(new ElevatorDTO(
                    Objects.requireNonNull(player.getLocation().getWorld()).getName(),
                    currentY, blX, blY, blZ, trX, trY, trZ));

            player.sendMessage(Message.ELEVATOR_CREATED.replace(id).getMessage());
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INTEGER_PARAMETER.getMessage());
        }
    }

}
