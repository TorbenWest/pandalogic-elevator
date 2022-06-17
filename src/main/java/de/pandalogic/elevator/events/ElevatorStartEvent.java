package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import lombok.Getter;
import lombok.NonNull;

public class ElevatorStartEvent extends ElevatorFloorEvent {

    @Getter
    private final Floor targetFloor;

    public ElevatorStartEvent(@NonNull Elevator elevator, Floor currentFloor, @NonNull Floor targetFloor) {
        super(elevator, currentFloor);
        this.targetFloor = targetFloor;
    }

}
