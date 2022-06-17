package de.pandalogic.elevator;

import de.pandalogic.elevator.exceptions.TooManyFloorsException;
import de.pandalogic.elevator.utils.ItemBuilder;
import de.pandalogic.elevator.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.pandalogic.elevator.utils.MessageUtil.translate;

public class ElevatorInventory {

    private final static Map<Integer, Inventory> INVENTORIES = new HashMap<>();

    private final static ItemStack MAGENTA_PANE = new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE)
            .setName(" ")
            .toItemStack();
    private final static ItemStack PINK_PANE = new ItemBuilder(Material.PINK_STAINED_GLASS_PANE)
            .setName(" ")
            .toItemStack();

    public static Inventory getElevatorInventory(Elevator elevator) {
        if (!INVENTORIES.containsKey(elevator.getId())) {
            if (elevator.getFloors().size() > 28) {
                throw new TooManyFloorsException(elevator.getId());
            }

            Inventory inventory = getInventory(elevator.getId());
            int counter = 0;

            for (int current : getFloorsSorted(elevator)) {
                ItemStack minecart;

                if (elevator.getCurrentFloor() != null && elevator.getCurrentFloor().getNumber() == current) {
                    minecart = getGlowMinecart(current);
                } else {
                    minecart = getMinecart(current);
                }

                inventory.setItem(getMinecartSlots()[counter++], minecart);
            }

            INVENTORIES.put(elevator.getId(), inventory);
        }

        return INVENTORIES.get(elevator.getId());
    }

    public static void deleteInventory(int elevatorId) {
        for (Player current : Bukkit.getOnlinePlayers()) {
            InventoryHolder holder = current.getOpenInventory().getTopInventory().getHolder();

            if (holder instanceof ElevatorInventoryHolder) {
                if (((ElevatorInventoryHolder) holder).getElevatorId() == elevatorId) {
                    current.closeInventory();
                    current.sendMessage(Message.ELEVATOR_INVENTORY_CLOSED.getMessage());
                }
            }
        }

        INVENTORIES.entrySet().removeIf(e -> e.getKey() == elevatorId);
    }

    public static void updateInventory(Elevator elevator, int floorNumber, boolean remove) {
        var inventory = getElevatorInventory(elevator);
        int slot = getMinecartSlots()[getFloorsSorted(elevator).indexOf(floorNumber)];
        inventory.setItem(slot, remove ? getMinecart(floorNumber) : getGlowMinecart(floorNumber));
    }

    private static List<Integer> getFloorsSorted(Elevator elevator) {
        return new ArrayList<>(elevator.getFloors()).stream()
                .sorted().map(Floor::getNumber).collect(Collectors.toList());
    }

    private static ItemStack getMinecart(int floorNumber) {
        return new ItemBuilder(Material.MINECART)
                .setName(translate("&5Etage: &f" + floorNumber))
                .toItemStack();
    }

    private static ItemStack getGlowMinecart(int floorNumber) {
        return new ItemBuilder(Material.MINECART)
                .setName(translate("&5Etage: &1" + floorNumber))
                .addUnsafeEnchantment(Enchantment.DURABILITY, 1)
                .setFlags()
                .toItemStack();
    }

    private static Inventory getInventory(int elevatorId) {
        ElevatorInventoryHolder holder = new ElevatorInventoryHolder(elevatorId);
        Inventory inventory = Bukkit.createInventory(holder, 54, translate("&5Etagen"));
        holder.setInventory(inventory);

        inventory.setItem(0, MAGENTA_PANE);
        inventory.setItem(1, MAGENTA_PANE);
        inventory.setItem(4, MAGENTA_PANE);
        inventory.setItem(7, MAGENTA_PANE);
        inventory.setItem(8, MAGENTA_PANE);
        inventory.setItem(9, MAGENTA_PANE);
        inventory.setItem(17, MAGENTA_PANE);
        inventory.setItem(36, MAGENTA_PANE);
        inventory.setItem(44, MAGENTA_PANE);
        inventory.setItem(45, MAGENTA_PANE);
        inventory.setItem(46, MAGENTA_PANE);
        inventory.setItem(49, MAGENTA_PANE);
        inventory.setItem(52, MAGENTA_PANE);
        inventory.setItem(53, MAGENTA_PANE);

        inventory.setItem(2, PINK_PANE);
        inventory.setItem(3, PINK_PANE);
        inventory.setItem(5, PINK_PANE);
        inventory.setItem(6, PINK_PANE);
        inventory.setItem(18, PINK_PANE);
        inventory.setItem(26, PINK_PANE);
        inventory.setItem(27, PINK_PANE);
        inventory.setItem(35, PINK_PANE);
        inventory.setItem(47, PINK_PANE);
        inventory.setItem(48, PINK_PANE);
        inventory.setItem(50, PINK_PANE);
        inventory.setItem(51, PINK_PANE);
        return inventory;
    }

    private static int[] getMinecartSlots() {
        return new int[]{10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43};
    }

}
