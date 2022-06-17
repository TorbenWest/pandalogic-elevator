package de.pandalogic.elevator;

import de.pandalogic.elevator.commands.ElevatorCommand;
import de.pandalogic.elevator.listener.ElevatorInventoryListener;
import de.pandalogic.elevator.listener.PlayerInteractListener;
import de.pandalogic.elevator.listener.PlayerQuitListener;
import de.pandalogic.elevator.utils.DatabaseUtil;
import de.pandalogic.elevator.utils.PluginConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class ElevatorPlugin extends JavaPlugin {

    public static final String PREFIX = "&8● &5Elevator &8▎ ";

    @Getter
    private static ElevatorPlugin instance;

    @Getter
    private ElevatorManager manager;

    @Getter
    private DatabaseUtil databaseUtil;

    @Override
    public void onEnable() {
        instance = this;

        try {
            PluginConfig.copyConfigFile(this);
            this.databaseUtil = new DatabaseUtil(this);

            if (ElevatorConfig.MYSQL_EXECUTE_SCHEMA.get()) {
                DatabaseUtil.executeScript(this.databaseUtil.getConnection(),
                        Objects.requireNonNull(this.getClass().getResourceAsStream("/schema.sql")));
            }

            this.manager = new PLElevatorManager();
            this.manager.loadElevators();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new ElevatorInventoryListener(), this);
        Objects.requireNonNull(this.getCommand("elevator")).setExecutor(new ElevatorCommand());
    }

    @Override
    public void onDisable() {
        this.saveConfig();
        this.manager.getElevators().forEach(Elevator::terminate);
        this.databaseUtil.close();
    }

}
