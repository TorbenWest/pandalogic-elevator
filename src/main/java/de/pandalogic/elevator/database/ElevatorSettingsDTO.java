package de.pandalogic.elevator.database;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "elevatorSettingsId")
public class ElevatorSettingsDTO {

    private int elevatorSettingsId;
    private int elevatorId;
    private int playerPositionCheck;
    private int maxPlayer;
    private int nextStopCheck;
    private int startDelay;
    private int speed;

    public ElevatorSettingsDTO(int elevatorSettingsId, int elevatorId, int playerPositionCheck,
                               int maxPlayer, int nextStopCheck, int startDelay, int speed) {
        this.elevatorSettingsId = elevatorSettingsId;
        this.elevatorId = elevatorId;
        this.playerPositionCheck = playerPositionCheck;
        this.maxPlayer = maxPlayer;
        this.nextStopCheck = nextStopCheck;
        this.startDelay = startDelay;
        this.speed = speed;
    }

}
