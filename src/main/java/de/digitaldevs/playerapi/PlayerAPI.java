package de.digitaldevs.playerapi;

import de.digitaldevs.database.mysql.MySQLConnector;
import de.digitaldevs.playerapi.utils.MySQLConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerAPI extends JavaPlugin {

    private MySQLConfig config;

    private MySQLConnector mySQLConnector;
    @Override
    public void onEnable() {
        this.config = new MySQLConfig();
        this.config.initDefault();
        mySQLConnector = new MySQLConnector(config.get("Host"), config.get("Port"), config.get("Database"), config.get("Username"), config.get("Password"),true);
    }

    public void onDisable() {

    }
}
