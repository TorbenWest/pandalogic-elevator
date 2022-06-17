package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerCallElevatorEvent extends ElevatorPlayerEvent implements Cancellable {

    @Getter
    private final Floor elevatorFloor;

    private boolean isCancelled;

    public PlayerCallElevatorEvent(Elevator elevator, Player player, Floor elevatorFloor) {
        super(elevator, player);
        this.elevatorFloor = elevatorFloor;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

}
