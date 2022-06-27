package me.gamersclub.playerwarps.util;

import me.gamersclub.playerwarps.PwarpMain;
import me.gamersclub.playerwarps.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.List;
import java.util.Objects;

public class TabComplete implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        if (PwarpMain.mainPwCmdList.contains(event.getBuffer().toLowerCase())) {
            List<String> warpList = StorageManager.listWarps();
            warpList.replaceAll(s -> s.substring(2));
            event.setCompletions(warpList);
        } else if (PwarpMain.pwsCmdList.contains(event.getBuffer().toLowerCase())) {
            event.setCancelled(true);
        } else if (PwarpMain.setCmdList.contains(event.getBuffer().toLowerCase())) {
            event.setCancelled(true);
        } else if (PwarpMain.delCmdList.contains(event.getBuffer().toLowerCase())) {
            String player = Objects.requireNonNull(Bukkit.getPlayer(event.getSender().getName())).getUniqueId().toString();
            event.setCompletions(StorageManager.getOwningWarps(player));
        }
    }
}
