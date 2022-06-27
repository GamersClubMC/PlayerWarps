package me.gamersclub.playerwarps;

import me.gamersclub.playerwarps.cmds.DeletePlayerWarp;
import me.gamersclub.playerwarps.cmds.PlayerWarp;
import me.gamersclub.playerwarps.cmds.PlayerWarps;
import me.gamersclub.playerwarps.cmds.SetPlayerWarp;
import me.gamersclub.playerwarps.storage.StorageManager;
import me.gamersclub.playerwarps.util.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PwarpMain extends JavaPlugin {
    public static String storage; //what storage type we use
    public static List<String> colors = new ArrayList<>(); //list of valid colors for warp
    public static List<String> mainPwCmdList = new ArrayList<>();
    public static List<String> pwsCmdList = new ArrayList<>();
    public static List<String> setCmdList = new ArrayList<>();
    public static List<String> delCmdList = new ArrayList<>();
    private static final String[] pw = {"/pwarp ", "/pw ", "/warp "};
    private static final String[] list = {"/pwarps ", "/pws ", "/warps "};
    private static final String[] set = {"/setpwarp ", "/setpw", "/setwarp ", "/createpw ", "/createpwarp ", "/createwarp ", "/pwarp set ", "/pw set ", "/warp set "};
    private static final String[] del = {"/delpwarp ", "/delpw ", "/delwarp ", "/deletepwarp ", "/deletepw ", "/deletewarp ", "/rmpwarp ", "/rmpw ","/rmwarp ","/pwarp del ","/pwarp delete ", "/pwarp remove ", "/pwarp rm ", "/pw del ", "/pw delete ", "/pw remove ", "/pw rm ", "/warp del ", "/warp delete ", "/warp remove ", "/warp rm "};
    private static final String[] color = {"BLACK","DARK_BLUE","DARK_GREEN","DARK_AQUA","DARK_RED","GOLD","GRAY","DARK_GRAY","BLUE","GREEN","AQUA","RED","LIGHT_PURPLE","YELLOW","WHITE"};

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[PlayerWarps] ====================");
        Bukkit.getLogger().info("[PlayerWarps] PlayerWarps v1.0.0");
        Bukkit.getLogger().info("[PlayerWarps] ====================");

        //config checker
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        if (!isConfigUpToDate()) {
            Bukkit.getLogger().warning("[PlayerWarps] Your configuration is out of date!");
            Bukkit.getLogger().warning("[PlayerWarps] Please regenerate it when possible!");
        }

        //storage initializer
        storage = this.getConfig().getString("settings.storage-type");
        StorageManager.initStorage();

        //initialize all variables
        colors.addAll(Arrays.asList(color));
        mainPwCmdList.addAll(Arrays.asList(pw));
        pwsCmdList.addAll(Arrays.asList(list));
        setCmdList.addAll(Arrays.asList(set));
        delCmdList.addAll(Arrays.asList(del));

        //Register commands
        Objects.requireNonNull(this.getCommand("pwarp")).setExecutor(new PlayerWarp()); //Main pwarp command
        Objects.requireNonNull(this.getCommand("pwarps")).setExecutor(new PlayerWarps()); //list all pws
        Objects.requireNonNull(this.getCommand("setpwarp")).setExecutor(new SetPlayerWarp()); //set pw
        Objects.requireNonNull(this.getCommand("delpwarp")).setExecutor(new DeletePlayerWarp()); //delete pw

        this.getServer().getPluginManager().registerEvents(new TabComplete(), this);
        Bukkit.getLogger().info("[PlayerWarps] Enabled " + this.getName() + " successfully!");
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");

        StorageManager.closeStorage();
    }

    //update version when major change to config
    public boolean isConfigUpToDate() {
        return this.getConfig().getInt("settings.config-version") == 1;
    }

}
