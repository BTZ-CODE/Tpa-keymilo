package pl.btz.tpa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.btz.tpa.config.MessagesConfig;

import java.util.UUID;

public class TPADeclineCommand implements CommandExecutor {

    private final TPACommand tpaCommand;
    private final MessagesConfig messagesConfig;

    public TPADeclineCommand(TPACommand tpaCommand, MessagesConfig messagesConfig) {
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

        if (requester == null) {
            player.sendMessage(messagesConfig.getMessage("no_request"));
            return true;
        }

        tpaCommand.tpaRequests.remove(player.getUniqueId());
        tpaCommand.tpaTimeouts.remove(player.getUniqueId());
        tpaCommand.tpaCoordinates.remove(player.getUniqueId());

        player.sendMessage(messagesConfig.getMessage("tpa_declined"));
        return true;
    }
}