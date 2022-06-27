package me.gamersclub.playerwarps.storage;

import org.bukkit.Bukkit;

import java.sql.*;

public class H2Storage extends SQLiteStorage {

    @Override
    public void initStorage() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:./plugins/PlayerWarps/" + dbName + ".h2");

            Statement initDatabase = connection.createStatement();
            initDatabase.executeUpdate(initDB);
            initDatabase.close();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().warning("Unable to initialize the H2 database!\n" + e);
        }
    }
}
