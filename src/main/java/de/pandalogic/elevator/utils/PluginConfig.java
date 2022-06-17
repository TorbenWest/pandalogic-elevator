package de.pandalogic.elevator.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.BiFunction;

public interface PluginConfig {

    <T> T get();

    FileConfiguration getConfig();

    default <T> T read(String path, BiFunction<FileConfiguration, String, T> function) {
        return function.apply(this.getConfig(), path);
    }

    static void copyConfigFile(JavaPlugin plugin) throws IOException {
        PluginConfig.copyConfigFile(plugin, "config.yml");
    }

    static void copyConfigFile(JavaPlugin plugin, String filename) throws IOException {
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(filename);

        if (filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty!");
        }

        File dataFolder = plugin.getDataFolder();

        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                return;
            }
        }

        File file = new File(dataFolder, filename);

        if (file.exists()) {
            return;
        }

        InputStream configStream = plugin.getClass().getResourceAsStream("/" + filename);

        if (configStream == null) {
            return;
        }

        Files.copy(configStream, file.toPath());
        configStream.close();
    }

}
