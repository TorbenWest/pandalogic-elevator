package de.pandalogic.elevator.listener;

import de.pandalogic.elevator.ElevatorInventory;
import de.pandalogic.elevator.ElevatorInventoryHolder;
import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.events.ElevatorReceiveCallEvent;
import de.pandalogic.elevator.events.ElevatorStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class ElevatorInventoryListener implements Listener {

    @EventHandler
    public void onElevatorStart(ElevatorStartEvent event) {
        ElevatorInventory.updateInventory(event.getElevator(), event.getElevatorFloor().getNumber(), true);
    }

    @EventHandler
    public void onElevatorReceiveCall(ElevatorReceiveCallEvent event) {
        ElevatorInventory.updateInventory(event.getElevator(), event.getElevatorFloor().getNumber(), false);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getView().getBottomInventory() == event.getClickedInventory()) {
            if (event.getView().getTopInventory().getHolder() instanceof ElevatorInventoryHolder) {
                event.setCancelled(true);
                return;
            }
        }

        if (!(event.getClickedInventory().getHolder() instanceof ElevatorInventoryHolder)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null) {
            return;
        }

        if (!event.getCurrentItem().getType().equals(Material.MINECART)) {
            return;
        }

        String targetFloor = Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName().substring(11);

        try {
            int floor = Integer.parseInt(targetFloor);
            int elevatorId = ((ElevatorInventoryHolder) event.getClickedInventory().getHolder()).getElevatorId();
            PLElevatorManager.findElevatorById(elevatorId).addStop(floor);
            event.getWhoClicked().closeInventory();
        } catch (
                NumberFormatException e) {
            Bukkit.getLogger().warning("Elevator: Target floor '" + targetFloor + "' is not an integer!");
        }
    }

}
