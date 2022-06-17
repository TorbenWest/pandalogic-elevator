package de.pandalogic.elevator;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ElevatorInventoryHolder implements InventoryHolder {

    @Getter
    private final int elevatorId;

    @Getter
    @Setter
    private Inventory inventory;

    public ElevatorInventoryHolder(int elevatorId) {
        this.elevatorId = elevatorId;
    }

}
