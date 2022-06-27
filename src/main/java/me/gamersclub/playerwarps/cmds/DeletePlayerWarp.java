package me.gamersclub.playerwarps.cmds;

import me.gamersclub.playerwarps.config.ConfigMgr;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeletePlayerWarp implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("delpwarp")) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ConfigMgr.getConfigString("messages.console-sender"));
                return true;
            }

            if (!sender.hasPermission("playerwarps.delwarp")) {
                sender.sendMessage(ConfigMgr.getConfigString("messages.no-permission"));
                return true;
            }

            if (args.length >= 1) {
                Player player = (Player) sender;
                player.performCommand("pwarp rm " + args[0]);
            } else {
                sender.sendMessage("§cPlease specify a warp to remove!");
            }
        }
        return true;
    }
}
