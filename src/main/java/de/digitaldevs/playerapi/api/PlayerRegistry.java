package de.digitaldevs.playerapi.api;

import de.digitaldevs.playerapi.player.GamePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerRegistry {

    private final List<GamePlayer> gamePlayers;

    public PlayerRegistry() {
        this.gamePlayers = new LinkedList<>();
    }

    public GamePlayer getPlayerByName(@NotNull final String playerName) {
        return null;
    }

    public GamePlayer getPlayerByUUID(@NotNull final UUID uuid) {
        return null;
    }
}
