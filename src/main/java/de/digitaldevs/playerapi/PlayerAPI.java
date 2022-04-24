package de.digitaldevs.playerapi;

import de.digitaldevs.database.mysql.MySQLConnector;
import de.digitaldevs.playerapi.utils.MySQLConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerAPI extends JavaPlugin {

    @Getter
    public MySQLConnector mySQLConnector;

    @Override
    public void onEnable() {
        MySQLConfig config = new MySQLConfig();
        config.initDefault();
        mySQLConnector = new MySQLConnector(config.get("Host"), config.get("Port"), config.get("Database"), config.get("Username"), config.get("Password"), true);
        mySQLConnector.openConnection();
    }

    public void onDisable() {

    }

}
