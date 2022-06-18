package de.pandalogic.elevator;

import de.pandalogic.elevator.database.ElevatorFloorDTO;
import de.pandalogic.elevator.database.ElevatorSettingsDTO;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Deque;
import java.util.List;

public interface Elevator {

    void start();

    void stop();

    void terminate();

    void registerFloor(ElevatorFloorDTO dto);

    void registerFloor(Floor floor);

    void unregisterFloor(int floorNumber);

    int getId();

    void addStop(Floor floor);

    void addStop(int floorNumber);

    void addStop(Block button);

    Floor getCurrentFloor();

    Floor getFloorForButton(Block button);

    Floor getTargetFloor();

    Deque<Floor> getNextStops();

    List<Floor> getFloors();

    boolean isPlayerInElevator(Player player);

    int getCurrentY();

    ElevatorSettingsDTO getSettings();

    void onPlayerQuit(Player player);

    World getWorld();
}
