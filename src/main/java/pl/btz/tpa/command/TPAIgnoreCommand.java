package pl.btz.tpa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.btz.tpa.config.MessagesConfig;

import java.util.HashSet;
import java.util.UUID;

public class TPAIgnoreCommand implements CommandExecutor {

    private final HashSet<UUID> ignoredPlayers = new HashSet<>();
    private final MessagesConfig messagesConfig;

    public TPAIgnoreCommand(MessagesConfig messagesConfig) {
        this.messagesConfig = messagesConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messagesConfig.getMessage("only_players"));
            return true;
        }

        Player player = (Player) sender;
        if (ignoredPlayers.contains(player.getUniqueId())) {
            ignoredPlayers.remove(player.getUniqueId());
            player.sendMessage(messagesConfig.getMessage("tpa_ignore_disabled"));
        } else {
            ignoredPlayers.add(player.getUniqueId());
            player.sendMessage(messagesConfig.getMessage("tpa_ignore_enabled"));
        }

        return true;
    }

    public boolean isIgnoring(UUID playerUUID) {
        return ignoredPlayers.contains(playerUUID);
    }
}