package de.pandalogic.elevator.utils;

import de.pandalogic.elevator.ElevatorConfig;
import de.pandalogic.elevator.ElevatorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;

public class DatabaseUtil {

    private final JavaPlugin plugin;
    private final String username;
    private final String password;
    private final String database;
    private final String host;
    private final String port;

    private Connection connection;

    public DatabaseUtil(JavaPlugin plugin) {
        this.plugin = plugin;
        this.username = ElevatorConfig.MYSQL_USERNAME.get();
        this.password = ElevatorConfig.MYSQL_PASSWORD.get();
        this.database = ElevatorConfig.MYSQL_DATABASE.get();
        this.host = ElevatorConfig.MYSQL_HOST.get();
        this.port = ElevatorConfig.MYSQL_PORT.get();
        this.connect();
    }

    public void connect() {
        if (this.isConnected()) {
            return;
        }

        try {
            Properties properties = new Properties();
            properties.put("user", this.username);
            properties.put("password", this.password);
            properties.put("autoReconnect", "true");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" +
                    this.database, properties);
            Bukkit.getLogger().log(Level.INFO, this.getPrefix() + "Successfully connected to database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (!this.isConnected()) {
            return;
        }

        try {
            this.connection.close();
            Bukkit.getLogger().log(Level.INFO, this.getPrefix() + "Successfully disconnected from database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    public Connection getConnection() {
        try {
            if (!this.connection.isValid(2)) {
                this.connection = null;
                this.connect();
            }

            return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String getPrefix() {
        return "[" + this.plugin.getName() + "] MySQL: ";
    }

    public static void executeScript(Connection connection, InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("InputStream for script is null!");
        }

        try {
            Statement statement = null;

            try (Scanner scanner = new Scanner(inputStream)) {
                scanner.useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");
                statement = connection.createStatement();

                while (scanner.hasNext()) {
                    String line = scanner.next().trim();

                    if (!line.isEmpty()) {
                        statement.execute(line);
                    }
                }
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PreparedStatement getStatement(String query) throws SQLException {
        return ElevatorPlugin.getInstance().getDatabaseUtil().getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    public static ResultSet getResultSet(String query) throws SQLException {
        return getStatement(query).executeQuery();
    }

}
