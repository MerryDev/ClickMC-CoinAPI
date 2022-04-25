package de.digitaldevs.playerapi.api.player;

import com.mojang.authlib.GameProfile;
import de.digitaldevs.database.mysql.MySQLHandler;
import de.digitaldevs.playerapi.PlayerAPI;
import de.digitaldevs.playerapi.api.events.CoinAmountChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        String uuid = this.getUUID().toString();
        this.handler.createBuilder("SELECT amount FROM coins WHERE uuid=?;").addParameters(uuid).queryAsync(result -> {
            if (result == null){
                System.out.println("HAHAHAHAHA");return;}
            try {
                int coins = result.getInt("coins");
                System.out.println(coins+"HEHEHEHEHEHEHE");
                amount.set(coins);
            } catch (SQLException ignored) {
            }
        });

        return amount.get();
    }

    @Override
    public void setCoins(int newAmount) {
        int oldAmount = this.coins;
        String uuid = this.getUUID().toString();
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(uuid, newAmount, newAmount).updateAsync();
        this.callCoinAmountChangeEvent(oldAmount, newAmount);
    }

    @Override
    public void addCoins(int addAmount) {
        int oldAmount = this.coins;
        int newAmount = oldAmount + addAmount;
        String uuid = this.getUUID().toString();
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(uuid, addAmount, newAmount).updateAsync();
        this.callCoinAmountChangeEvent(oldAmount, newAmount);
    }

    @Override
    public void removeCoins(int removeAmount) {
        int oldAmount = this.coins;
        int newAmount = oldAmount - removeAmount;
        String uuid = this.getUUID().toString();
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?) ON DUPLICATE KEY UPDATE amount = ?;").addParameters(uuid, Math.max(newAmount, 0), newAmount).updateAsync();
        this.callCoinAmountChangeEvent(oldAmount, newAmount);
    }

    @Override
    public void register() {
        if (isRegistered()) return;
        String uuid = this.getUUID().toString();
        this.handler.createBuilder("INSERT INTO players (uuid) VALUES (?);").addParameters(uuid).updateAsync();
        this.handler.createBuilder("INSERT INTO coins (uuid, amount) VALUES (?, ?);").addParameters(uuid, 500).updateAsync();
        System.out.println(this.getUUID());
        System.out.println(this.getUUID().toString());

    }

    @Override
    public void unregister() {
        if (!isRegistered()) return;
        String uuid = this.getUUID().toString();
        this.handler.createBuilder("DELETE FROM players WHERE uuid = (?);").addParameters(uuid).updateAsync();
        this.handler.createBuilder("DELETE FROM coins WHERE uuid = (?);").addParameters(uuid).updateAsync();
    }

    @Override
    public boolean isRegistered() {
        String playerUUID = this.getUUID().toString();
        AtomicBoolean isRegistered = new AtomicBoolean(false);
        this.handler.createBuilder("SELECT * FROM players WHERE uuid=?;").addParameters(playerUUID).queryAsync(result -> {
            if (result == null) return;
            try {
                String uuid = result.getString("uuid");
                if (uuid != null) isRegistered.set(true);
            } catch (SQLException ignored) {
            }
        });
        return isRegistered.get();
    }

    @Nullable
    @Override
    public Player getBukkitPlayer() {
        String uuid = this.getUUID().toString();
        return Bukkit.getPlayer(uuid);
    }

    private void callCoinAmountChangeEvent(int oldAmount, int newAmount) {
        Bukkit.getPluginManager().callEvent(new CoinAmountChangeEvent(this, oldAmount, newAmount));
        this.coins = newAmount;
    }
}
