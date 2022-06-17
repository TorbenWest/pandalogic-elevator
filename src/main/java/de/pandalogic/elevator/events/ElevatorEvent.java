package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

abstract class ElevatorEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    @NonNull
    private final Elevator elevator;

    public ElevatorEvent(@NonNull Elevator elevator) {
        this.elevator = elevator;
    }

    @Override
    @NonNull
    public HandlerList getHandlers() {
        return handlerList;
    }

}
