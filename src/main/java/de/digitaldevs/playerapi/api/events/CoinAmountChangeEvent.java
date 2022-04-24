package de.digitaldevs.playerapi.api.events;

import de.digitaldevs.playerapi.api.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CoinAmountChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final GamePlayer gamePlayer;
    private final int oldCoinAmount;
    private final int newCoinAmount;

    public CoinAmountChangeEvent(@NotNull final GamePlayer gamePlayer, int oldCoinAmount, int newCoinAmount) {
        this.gamePlayer = gamePlayer;
        this.oldCoinAmount = oldCoinAmount;
        this.newCoinAmount = newCoinAmount;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getOldCoinAmount() {
        return oldCoinAmount;
    }

    public int getNewCoinAmount() {
        return newCoinAmount;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
