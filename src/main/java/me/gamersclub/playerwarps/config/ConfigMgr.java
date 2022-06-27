package me.gamersclub.playerwarps.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ConfigMgr {
    private final static String NAME = "PlayerWarps";

    public static String getConfigString(String value) {
        return ChatColor.translateAlternateColorCodes('&', Bukkit.getPluginManager().getPlugin(NAME).getConfig().getString(value));
    }
    public static boolean getConfigBoolean(String value) {
        return Bukkit.getPluginManager().getPlugin(NAME).getConfig().getBoolean(value);
    }

    public static int getConfigInt(String value) {
        return Bukkit.getPluginManager().getPlugin(NAME).getConfig().getInt(value);
    }

}
