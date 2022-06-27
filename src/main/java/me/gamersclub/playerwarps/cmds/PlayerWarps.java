package me.gamersclub.playerwarps.cmds;

import me.gamersclub.playerwarps.config.ConfigMgr;
import me.gamersclub.playerwarps.storage.StorageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerWarps implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("pwarps")) {
            if (sender instanceof ConsoleCommandSender) {
                List<String> warpList = StorageManager.listWarps();
                String warps = "&r";

                for (int i = 0; i < warpList.size(); i++) {
                    if (!(i == 0)) {
                        warps = warps.concat("&r&8, &r" + warpList.get(i));
                    } else {
                        warps = warps.concat(" " + warpList.get(i));
                    }
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',String.format(ConfigMgr.getConfigString("messages.list-warps"),warps)));
                return true;
            }

            Player player = (Player) sender;
            player.performCommand("pwarp list");
        }
        return true;
    }
}
