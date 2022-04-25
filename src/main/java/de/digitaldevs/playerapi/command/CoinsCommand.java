package de.digitaldevs.playerapi.command;


import de.digitaldevs.playerapi.PlayerAPI;
import de.digitaldevs.playerapi.api.PlayerRegistry;
import de.digitaldevs.playerapi.api.player.GamePlayer;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@AllArgsConstructor
public class CoinsCommand implements CommandExecutor {

    private final PlayerAPI plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Du bist kein Spieler!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            final PlayerRegistry registry = this.plugin.getPlayerRegistry();
            final GamePlayer gamePlayer = registry.getPlayerByUUID(player.getUniqueId());

            if (gamePlayer == null) {
                player.sendMessage("§7[§3C§bl§3i§bc§3k§bM§3C§7] §cDa hat wohl was nicht geklappt :(");
                return true;
            }
            final int coins = gamePlayer.getCoinsAsync();
            player.sendMessage("§7[§3C§bl§3i§bc§3k§bM§3C§7] Du hast aktuell §e" + this.format(coins) + " §7Coins.");

        }

        return true;
    }

    private String format(int input) {
        final DecimalFormatSymbols format = new DecimalFormatSymbols(Locale.GERMANY);
        final DecimalFormat decimalFormat = new DecimalFormat("##,###", format);
        return decimalFormat.format(input);
    }

}
