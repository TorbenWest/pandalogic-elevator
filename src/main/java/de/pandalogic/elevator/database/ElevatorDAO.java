package de.pandalogic.elevator.database;

import de.pandalogic.elevator.ElevatorPlugin;
import de.pandalogic.elevator.PLElevator;
import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.utils.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorDAO {

    private static final String TABLE_NAME = "elevator";

    public static List<ElevatorDTO> getElevators() {
        List<ElevatorDTO> result = new ArrayList<>();
        String qry = "SELECT * FROM " + TABLE_NAME + ";";

        try {
            ResultSet set = DatabaseUtil.getResultSet(qry);

            while (set.next()) {
                int elevatorId = set.getInt("elevator_id");
                String worldName = set.getString("world_name");
                int currentY = set.getInt("current_y");
                int bottomLeftX = set.getInt("bottom_left_x");
                int bottomLeftY = set.getInt("bottom_left_y");
                int bottomLeftZ = set.getInt("bottom_left_z");
                int topRightX = set.getInt("top_right_x");
                int topRightY = set.getInt("top_right_y");
                int topRightZ = set.getInt("top_right_z");
                result.add(new ElevatorDTO(elevatorId, worldName, currentY,
                        bottomLeftX, bottomLeftY, bottomLeftZ,
                        topRightX, topRightY, topRightZ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static int addElevator(ElevatorDTO dto) {
        String qry = "INSERT INTO " + TABLE_NAME + " (world_name, current_y, bottom_left_x, bottom_left_y," +
                " bottom_left_z, top_right_x, top_right_y, top_right_z) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement statement = DatabaseUtil.getStatement(qry);
            statement.setString(1, dto.getWorldName());
            statement.setInt(2, dto.getCurrentY());
            statement.setInt(3, dto.getBottomLeftX());
            statement.setInt(4, dto.getBottomLeftY());
            statement.setInt(5, dto.getBottomLeftZ());
            statement.setInt(6, dto.getTopRightX());
            statement.setInt(7, dto.getTopRightY());
            statement.setInt(8, dto.getTopRightZ());
            statement.executeUpdate();

            ResultSet set = statement.getGeneratedKeys();
            set.next();
            int id = set.getInt(1);

            dto.setElevatorId(id);
            ElevatorPlugin.getInstance().getManager().getElevators().add(new PLElevator(dto));
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("It is not possible to save the following elevator: " + dto);
    }

    public static void updateElevatorOnTerminate(ElevatorDTO dto) {
        String qry = "UPDATE " + TABLE_NAME + " " +
                "SET current_y = ?, bottom_left_y = ?, top_right_y = ? " +
                "WHERE elevator_id = ?;";

        try {
            PreparedStatement statement = DatabaseUtil.getStatement(qry);
            statement.setInt(1, dto.getCurrentY());
            statement.setInt(2, dto.getBottomLeftY());
            statement.setInt(3, dto.getTopRightY());
            statement.setInt(4, dto.getElevatorId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeElevator(int id) {
        PLElevatorManager.findElevatorById(id).terminate();
        ElevatorPlugin.getInstance().getManager().getElevators().removeIf(e -> e.getId() == id);
        String qry = "DELETE FROM " + TABLE_NAME + " WHERE elevator_id = ?;";

        try {
            PreparedStatement statement = DatabaseUtil.getStatement(qry);
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
