package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class PlayerUseElevatorEvent extends ElevatorPlayerEvent {

    @Getter
    private final Floor floor;

    public PlayerUseElevatorEvent(@NonNull Elevator elevator, @NonNull Player player, Floor floor) {
        super(elevator, player);
        this.floor = floor;
    }

}
