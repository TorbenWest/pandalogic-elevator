package de.pandalogic.elevator.database;

import de.pandalogic.elevator.utils.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ElevatorSettingsDAO {

    private static final String TABLE_NAME = "elevator_settings";

    public static ElevatorSettingsDTO getSettings(int elevatorId) {
        String qry = "SELECT * FROM " + TABLE_NAME + " WHERE elevator_id = " + elevatorId + ";";

        try {
            ResultSet set = DatabaseUtil.getResultSet(qry);

            if (set.next()) {
                int elevatorFloorId = set.getInt("elevator_settings_id");
                int playerPositionCheck = set.getInt("player_position_check");
                int maxPlayer = set.getInt("max_player");
                int nextStopCheck = set.getInt("next_stop_check");
                int startDelay = set.getInt("start_delay");
                int speed = set.getInt("speed");
                return new ElevatorSettingsDTO(elevatorFloorId, elevatorId, playerPositionCheck,
                        maxPlayer, nextStopCheck, startDelay, speed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("No elevator settings found for elevator id " + elevatorId + "!");
    }

    public static void updateSettings(ElevatorSettingsDTO dto) {
        String qry = "UPDATE " + TABLE_NAME + " " +
                "SET player_position_check = ?, max_player = ?, " +
                "next_stop_check = ?, start_delay = ?, speed = ? " +
                "WHERE elevator_id = ?;";

        try {
            PreparedStatement statement = DatabaseUtil.getStatement(qry);
            statement.setInt(1, dto.getPlayerPositionCheck());
            statement.setInt(2, dto.getMaxPlayer());
            statement.setInt(3, dto.getNextStopCheck());
            statement.setInt(4, dto.getStartDelay());
            statement.setInt(5, dto.getSpeed());
            statement.setInt(6, dto.getElevatorId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
