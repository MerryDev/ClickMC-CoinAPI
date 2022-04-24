package de.digitaldevs.playerapi.api;

import de.digitaldevs.playerapi.player.GamePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerRegistry {

    private static final PlayerRegistry playerRegistry = new PlayerRegistry();

    private PlayerRegistry() { this.gamePlayers = new LinkedList<>(); }

    public static final synchronized PlayerRegistry getInstance() {
        return playerRegistry;
    }

    private final List<GamePlayer> gamePlayers;



    public GamePlayer getPlayerByName(@NotNull final String playerName) {
        return null;
    }

    public GamePlayer getPlayerByUUID(@NotNull final UUID uuid) {
        return null;
    }
}
