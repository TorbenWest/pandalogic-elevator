package de.pandalogic.elevator.database;

import de.pandalogic.elevator.PLElevatorManager;
import de.pandalogic.elevator.utils.DatabaseUtil;
import org.bukkit.block.BlockFace;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorFloorDAO {

    private static final String TABLE_NAME = "elevator_floor";

    public static List<ElevatorFloorDTO> getFloors(int elevatorId) {
        List<ElevatorFloorDTO> result = new ArrayList<>();
        String qry = "SELECT * FROM " + TABLE_NAME + " WHERE elevator_id = " + elevatorId + ";";

        try {
            ResultSet set = DatabaseUtil.getResultSet(qry);

            while (set.next()) {
                int elevatorFloorId = set.getInt("elevator_floor_id");
                int floorNumber = set.getInt("floor_number");
                int baseY = set.getInt("base_y");
                int buttonX = set.getInt("button_x");
                int buttonY = set.getInt("button_y");
                int buttonZ = set.getInt("button_z");
                int gateBottomLeftX = set.getInt("gate_bottom_left_x");
                int gateBottomLeftY = set.getInt("gate_bottom_left_y");
                int gateBottomLeftZ = set.getInt("gate_bottom_left_z");
                int gateTopRightX = set.getInt("gate_top_right_x");
                int gateTopRightY = set.getInt("gate_top_right_y");
                int gateTopRightZ = set.getInt("gate_top_right_z");
                String outputDirection = set.getString("output_direction");
                result.add(new ElevatorFloorDTO(elevatorFloorId, elevatorId, floorNumber, baseY,
                        buttonX, buttonY, buttonZ, gateBottomLeftX, gateBottomLeftY, gateBottomLeftZ,
                        gateTopRightX, gateTopRightY, gateTopRightZ, BlockFace.valueOf(outputDirection)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void addFloor(ElevatorFloorDTO dto) {
        String qry = "INSERT INTO " + TABLE_NAME + " (elevator_id, floor_number, base_y, button_x, " +
                "button_y, button_z, gate_bottom_left_x, gate_bottom_left_y, gate_bottom_left_z, " +
                "gate_top_right_x, gate_top_right_y, gate_top_right_z, output_direction) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement statement = DatabaseUtil.getStatement(qry);
            statement.setInt(1, dto.getElevatorId());
            statement.setInt(2, dto.getFloorNumber());
            statement.setInt(3, dto.getBaseY());
            statement.setInt(4, dto.getButtonX());
            statement.setInt(5, dto.getButtonY());
            statement.setInt(6, dto.getButtonZ());
            statement.setInt(7, dto.getGateBottomLeftX());
            statement.setInt(8, dto.getGateBottomLeftY());
            statement.setInt(9, dto.getGateBottomLeftZ());
            statement.setInt(10, dto.getGateTopRightX());
            statement.setInt(11, dto.getGateTopRightY());
            statement.setInt(12, dto.getGateTopRightZ());
            statement.setString(13, dto.getOutputDirection().toString());
            statement.executeUpdate();

            ResultSet set = statement.getGeneratedKeys();
            set.next();
            int id = set.getInt(1);

            dto.setElevatorFloorId(id);
            PLElevatorManager.findElevatorById(dto.getElevatorId()).registerFloor(dto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeFloor(int elevatorId, int floorNumber) {
        PLElevatorManager.findElevatorById(elevatorId).unregisterFloor(floorNumber);
        String qry = "DELETE FROM " + TABLE_NAME + " WHERE elevator_id = ? AND floor_number = ?;";

        try {
            PreparedStatement statement = DatabaseUtil.getStatement(qry);
            statement.setObject(1, elevatorId);
            statement.setObject(2, floorNumber);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
