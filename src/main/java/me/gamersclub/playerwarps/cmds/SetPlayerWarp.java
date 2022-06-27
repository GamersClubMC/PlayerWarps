package me.gamersclub.playerwarps.cmds;

import me.gamersclub.playerwarps.config.ConfigMgr;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetPlayerWarp implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("setpwarp")) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ConfigMgr.getConfigString("messages.console-sender"));
                return true;
            }

            if (!sender.hasPermission("playerwarps.setwarp")) {
                sender.sendMessage(ConfigMgr.getConfigString("messages.no-permission"));
                return true;
            }
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ConfigMgr.getConfigString("messages.set-pw-usage"));
            } else if (args.length == 1) {
                player.performCommand("pwarp set " + args[0]);
            } else if (args.length == 2) {
                player.performCommand("pwarp set " + args[0] + " " + args[1]);
            }
        }
        return true;
    }
}
