package de.pandalogic.elevator;

import de.pandalogic.elevator.database.ElevatorDAO;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PLElevatorManager implements ElevatorManager {

    private List<Elevator> elevators;

    @Override
    public void loadElevators() {
        this.elevators = ElevatorDAO.getElevators().stream().map(PLElevator::new).collect(Collectors.toList());
    }

    @Override
    public List<Elevator> getElevators() {
        return this.elevators;
    }

    public static List<Location> getLocations(String map, int bottomLeftX, int bottomLeftY, int bottomLeftZ,
                                              int topRightX, int topRightY, int topRightZ) {
        var elevator = new ArrayList<Location>();

        for (int x = Math.min(bottomLeftX, topRightX); x <= Math.max(bottomLeftX, topRightX); x++) {
            for (int y = Math.min(bottomLeftY, topRightY); y <= Math.max(bottomLeftY, topRightY); y++) {
                for (int z = Math.min(bottomLeftZ, topRightZ); z <= Math.max(bottomLeftZ, topRightZ); z++) {
                    elevator.add(new Location(Bukkit.getWorld(map), x, y, z));
                }
            }
        }

        return elevator;
    }

    public static Elevator findElevatorById(int id) {
        return ElevatorPlugin.getInstance().getManager().getElevators()
                .stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

}