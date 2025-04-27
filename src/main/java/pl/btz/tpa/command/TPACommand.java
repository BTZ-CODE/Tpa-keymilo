package pl.btz.tpa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.btz.tpa.Main;
import pl.btz.tpa.config.MessagesConfig;

import java.util.HashMap;
import java.util.UUID;

public class TPACommand implements CommandExecutor {

    private final Main plugin;
    private final MessagesConfig messagesConfig;
    public final HashMap<UUID, UUID> tpaRequests = new HashMap<>();
    public final HashMap<UUID, Long> tpaTimeouts = new HashMap<>();
    public final HashMap<UUID, String> tpaCoordinates = new HashMap<>();

    public TPACommand(Main plugin, MessagesConfig messagesConfig) {
        this.plugin = plugin;
        this.messagesConfig = messagesConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messagesConfig.getMessage("only_players"));
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("tpa") || label.equalsIgnoreCase("tpahere")) {
            if (args.length != 1) {
                player.sendMessage(messagesConfig.getMessage("usage_tpa"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(messagesConfig.getMessage("player_not_found"));
                return true;
            } else if (target == player) {
                player.sendMessage(messagesConfig.getMessage("cannot_tpa_self"));
                return true;
            } else if (tpaRequests.containsKey(target.getUniqueId())) {
                player.sendMessage(messagesConfig.getMessage("already_requested").replace("{player}", target.getName()));
                return true;
            } else if (tpaRequests.containsKey(player.getUniqueId())) {
                player.sendMessage(messagesConfig.getMessage("already_requested_by").replace("{player}", target.getName()));
                return true;
            }

            tpaRequests.put(target.getUniqueId(), player.getUniqueId());
            tpaTimeouts.put(target.getUniqueId(), System.currentTimeMillis() + 60000);
            tpaCoordinates.put(target.getUniqueId(), label.equalsIgnoreCase("tpahere") ? "here" : "to");

            player.sendMessage(messagesConfig.getMessage("tpa_sent").replace("{player}", target.getName()));
            target.sendMessage(messagesConfig.getMessage("tpa_received").replace("{player}", player.getName()));
        }

        return true;
    }
}