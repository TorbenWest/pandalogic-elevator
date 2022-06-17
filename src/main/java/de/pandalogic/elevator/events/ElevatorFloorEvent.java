package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import lombok.Getter;
import lombok.NonNull;

abstract class ElevatorFloorEvent extends ElevatorEvent {

    @Getter
    @NonNull
    private final Floor elevatorFloor;

    protected ElevatorFloorEvent(@NonNull Elevator elevator, @NonNull Floor elevatorFloor) {
        super(elevator);
        this.elevatorFloor = elevatorFloor;
    }

}
