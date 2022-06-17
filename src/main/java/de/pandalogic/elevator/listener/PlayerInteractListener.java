package de.pandalogic.elevator.listener;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.ElevatorPlugin;
import de.pandalogic.elevator.Floor;
import de.pandalogic.elevator.events.PlayerCallElevatorEvent;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clicked = event.getClickedBlock();

        if (clicked == null || !clicked.getType().name().endsWith("_BUTTON")) {
            return;
        }

        for (Elevator current : ElevatorPlugin.getInstance().getManager().getElevators()) {
            for (Floor floor : current.getFloors()) {
                if (floor.getButton().equals(clicked.getLocation())) {
                    PlayerCallElevatorEvent elevatorEvent = new PlayerCallElevatorEvent(current, event.getPlayer(), floor);
                    Bukkit.getPluginManager().callEvent(elevatorEvent);

                    if (elevatorEvent.isCancelled()) {
                        event.getPlayer().sendMessage(Message.ELEVATOR_CALL_CANCELLED.getMessage());
                        return;
                    }

                    current.addStop(floor);
                    return;
                }
            }
        }
    }

}
