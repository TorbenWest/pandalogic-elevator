package de.pandalogic.elevator.commands.subcommands;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.ElevatorInventory;
import de.pandalogic.elevator.ElevatorPlugin;
import de.pandalogic.elevator.commands.SubCommand;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.entity.Player;

public class ElevatorInventoryCommand extends SubCommand {

    @Override
    public String getDescription() {
        return "Open the elevator inventory for the specific elevator.";
    }

    @Override
    public String getSyntax() {
        return "/elevator inventory";
    }

    @Override
    public String[] getNames() {
        return this.getArray("inventory", "inv", "menu");
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public void perform(Player player, String[] args) {
        for (Elevator current : ElevatorPlugin.getInstance().getManager().getElevators()) {
            if (current.isPlayerInElevator(player)) {
                player.openInventory(ElevatorInventory.getElevatorInventory(current));
                return;
            }
        }

        player.sendMessage(Message.PLAYER_NOT_IN_ELEVATOR.getMessage());
    }

}
