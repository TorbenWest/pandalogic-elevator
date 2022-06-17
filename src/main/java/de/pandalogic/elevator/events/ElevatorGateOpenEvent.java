package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import lombok.NonNull;

public class ElevatorGateOpenEvent extends ElevatorFloorEvent {

    public ElevatorGateOpenEvent(@NonNull Elevator elevator, @NonNull Floor elevatorFloor) {
        super(elevator, elevatorFloor);
    }

}
