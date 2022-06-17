package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import lombok.NonNull;

public class ElevatorStopEvent extends ElevatorFloorEvent {

    public ElevatorStopEvent(@NonNull Elevator elevator, Floor elevatorFloor) {
        super(elevator, elevatorFloor);
    }

}
