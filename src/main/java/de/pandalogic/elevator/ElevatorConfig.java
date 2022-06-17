package de.pandalogic.elevator;

import de.pandalogic.elevator.utils.PluginConfig;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings("unchecked")
public enum ElevatorConfig implements PluginConfig {

    MYSQL_EXECUTE_SCHEMA {
        @Override
        public Boolean get() {
            return this.read("mysql.execute_schema", MemorySection::getBoolean);
        }
    },
    MYSQL_USERNAME {
        @Override
        public String get() {
            return this.read("mysql.username", MemorySection::getString);
        }
    },
    MYSQL_PASSWORD {
        @Override
        public String get() {
            return this.read("mysql.password", MemorySection::getString);
        }
    },
    MYSQL_DATABASE {
        @Override
        public String get() {
            return this.read("mysql.database", MemorySection::getString);
        }
    },
    MYSQL_HOST {
        @Override
        public String get() {
            return this.read("mysql.host", MemorySection::getString);
        }
    },
    MYSQL_PORT {
        @Override
        public String get() {
            return this.read("mysql.port", MemorySection::getString);
        }
    };

    @Override
    public FileConfiguration getConfig() {
        return ElevatorPlugin.getInstance().getConfig();
    }

}
