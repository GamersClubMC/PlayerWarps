package me.gamersclub.playerwarps.storage;

import java.util.List;

public abstract class AbstractStorageManager {
    final String initDB = "CREATE TABLE IF NOT EXISTS `warps` (`uuid` char(36), `warpName` varchar(25), `color` varchar(16), `world` varchar(256), `x` DOUBLE NOT NULL, `y` DOUBLE NOT NULL, `z` DOUBLE NOT NULL, `yaw` FLOAT NOT NULL, `pitch` FLOAT NOT NULL);";
    final String getWarp = "SELECT * FROM `warps`;";
    final String makeWarp = "INSERT INTO `warps` (`uuid`, `warpName`, `color`, `world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES (?,?,?,?,?,?,?,?,?)";
    final String locToWarp = "SELECT `uuid`,`warpName`,`world`,`x`,`y`,`z`,`yaw`,`pitch` FROM `warps` WHERE `warpName`='";
    final String delWarp = "DELETE FROM `warps` WHERE `uuid`='";
    final String selOwningWarp = "SELECT COUNT(*) AS total FROM `warps` WHERE `uuid` = '";

    public abstract void initStorage();

    public abstract void closeStorage();

    public abstract void createWarp(String uuid, String name, String color,String world, double x, double y, double z, float pitch, float yaw);

    public abstract List<String> getWarps();

    public abstract List<String> listOwningWarps(String uuid);

    public abstract boolean checkWarp(String warp);

    public abstract boolean checkIfOwnWarp(String uuid, String warp);

    public abstract int getWarpCount(String uuid);

    public abstract List<String> locationToWarp(String warp);

    public abstract void deleteWarp(String uuid, String warp);
}
