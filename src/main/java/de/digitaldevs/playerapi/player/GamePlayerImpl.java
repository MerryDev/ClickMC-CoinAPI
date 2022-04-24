package de.digitaldevs.playerapi.player;

import com.mojang.authlib.GameProfile;
import de.digitaldevs.database.mysql.MySQLHandler;
import de.digitaldevs.playerapi.PlayerAPI;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePlayerImpl implements GamePlayer {

    private final PlayerAPI plugin;
    final MySQLHandler handler;
    private final GameProfile gameProfile;
    private final int coins;



    public GamePlayerImpl(@NotNull final PlayerAPI plugin, @NotNull final GameProfile gameProfile) {
        this.plugin = plugin;
        this.handler = plugin.getMySQLHandler();
        this.gameProfile = gameProfile;
        this.coins = this.getCoinsAsync();
    }



    @Override
    public UUID getUUID() {
        return this.gameProfile.getId();
    }

    @Override
    public int getCoins() {
        return this.coins;
    }

    @Override
    public int getCoinsAsync() {
        final AtomicInteger amount = new AtomicInteger(0);

        this.handler.createBuilder("SELECT amount FROM coins WHERE uuid=?;").addParameters(this.getUUID()).queryAsync(result -> {
            if (result == null) return;
            try {
                int coins = result.getInt("coins");
                amount.set(coins);
            } catch (SQLException ignored) {
            }
        });

        return amount.get();
    }

    @Override
    public void setCoins(int newAmount) {
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(this.getUUID(), newAmount, newAmount).updateAsync();
    }

    @Override
    public void addCoins(int addAmount) {
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = amount+?;").addParameters(this.getUUID(), addAmount, addAmount).updateAsync();
    }

    @Override
    public void removeCoins(int removeAmount) {
        final MySQLHandler handler = this.plugin.getMySQLHandler();

        if(getCoins()-removeAmount < 0) {
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(this.getUUID(), removeAmount, removeAmount).updateAsync();
        } else {
            setCoins(0);
        }
    }
}
