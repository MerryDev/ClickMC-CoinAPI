package de.digitaldevs.playerapi.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface GamePlayer {

    UUID getUUID();

    int getCoins();

    int getCoinsAsync();

    void setCoins(int newAmount);

    void addCoins(int addAmount);

    void removeCoins(int removeAmount);

    void register();

    void unregister();

    boolean isRegistered();

    @Nullable Player getBukkitPlayer();
}
