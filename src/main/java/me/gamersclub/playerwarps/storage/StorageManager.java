package me.gamersclub.playerwarps.storage;

import me.gamersclub.playerwarps.PwarpMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorageManager {
    static final SQLiteStorage sqLite = new SQLiteStorage();
    static final MySQLStorage mySQL = new MySQLStorage();
    static final H2Storage h2 = new H2Storage();

    public static void initStorage() {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            sqLite.initStorage();
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            h2.initStorage();
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            mySQL.initStorage();
        }
    }

    public static void closeStorage() {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            sqLite.closeStorage();
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            h2.closeStorage();
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            mySQL.closeStorage();
        }
    }

    public static void addWarp(String uuid, String name, String color,String world, double x, double y, double z, float yaw, float pitch) {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            sqLite.createWarp(uuid, name, color, world, x, y, z, yaw, pitch);
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            h2.createWarp(uuid, name, color, world, x, y, z, yaw, pitch);
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            mySQL.createWarp(uuid, name, color, world, x, y, z, yaw, pitch);
        }
    }

    public static boolean checkWarp(String warpName) {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            return sqLite.checkWarp(warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            return h2.checkWarp(warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            return mySQL.checkWarp(warpName);
        }
        return false;
    }

    public static boolean checkIfOwnerOfWarp(String uuid,String warpName) {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            return sqLite.checkIfOwnWarp(uuid,warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            return h2.checkIfOwnWarp(uuid, warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            return mySQL.checkIfOwnWarp(uuid,warpName);
        }
        return false;
    }

    public static Location getWarp(Player player, String warpName) {
        List<String> warpInfo = new ArrayList<>();
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            warpInfo = sqLite.locationToWarp(warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            warpInfo = h2.locationToWarp(warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            warpInfo = mySQL.locationToWarp(warpName);
        }

        //more reliable to get their username through their uuid in case of name changes
        UUID uuid = UUID.fromString(warpInfo.get(0));
        String username = Bukkit.getOfflinePlayer(uuid).getName();

        String name = warpInfo.get(1);
        String world = warpInfo.get(2);
        double x = Double.parseDouble(warpInfo.get(3));
        double y = Double.parseDouble(warpInfo.get(4));
        double z = Double.parseDouble(warpInfo.get(5));
        float yaw = Float.parseFloat(warpInfo.get(6));
        float pitch = Float.parseFloat(warpInfo.get(7));

        player.sendMessage("§bYou were teleported to §r" + name + " §bby §r" + username + "§b!");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }


    public static List<String> getOwningWarps(String uuid) {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            return sqLite.listOwningWarps(uuid);
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            return h2.listOwningWarps(uuid);
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            return mySQL.listOwningWarps(uuid);
        }

        return new ArrayList<>();
    }

    public static int getWarpCountNumber(String uuid) {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            return sqLite.getWarpCount(uuid);
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            return h2.getWarpCount(uuid);
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            return mySQL.getWarpCount(uuid);
        }
        return 0;
    }

    public static List<String> listWarps() {
        List<String> warpList = new ArrayList<>();

        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            warpList = sqLite.getWarps();
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            warpList = h2.getWarps();
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            warpList = mySQL.getWarps();
        }

        for (int i = 0; i < warpList.size(); i++) {
            String cache = warpList.get(i);

            //I hate this if statement its so big
            if (cache.contains("BLACK")) {
                warpList.set(i,cache.replace("BLACK ","&0"));
            } else if (cache.contains("DARK_BLUE")) {
                warpList.set(i,cache.replace("DARK_BLUE ","&1"));
            } else if (cache.contains("DARK_GREEN")) {
                warpList.set(i,cache.replace("DARK_GREEN ","&2"));
            } else if (cache.contains("DARK_AQUA")) {
                warpList.set(i,cache.replace("DARK_AQUA ","&3"));
            } else if (cache.contains("DARK_RED")) {
                warpList.set(i,cache.replace("DARK_RED ","&4"));
            } else if (cache.contains("DARK_PURPLE")) {
                warpList.set(i,cache.replace("DARK_PURPLE ","&5"));
            } else if (cache.contains("DARK_GRAY")) {
                warpList.set(i,cache.replace("DARK_GRAY ","&8"));
            } else if (cache.contains("GOLD")) {
                warpList.set(i,cache.replace("GOLD ","&6"));
            } else if (cache.contains("GRAY")) {
                warpList.set(i,cache.replace("GRAY ","&7"));
            } else if (cache.contains("BLUE")) {
                warpList.set(i,cache.replace("BLUE ","&9"));
            } else if (cache.contains("GREEN")) {
                warpList.set(i,cache.replace("GREEN ","&a"));
            } else if (cache.contains("AQUA")) {
                warpList.set(i,cache.replace("AQUA ","&b"));
            } else if (cache.contains("RED")) {
                warpList.set(i,cache.replace("RED ","&c"));
            } else if (cache.contains("LIGHT_PURPLE")) {
                warpList.set(i,cache.replace("LIGHT_PURPLE ","&d"));
            } else if (cache.contains("YELLOW")) {
                warpList.set(i,cache.replace("YELLOW ","&e"));
            } else if (cache.contains("WHITE")) {
                warpList.set(i,cache.replace("WHITE ","&f"));
            }
        }

        return warpList;
    }

    public static void removeWarp(String uuid, String warpName) {
        if (PwarpMain.storage.equalsIgnoreCase("sqlite")) {
            sqLite.deleteWarp(uuid, warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("h2")) {
            h2.deleteWarp(uuid, warpName);
        } else if (PwarpMain.storage.equalsIgnoreCase("mysql")) {
            mySQL.deleteWarp(uuid, warpName);
        }
    }

}
