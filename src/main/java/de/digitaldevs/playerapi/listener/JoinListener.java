package de.digitaldevs.playerapi.listener;

import com.mojang.authlib.GameProfile;
import de.digitaldevs.playerapi.PlayerAPI;
import de.digitaldevs.playerapi.api.PlayerRegistry;
import de.digitaldevs.playerapi.api.player.GamePlayer;
import de.digitaldevs.playerapi.api.player.GamePlayerImpl;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final PlayerAPI plugin;

    public JoinListener(PlayerAPI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerRegistry registry = this.plugin.getPlayerRegistry();
        final GamePlayer gamePlayer = registry.getPlayerByUUID(player.getUniqueId());
        GameProfile gameProfile = ((CraftPlayer) player).getProfile();


        if(!gamePlayer.isRegistered()) {
            this.plugin.getPlayerRegistry().registerGamePlayer(new GamePlayerImpl(this.plugin, gameProfile));
        }
    }

}
