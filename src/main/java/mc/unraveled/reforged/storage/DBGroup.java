package mc.unraveled.reforged.storage;

import mc.unraveled.reforged.permission.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBGroup {
    private Connection connection;

    public DBGroup(Connection connection) {
        this.connection = connection;
    }

    public void createTable(Rank rank) {
        try (PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + rank.getAttachment().getName() + " (uuid VARCHAR(36), permissions VARCHAR(64));")) {
            statement.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to create table: " + e.getMessage());
        }
    }

    public void addPermission(Rank rank, String permission) {
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT INTO " + rank.getAttachment().getName() + " (permissions) VALUES (?);")) {
            statement.setString(1, permission);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to add permission: " + e.getMessage());
        }
    }

    public void removePermission(Rank rank, String permission) {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM " + rank.getAttachment().getName() + " WHERE permissions = ?;")) {
            statement.setString(1, permission);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to remove permission: " + e.getMessage());
        }
    }

    public List<String> getPermissions(Rank rank) {
        List<String> permissions = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT permissions FROM " + rank.getAttachment().getName() + ";")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                permissions.add(rs.getString("permissions"));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to get permissions: " + e.getMessage());
        }
        return permissions;
    }

    public void addPlayer(@NotNull Rank rank, @NotNull OfflinePlayer player) {
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT INTO " + rank.getAttachment().getName() + " (uuid) VALUES (?);")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to add player: " + e.getMessage());
        }
    }

    public void removePlayer(@NotNull Rank rank, @NotNull OfflinePlayer player) {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM " + rank.getAttachment().getName() + " WHERE uuid = ?;")) {
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to remove player: " + e.getMessage());
        }
    }

    public List<UUID> getPlayers(Rank rank) {
        List<UUID> players = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT uuid FROM " + rank.getAttachment().getName() + ";")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                players.add(uuid);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to get players: " + e.getMessage());
        }

        return players;
    }

    public void close() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to close connection: " + e.getMessage());
        }
    }

    public void open(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
