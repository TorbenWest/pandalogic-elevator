package de.pandalogic.elevator.database;

import de.pandalogic.elevator.PLElevatorManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "elevatorFloorId")
public class ElevatorFloorDTO {

    private int elevatorFloorId;
    private int elevatorId;
    private int floorNumber;
    private int baseY;
    private int buttonX;
    private int buttonY;
    private int buttonZ;
    private int gateBottomLeftX;
    private int gateBottomLeftY;
    private int gateBottomLeftZ;
    private int gateTopRightX;
    private int gateTopRightY;
    private int gateTopRightZ;
    private BlockFace outputDirection;

    public ElevatorFloorDTO(int elevatorFloorId, int elevatorId, int floorNumber, int baseY,
                            int buttonX, int buttonY, int buttonZ, int gateBottomLeftX,
                            int gateBottomLeftY, int gateBottomLeftZ, int gateTopRightX,
                            int gateTopRightY, int gateTopRightZ, BlockFace outputDirection) {
        this.elevatorFloorId = elevatorFloorId;
        this.elevatorId = elevatorId;
        this.floorNumber = floorNumber;
        this.baseY = baseY;
        this.buttonX = buttonX;
        this.buttonY = buttonY;
        this.buttonZ = buttonZ;
        this.gateBottomLeftX = gateBottomLeftX;
        this.gateBottomLeftY = gateBottomLeftY;
        this.gateBottomLeftZ = gateBottomLeftZ;
        this.gateTopRightX = gateTopRightX;
        this.gateTopRightY = gateTopRightY;
        this.gateTopRightZ = gateTopRightZ;
        this.outputDirection = outputDirection;
    }

    public ElevatorFloorDTO(int elevatorId, int floorNumber, int baseY, int buttonX, int buttonY, int buttonZ,
                            int gateBottomLeftX, int gateBottomLeftY, int gateBottomLeftZ, int gateTopRightX,
                            int gateTopRightY, int gateTopRightZ, BlockFace outputDirection) {
        this(-1, elevatorId, floorNumber, baseY, buttonX, buttonY, buttonZ,
                gateBottomLeftX, gateBottomLeftY, gateBottomLeftZ,
                gateTopRightX, gateTopRightY, gateTopRightZ, outputDirection);
    }

    public List<Location> getEntranceLocation(String world) {
        return PLElevatorManager.getLocations(world, this.gateBottomLeftX, this.gateBottomLeftY, this.gateBottomLeftZ,
                this.gateTopRightX, this.gateTopRightY, this.gateTopRightZ);
    }

}
