package me.gamersclub.playerwarps.storage;

import me.gamersclub.playerwarps.config.ConfigMgr;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLStorage extends SQLiteStorage {
    @Override
    public void initStorage() {
        try {
            String host = ConfigMgr.getConfigString("settings.storage.address");
            int port = ConfigMgr.getConfigInt("settings.storage.port");
            String user = ConfigMgr.getConfigString("settings.storage.user");
            String pass = ConfigMgr.getConfigString("settings.storage.pass");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbName,user,pass);

            Statement initDatabase = connection.createStatement();
            initDatabase.executeUpdate(initDB);
            initDatabase.close();
        } catch (ClassNotFoundException | SQLException e) {
            Bukkit.getLogger().warning("Unable to connect to the MySQL database!\n" + e);
        }
    }
}
