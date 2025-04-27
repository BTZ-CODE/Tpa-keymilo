package pl.btz.tpa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.btz.tpa.config.MessagesConfig;

import java.util.UUID;

public class TPAcceptCommand implements CommandExecutor {

    private final TPACommand tpaCommand;
    private final MessagesConfig messagesConfig;

    public TPAcceptCommand(TPACommand tpaCommand, MessagesConfig messagesConfig) {
        this.tpaCommand = tpaCommand;
        this.messagesConfig = messagesConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messagesConfig.getMessage("only_players"));
            return true;
        }

        Player player = (Player) sender;
        UUID requester = tpaCommand.tpaRequests.get(player.getUniqueId());

        if (requester == null || System.currentTimeMillis() > tpaCommand.tpaTimeouts.get(player.getUniqueId())) {
            player.sendMessage(messagesConfig.getMessage("no_request"));
            return true;
        }

        Player requesterPlayer = Bukkit.getPlayer(requester);
        if (requesterPlayer == null) {
            player.sendMessage(messagesConfig.getMessage("player_not_found"));
            return true;
        }

        if ("here".equals(tpaCommand.tpaCoordinates.get(player.getUniqueId()))) {
            requesterPlayer.teleport(player.getLocation());
        } else {
            player.teleport(requesterPlayer.getLocation());
        }

        player.sendMessage(messagesConfig.getMessage("tpa_accepted").replace("{player}", requesterPlayer.getName()));
        requesterPlayer.sendMessage(messagesConfig.getMessage("tpa_accepted_by").replace("{player}", player.getName()));

        tpaCommand.tpaRequests.remove(player.getUniqueId());
        tpaCommand.tpaTimeouts.remove(player.getUniqueId());
        tpaCommand.tpaCoordinates.remove(player.getUniqueId());

        return true;
    }
}