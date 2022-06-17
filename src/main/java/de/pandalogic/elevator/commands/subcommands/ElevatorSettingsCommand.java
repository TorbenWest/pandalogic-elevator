package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.database.ElevatorSettingsDAO;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

public class ElevatorSettingsCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Changes elevator settings.";
    }

    @Override
    public String getSyntax() {
        return "/elevator settings <ElevatorId> <Setting> <Value>";
    }

    @Override
    public String[] getNames() {
        return this.getArray("settings", "setting");
    }

    @Override
    public int arguments() {
        return 3;
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

            int value = Integer.parseInt(args[3]);

            if (value <= 0) {
                player.sendMessage(Message.ELEVATOR_INVALID_VALUE.getMessage());
                return;
            }

            String setting = args[2].replaceAll("-", "_").toLowerCase();

            switch (setting) {
                case "player_position_check" -> elevator.getSettings().setPlayerPositionCheck(value);
                case "max_player" -> elevator.getSettings().setMaxPlayer(value);
                case "next_stop_check" -> elevator.getSettings().setNextStopCheck(value);
                case "speed" -> elevator.getSettings().setSpeed(value);
                case "start_delay" -> elevator.getSettings().setStartDelay(value);
                default -> {
                    player.sendMessage(Message.ELEVATOR_UNKNOWN_SETTING.getMessage());
                    return;
                }
            }

            ElevatorSettingsDAO.updateSettings(elevator.getSettings());
            player.sendMessage(Message.ELEVATOR_SETTING_UPDATED.getMessage());
        } catch (NumberFormatException e) {
            player.sendMessage(Message.INTEGER_PARAMETER.getMessage());
        }
    }

}
