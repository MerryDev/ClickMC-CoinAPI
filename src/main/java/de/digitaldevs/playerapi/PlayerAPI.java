package de.digitaldevs.playerapi;

import de.digitaldevs.database.mysql.MySQLHandler;
import de.digitaldevs.playerapi.utils.MySQLConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerAPI extends JavaPlugin {

    @Getter
    private MySQLHandler mySQLHandler;
    @Override
    public void onEnable() {
        final MySQLConfig config = new MySQLConfig();
        config.initDefault();
        mySQLHandler = new MySQLHandler(config.get("Host"), config.get("Port"), config.get("Database"), config.get("Username"), config.get("Password"),true);
        mySQLHandler.openConnection();
    }

    public void onDisable() {

    }
}
