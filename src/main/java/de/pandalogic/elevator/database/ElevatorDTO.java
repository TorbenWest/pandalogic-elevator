package de.pandalogic.elevator.database;

import de.pandalogic.elevator.Elevator;
import de.pandalogic.elevator.Floor;
import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.PLFloor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(of = "elevatorId")
public class ElevatorDTO {

    private ElevatorSettingsDTO settingsDTO;

    private int elevatorId;
    private String worldName;
    private int currentY;
    private int bottomLeftX;
    private int bottomLeftY;
    private int bottomLeftZ;
    private int topRightX;
    private int topRightY;
    private int topRightZ;

    public ElevatorDTO(int elevatorId, String worldName, int currentY,
                       int bottomLeftX, int bottomLeftY, int bottomLeftZ,
                       int topRightX, int topRightY, int topRightZ) {
        this.elevatorId = elevatorId;
        this.worldName = worldName;
        this.currentY = currentY;
        this.bottomLeftX = bottomLeftX;
        this.bottomLeftY = bottomLeftY;
        this.bottomLeftZ = bottomLeftZ;
        this.topRightX = topRightX;
        this.topRightY = topRightY;
        this.topRightZ = topRightZ;
    }

    public ElevatorDTO(String worldName, int currentY, int bottomLeftX, int bottomLeftY, int bottomLeftZ,
                       int topRightX, int topRightY, int topRightZ) {
        this(-1, worldName, currentY, bottomLeftX, bottomLeftY, bottomLeftZ, topRightX, topRightY, topRightZ);
    }

    public List<Location> getElevatorLocations() {
       return PLElevatorManager.getLocations(this.worldName, this.bottomLeftX, this.bottomLeftY, this.bottomLeftZ,
               this.topRightX, this.topRightY, this.topRightZ);
    }

    public List<Floor> getFloors(Elevator elevator) {
        return ElevatorFloorDAO.getFloors(this.elevatorId).stream()
                .map(e -> new PLFloor(e, this.worldName, elevator))
                .collect(Collectors.toList());
    }

    public ElevatorSettingsDTO getSettings() {
        if (this.settingsDTO == null) {
            this.settingsDTO = ElevatorSettingsDAO.getSettings(this.elevatorId);
        }

        return this.settingsDTO;
    }

    public void increaseY() {
        this.currentY++;
        this.bottomLeftY++;
        this.topRightY++;
    }

    public void decreaseY() {
        this.currentY--;
        this.bottomLeftY--;
        this.topRightY--;
    }

}
