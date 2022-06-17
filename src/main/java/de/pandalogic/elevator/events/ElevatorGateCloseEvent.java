package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import lombok.NonNull;

public class ElevatorGateCloseEvent extends ElevatorFloorEvent {

    public ElevatorGateCloseEvent(@NonNull Elevator elevator, @NonNull Floor elevatorFloor) {
        super(elevator, elevatorFloor);
    }

}
