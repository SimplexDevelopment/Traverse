package mc.unraveled.reforged.storage;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionHandler {
    private final DBProperties properties;

    public DBConnectionHandler(DBProperties properties) {
        this.properties = properties;
    }

    public Connection establish() {
        try {
            return DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword());
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to establish connection: " + e.getMessage());
            return null;
        }
    }

    public ContextConnection establishContext() {
        try {
            return new ContextConnection(DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword()));
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to establish connection: " + e.getMessage());
            return null;
        }
    }

    public record ContextConnection(Connection connection) implements AutoCloseable {
        @Override
        public void close() {
            try {
                connection.close();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("Failed to close connection: " + e.getMessage());
            }
        }
    }
}
