package de.pandalogic.elevator.exceptions;

public class TooManyFloorsException extends RuntimeException {

    public TooManyFloorsException(int elevatorId) {
        super("The elevator " + elevatorId + " has too many floors registered! Cannot create inventory for this elevator.");
    }

}
