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
    private final GameProfile gameProfile;
    private final int coins;

    public GamePlayerImpl(@NotNull final PlayerAPI plugin, @NotNull final GameProfile gameProfile) {
        this.plugin = plugin;
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
        final MySQLHandler handler = this.plugin.getMySQLHandler();
        final AtomicInteger amount = new AtomicInteger(0);

        handler.createBuilder("").addParameters(this.getUUID()).queryAsync(result -> {
            if (result == null) return;
            try {
                int coins = result.getInt("coins");
                amount.set(coins);
            } catch (SQLException ignored) {
            }
        });

        return amount.get();
    }
}
