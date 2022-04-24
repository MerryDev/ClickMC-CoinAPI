package de.digitaldevs.coins.utils;

import de.digitaldevs.core.config.Config;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class MySQLConfig implements Config {

    private final File file = new File("plugins/CoinAPI/mysql.yml");
    private FileConfiguration configuration;

    @SneakyThrows
    @Override
    public Config create() {
        if(!this.file.exists()) {
            this.file.createNewFile();
        }
        return this;
    }

    @Override
    public void initDefault() {
        if(!this.file.exists()) { // File existiert noch nicht --> Standardwerte setzen
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
            this.configuration.set("Host", "10.27.27.2");
            this.configuration.set("Port", "3303");
            this.configuration.set("Database", "minecraft");
            this.configuration.set("Username", "root");
            this.configuration.set("Password", "N^^R@2#h79@P!XNTeXvFMuhavtcUB8mr##QndkG#GbwBC%vJNioQ3o6PW8R93MmDE3BR%jaMPEm9BnLT7GCZ3mTS#@VANpYytgTun^cN4Hvk#C8QSNj7Dh%u!Ey@i5q!");
            this.save();
        }
        this.configuration = YamlConfiguration.loadConfiguration(this.file); // File existiert schon
    }

    @Override
    public String get(@NotNull String path) {
        return this.configuration.getString(path);
    }

    @Override
    public void set(@NotNull String path, Object object) {
        this.configuration.set(path, object);
    }

    @Override
    public void save() {
        try {
            this.configuration.save(this.file);
        } catch(Exception ignored) {}
    }
}
