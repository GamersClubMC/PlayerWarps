package me.gamersclub.playerwarps.storage;

import me.gamersclub.playerwarps.config.ConfigMgr;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteStorage extends AbstractStorageManager {
    String dbName = ConfigMgr.getConfigString("settings.storage.db-name");
    protected Connection connection;

    @Override
    public void initStorage() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/PlayerWarps/" + dbName + ".db");

            Statement initDatabase = connection.createStatement();
            initDatabase.executeUpdate(initDB);
            initDatabase.close();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().warning("Unable to initialize the SQLite database!\n" + e);
        }
    }

    @Override
    public void closeStorage() {
        try {
            if (this.connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {}
    }

    @Override
    public List<String> getWarps() {
        List<String> warp = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(getWarp);
            while (resultSet.next()) {
                warp.add(resultSet.getString("color") + " " + resultSet.getString("warpName"));
            }
            resultSet.close();
        } catch (SQLException ignored) {}

        return warp;
    }

    @Override
    public List<String> listOwningWarps(String uuid) {
        List<String> owningWarps = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `warps` WHERE `uuid`='" + uuid + "';");
            if (!resultSet.isClosed()) {
                while (resultSet.next()) {
                    owningWarps.add(resultSet.getString("warpName"));
                }
                resultSet.close();
            }
        } catch (SQLException ignored) {}
        return owningWarps;
    }

    @Override
    public boolean checkWarp(String warp) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `uuid` FROM `warps` WHERE `warpName`='" + warp + "';");
            if (!resultSet.isClosed()) {
                resultSet.close();
                return true;
            }
        } catch (SQLException ignored) {}
        return false;
    }

    @Override
    public boolean checkIfOwnWarp(String uuid,String warp) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `uuid` FROM `warps` WHERE `warpName`='" + warp + "';");
            if (!resultSet.isClosed()) {
                resultSet.next();
                String dbUUID = resultSet.getString("uuid");

                resultSet.close();
                return dbUUID.equals(uuid);
            }
        } catch (SQLException ignored) {}
        return false;
    }

    @Override
    public List<String> locationToWarp(String warp) {
        List<String> warpInfo = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(locToWarp + warp + "';");
            resultSet.next();

            warpInfo.add(resultSet.getString("uuid"));
            warpInfo.add(resultSet.getString("warpName"));
            warpInfo.add(resultSet.getString("world"));
            warpInfo.add(String.valueOf(resultSet.getDouble("x")));
            warpInfo.add(String.valueOf(resultSet.getDouble("y")));
            warpInfo.add(String.valueOf(resultSet.getDouble("z")));
            warpInfo.add(String.valueOf(resultSet.getFloat("yaw")));
            warpInfo.add(String.valueOf(resultSet.getFloat("pitch")));

            resultSet.close();
        } catch (SQLException ignored) {}
        return warpInfo;
    }

    @Override
    public int getWarpCount(String uuid) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selOwningWarp + uuid + "';");

            resultSet.next();
            int count = resultSet.getInt("total");
            resultSet.close();
            return count;
        } catch (SQLException ignored) {}
        return 0;
    }

    @Override
    public void createWarp(String uuid, String name, String color, String world, double x, double y, double z, float yaw, float pitch) {
        Bukkit.getScheduler().runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PlayerWarps")), () -> {
            try {
                PreparedStatement insert = connection.prepareStatement(makeWarp);
                insert.setString(1, uuid);
                insert.setString(2, name);
                insert.setString(3, color);
                insert.setString(4, world);
                insert.setDouble(5, x);
                insert.setDouble(6, y);
                insert.setDouble(7, z);
                insert.setFloat(8, yaw);
                insert.setFloat(9, pitch);
                insert.executeUpdate();

            } catch (SQLException ignored) {}
        });

    }

    @Override
    public void deleteWarp(String uuid, String warp) {
        Bukkit.getScheduler().runTaskAsynchronously(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PlayerWarps")), () -> {
            try {
                Statement deleteWarp = connection.createStatement();
                deleteWarp.executeUpdate(delWarp + uuid + "' AND `warpName`='" + warp + "';");
                deleteWarp.close();
            } catch (SQLException ignored) {}
        });

    }
}
