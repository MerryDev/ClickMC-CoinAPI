package de.digitaldevs.playerapi.player;

import java.util.UUID;

public interface GamePlayer {

    UUID getUUID();

    int getCoins();

    int getCoinsAsync();

}
