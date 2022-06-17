package de.pandalogic.elevator;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

public interface Floor extends Comparable<Floor> {

    BlockFace getOutputDirection();

    int getNumber();

    Location getButton();

    List<Location> getEntrance();

    int getY();

    void close(List<Player> passengers);

    void open();

    boolean isOpen();

    default boolean isClosed() {
        return !this.isOpen();
    }

}
