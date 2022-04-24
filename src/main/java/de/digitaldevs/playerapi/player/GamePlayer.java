package de.digitaldevs.playerapi.player;

import java.util.UUID;

public interface GamePlayer {

    UUID getUUID();

    UUID getUUIDAsync();

    int getCoins();

    int getCoinsAsync();

}
