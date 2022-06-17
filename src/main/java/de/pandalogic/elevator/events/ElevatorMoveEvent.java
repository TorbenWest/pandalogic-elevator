package de.pandalogic.elevator.events;

import de.pandalogic.elevator.Elevator;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.block.BlockFace;

public class ElevatorMoveEvent extends ElevatorEvent {

    @Getter
    @NonNull
    private final BlockFace direction;

    public ElevatorMoveEvent(@NonNull Elevator elevator, @NonNull BlockFace direction) {
        super(elevator);

        if (direction != BlockFace.DOWN && direction != BlockFace.UP) {
            throw new IllegalArgumentException("BlockFace can only be DOWN or UP!");
        }

        this.direction = direction;
    }

}
