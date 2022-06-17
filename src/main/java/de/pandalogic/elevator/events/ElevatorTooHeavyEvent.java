package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.List;

public class ElevatorTooHeavyEvent extends ElevatorEvent {

    @Getter
    private final List<Player> passengers;

    public ElevatorTooHeavyEvent(@NonNull Elevator elevator, @NonNull List<Player> passengers) {
        super(elevator);
        this.passengers = passengers;
    }

}
