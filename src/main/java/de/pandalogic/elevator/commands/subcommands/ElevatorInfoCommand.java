package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

public class ElevatorInfoCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Gives information about an elevator.";
    }

    @Override
    public String getSyntax() {
        return "/elevator info <Id>";
    }

    @Override
    public String[] getNames() {
        return this.getArray("info", "information");
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public void perform(Player player, String[] args) {
        try {
            int id = Integer.parseInt(args[1]);
            Elevator elevator = PLElevatorManager.findElevatorById(id);

            if (elevator == null) {
                player.sendMessage(Message.ELEVATOR_NOT_EXISTS.getMessage());
                return;
            }

            player.sendMessage(Message.ELEVATOR_INFO.replace(id).replace(elevator.getCurrentY())
                    .replace(elevator.getFloors().size()).getMessage());
            elevator.getFloors().forEach(f -> player.sendMessage(Message.ELEVATOR_FLOORS.replace(f.getNumber())
                    .replace(f.getY()).getMessage()));
            player.sendMessage(Message.ELEVATOR_SETTINGS.getMessage());
            player.sendMessage(Message.ELEVATOR_SETTING.replace("player-position-check")
                    .replace(elevator.getSettings().getPlayerPositionCheck()).getMessage());
            player.sendMessage(Message.ELEVATOR_SETTING.replace("max-player")
                    .replace(elevator.getSettings().getMaxPlayer()).getMessage());
            player.sendMessage(Message.ELEVATOR_SETTING.replace("next-stop-check")
                    .replace(elevator.getSettings().getNextStopCheck()).getMessage());
            player.sendMessage(Message.ELEVATOR_SETTING.replace("speed")
                    .replace(elevator.getSettings().getSpeed()).getMessage());
            player.sendMessage(Message.ELEVATOR_SETTING.replace("start-delay")
                    .replace(elevator.getSettings().getStartDelay()).getMessage());
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INVALID_NUMBER.getMessage());
        }
    }

}