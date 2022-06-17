package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import lombok.Getter;
import org.bukkit.entity.Player;

abstract class ElevatorPlayerEvent extends ElevatorEvent {

    @Getter
    private final Player player;

    public ElevatorPlayerEvent(Elevator elevator, Player player) {
        super(elevator);
        this.player = player;
    }

}
