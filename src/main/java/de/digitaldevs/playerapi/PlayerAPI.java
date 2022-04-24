package de.digitaldevs.playerapi;

import de.digitaldevs.database.mysql.MySQLHandler;
import de.digitaldevs.playerapi.api.PlayerRegistry;
import de.digitaldevs.playerapi.listener.JoinListener;
import de.digitaldevs.playerapi.utils.MySQLConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerAPI extends JavaPlugin {

    @Getter
    private MySQLHandler mySQLHandler;

    @Getter
    private PlayerRegistry playerRegistry;

    @Override
    public void onEnable() {
        final MySQLConfig config = new MySQLConfig();
        config.initDefault();
        mySQLHandler = new MySQLHandler(config.get("Host"), config.get("Port"), config.get("Database"), config.get("Username"), config.get("Password"), true);
        mySQLHandler.openConnection();

        if (this.mySQLHandler.isConnected()) {
            this.playerRegistry = PlayerRegistry.getInstance();
            this.registerListener();
        }
    }

    public void onDisable() {
        if (this.mySQLHandler.isConnected()) this.mySQLHandler.closeConnection();
    }

    private void registerListener() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinListener(this), this);
    }
}