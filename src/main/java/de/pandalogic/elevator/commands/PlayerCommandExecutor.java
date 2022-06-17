package de.pandalogic.elevator.commands;

import de.pandalogic.elevator.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface PlayerCommandExecutor extends CommandExecutor {

    void onCommand(Player player, Command command, String[] args);

    @Override
    default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.NO_CONSOLE.getMessage());
            return true;
        }

        if (command.getPermission() != null && !player.hasPermission(command.getPermission())) {
            player.sendMessage(Message.NO_PERMISSION.getMessage());
            return true;
        }

        this.onCommand(player, command, args);
        return true;
    }

    default Player verifyTarget(Player sender, String name) {
        Player target = Bukkit.getPlayer(name);

        if (target == null) {
            sender.sendMessage(Message.TARGET_OFFLINE.getMessage());
            return null;
        }

        if (target.equals(sender)) {
            sender.sendMessage(Message.TARGET_EQUALS_SENDER.getMessage());
            return null;
        }

        return target;
    }


}
