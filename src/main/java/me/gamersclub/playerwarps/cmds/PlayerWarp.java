package me.gamersclub.playerwarps.cmds;

import me.gamersclub.playerwarps.PwarpMain;
import me.gamersclub.playerwarps.config.ConfigMgr;
import me.gamersclub.playerwarps.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerWarp implements CommandExecutor {
    private static final String[] disallowed = {"set","create","del","delete","rm","remove","reload"};
    private static final String valid = "§0black§r, §1dark_blue§r, §2dark_green§r, §3dark_aqua§r, §4dark_red§r, §5dark_purple§r, §6gold§r, §7gray§r, §8dark_gray§r, §9blue§r, §agreen§r, §baqua§r, §cred§r, §dlight_purple§r, §eyellow§r, §fwhite§r";

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("pwarp")) {
            if (args.length == 0) {
                sender.sendMessage("§bThis server is running PlayerWarps v1.0");
                sender.sendMessage("§cUsage: /pwarp (warp/set/remove) [name] {color (Optional)}");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof ConsoleCommandSender) {
                    Bukkit.getPluginManager().getPlugin("PlayerWarps").reloadConfig();

                    // Get new db type
                    String confDBType = ConfigMgr.getConfigString("settings.storage-type");

                    //swap db if not same as before
                    if (!PwarpMain.storage.equalsIgnoreCase(confDBType)) {
                        sender.sendMessage(ConfigMgr.getConfigString("messages.db-change"));
                        StorageManager.closeStorage();
                        sender.sendMessage(String.format(ConfigMgr.getConfigString("messages.old-db-type"),PwarpMain.storage.toLowerCase()));
                        PwarpMain.storage = confDBType;
                        sender.sendMessage(String.format(ConfigMgr.getConfigString("messages.new-db-type"),confDBType.toLowerCase()));
                        StorageManager.initStorage();
                    }
                    //say we finish reload
                    sender.sendMessage(ConfigMgr.getConfigString("messages.reload-done"));
                }
                else if (sender.hasPermission("playerwarps.reload")) {
                    Bukkit.getPluginManager().getPlugin("PlayerWarps").reloadConfig();

                    // Get new db type
                    String confDBType = ConfigMgr.getConfigString("settings.storage-type");

                    //swap db if not same as before
                    if (!PwarpMain.storage.equalsIgnoreCase(confDBType)) {
                        sender.sendMessage(ConfigMgr.getConfigString("messages.db-change"));
                        StorageManager.closeStorage();
                        sender.sendMessage(String.format(ConfigMgr.getConfigString("messages.old-db-type"),PwarpMain.storage.toLowerCase()));
                        PwarpMain.storage = confDBType;
                        sender.sendMessage(String.format(ConfigMgr.getConfigString("messages.new-db-type"),confDBType.toLowerCase()));
                        StorageManager.initStorage();
                    }
                    //say we finish reload
                    sender.sendMessage(ConfigMgr.getConfigString("messages.reload-done"));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("list")) {
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
            } else if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("create")) {
                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(ConfigMgr.getConfigString("messages.console-sender"));
                    return true;
                }
                if (args.length == 1) {
                    sender.sendMessage(ConfigMgr.getConfigString("messages.set-pw-usage"));
                }

                for (String value : disallowed) {
                    if (args[1].equalsIgnoreCase(value)) {
                        sender.sendMessage(ConfigMgr.getConfigString("messages.invalid-name"));
                        return true;
                    }
                }

                //by default the warp list method returns a color code along with the warp name
                //to save creating additional handling logic, we strip the first 2 chars as those are the color code
                List<String> warpList = StorageManager.listWarps();
                warpList.replaceAll(s1 -> s1.substring(2));

                for (String value : warpList) {
                    if (args[1].equalsIgnoreCase(value)) {
                        sender.sendMessage(ConfigMgr.getConfigString("messages.warp-already-exists"));
                        return true;
                    }
                }

                Player player = (Player) sender;
                Location location = player.getLocation();
                String color = null;
                int currentWarps = StorageManager.getWarpCountNumber(player.getUniqueId().toString());
                int maxWarps = ConfigMgr.getConfigInt("settings.max-pwarps");
                int allowedWarps = 0;

                for (int i = maxWarps; i > 0; i--) {
                    if (sender.hasPermission("playerwarps.amount." + i)) {
                        allowedWarps = i;
                        break;
                    }
                }
                if (maxWarps == -1) {
                    //we skip permission check this way
                } else if (currentWarps >= allowedWarps) {
                    sender.sendMessage(String.format(ConfigMgr.getConfigString("messages.reached-max"),allowedWarps));
                    return true;
                }

                if (args.length == 3) {
                    if (!PwarpMain.colors.contains(args[2].toUpperCase())) {
                        sender.sendMessage(ConfigMgr.getConfigString("messages.invalid-color"));
                        sender.sendMessage(String.format(ConfigMgr.getConfigString("messages.color-list"),valid));
                        color = "WHITE";
                    } else {
                        color = args[2].toUpperCase();
                    }
                }
                if (args.length == 2 || color == null) {
                    color = "WHITE";
                }

                String uuid = player.getUniqueId().toString();
                String world = player.getWorld().getName();
                double x = location.getX();
                double y = location.getY();
                double z = location.getZ();
                float yaw = location.getYaw();
                float pitch = location.getPitch();
                StorageManager.addWarp(uuid, args[1], color, world, x, y, z, yaw, pitch);
                sender.sendMessage(ConfigMgr.getConfigString("messages.warp-made"));
                sender.sendMessage(String.format(ConfigMgr.getConfigString("messages.warp-made-ex"),args[1]));
            } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("rm") || args[0].equalsIgnoreCase("del")) {
                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(ConfigMgr.getConfigString("messages.console-sender"));
                    return true;
                }

                if (!sender.hasPermission("playerwarps.delwarp")) {
                    sender.sendMessage(ConfigMgr.getConfigString("messages.no-permission"));
                    return true;
                }

                if (args.length >= 2) {
                    Player player = (Player) sender;
                    String playerUUID = player.getUniqueId().toString();
                    String warpToRemove = args[1];

                    if (StorageManager.checkIfOwnerOfWarp(playerUUID,warpToRemove)) {
                        StorageManager.removeWarp(playerUUID,warpToRemove);
                        player.sendMessage(ConfigMgr.getConfigString("messages.warp-deleted"));
                    } else {
                        player.sendMessage(ConfigMgr.getConfigString("messages.not-warp-owner"));
                    }
                } else {
                    sender.sendMessage(ConfigMgr.getConfigString("messages.specify-warp"));
                }
            } else {
                //we check if arg is a valid warp
                if (StorageManager.checkWarp(args[0])) {
                    //if is valid warp but cmd sender is console, disallow it
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(ConfigMgr.getConfigString("messages.console-sender"));
                        return true;
                    }

                    Player player = (Player) sender;
                    Location warpLoc = StorageManager.getWarp(player,args[0]);
                    player.teleport(warpLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);

                    if (ConfigMgr.getConfigBoolean("settings.apply-resistance-on-warp")) {
                        PotionEffect potionEffect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,ConfigMgr.getConfigInt("settings.resistance-duration")*20,255,false,false,false);
                        if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                            player.addPotionEffect(potionEffect);
                        }
                    }
                } else {
                    sender.sendMessage(ConfigMgr.getConfigString("messages.invalid-warp"));
                }
            }
        }
        return true;
    }
}
