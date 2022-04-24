package de.digitaldevs.playerapi.api;

import de.digitaldevs.playerapi.api.player.GamePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerRegistry {

    private static final PlayerRegistry playerRegistry = new PlayerRegistry();

    private final List<GamePlayer> gamePlayers;

    private PlayerRegistry() {
        this.gamePlayers = new LinkedList<>();
    }

    public static synchronized PlayerRegistry getInstance() {
        return playerRegistry;
    }

    public void registerGamePlayer(@NotNull final GamePlayer gamePlayer) {
        this.gamePlayers.add(gamePlayer);
        gamePlayer.register();
    }

    public void unregisterGamePlayer(@NotNull final GamePlayer gamePlayer, boolean deletePlayerData) {
        this.gamePlayers.remove(gamePlayer);
        if (deletePlayerData) gamePlayer.unregister();
    }

    @Nullable
    public GamePlayer getPlayerByUUID(@NotNull final UUID uuid) {
        AtomicReference<GamePlayer> player = new AtomicReference<>(null);
        gamePlayers.forEach(gamePlayer -> {
            if (gamePlayer.getUUID().equals(uuid)) player.set(gamePlayer);
        });
        return player.get();
    }


}
