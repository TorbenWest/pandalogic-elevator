package de.pandalogic.elevator.listener;

import de.pandalogic.elevator.ElevatorPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ElevatorPlugin.getInstance().getManager().getElevators()
                .forEach(e -> e.onPlayerQuit(event.getPlayer()));
    }

}
