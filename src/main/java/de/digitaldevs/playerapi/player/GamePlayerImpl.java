package de.digitaldevs.playerapi.player;

import com.mojang.authlib.GameProfile;
import de.digitaldevs.database.mysql.MySQLHandler;
import de.digitaldevs.playerapi.PlayerAPI;
import de.digitaldevs.playerapi.events.CoinAmountChangeEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePlayerImpl implements GamePlayer {

    final MySQLHandler handler;
    private final GameProfile gameProfile;
    private int coins;

    public GamePlayerImpl(@NotNull final PlayerAPI plugin, @NotNull final GameProfile gameProfile) {
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
        int oldAmount = this.coins;
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(this.getUUID(), newAmount, newAmount).updateAsync();
        this.callCoinAmountChangeEvent(oldAmount, newAmount);
    }

    @Override
    public void addCoins(int addAmount) {
        int oldAmount = this.coins;
        int newAmount = oldAmount + addAmount;
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(this.getUUID(), addAmount, newAmount).updateAsync();
        this.callCoinAmountChangeEvent(oldAmount, newAmount);
    }

    @Override
    public void removeCoins(int removeAmount) {
        int oldAmount = this.coins;
        int newAmount = oldAmount - removeAmount;
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(this.getUUID(), Math.max(newAmount, 0), newAmount).updateAsync();
        this.callCoinAmountChangeEvent(oldAmount, newAmount);
    }

    @Override
    public void register() {
        if (isRegistered()) return;
        this.handler.createBuilder("INSERT INTO players (uuid) VALUES (?);").addParameters(this.getUUID()).updateAsync();
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?);").addParameters(this.getUUID(), 500).updateAsync();

    }

    @Override
    public void unregister() {
        if (!isRegistered()) return;
        this.handler.createBuilder("DELETE FROM players WHERE uuid = (?);").addParameters(this.getUUID()).updateAsync();
        this.handler.createBuilder("DELETE FROM coins WHERE uuid = (?);").addParameters(this.getUUID()).updateAsync();
    }

    @Override
    public boolean isRegistered() {
        AtomicBoolean isRegistered = new AtomicBoolean(false);
        this.handler.createBuilder("SELECT * FROM players WHERE uuid=?;").addParameters(this.getUUID()).queryAsync(result -> {
            if (result == null) return;
            try {
                String uuid = result.getString("uuid");
                if (uuid != null) isRegistered.set(true);
            } catch (SQLException ignored) {
            }
        });
        return isRegistered.get();
    }

    private void callCoinAmountChangeEvent(int oldAmount, int newAmount) {
        Bukkit.getPluginManager().callEvent(new CoinAmountChangeEvent(this, oldAmount, newAmount));
        this.coins = newAmount;
    }
}
