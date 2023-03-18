package mc.unraveled.reforged.storage;

import mc.unraveled.reforged.banning.AbstractBan;
import mc.unraveled.reforged.banning.SimpleBan;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBBan {
    private Connection connection;

    public DBBan(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        try (PreparedStatement statement = getConnection()
                .prepareStatement("CREATE TABLE IF NOT EXISTS bans (uuid VARCHAR(36), ip VARCHAR(16), reason VARCHAR(64), banned_by VARCHAR(16), banned_at BIGINT, expires_at BIGINT, active BOOLEAN, PRIMARY KEY (uuid));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public void insert(@NotNull AbstractBan ban) {
        try (PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO bans (uuid, ip, reason, banned_by, banned_at, expires_at, active) VALUES (?, ?, ?, ?, ?, ?, ?);")) {
            statement.setString(1, ban.getUuid());
            statement.setString(2, ban.getIp());
            statement.setString(3, ban.getReason());
            statement.setString(4, ban.getSource());
            statement.setLong(5, ban.getPropagated());
            statement.setLong(6, ban.getExpiry());
            statement.setBoolean(7, ban.isActive());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    @Nullable
    public AbstractBan getBan(@NotNull UUID uuid) {
        try (PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM bans WHERE uuid = ?;")) {
            statement.setString(1, uuid.toString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new SimpleBan(rs.getString("uuid"),
                            rs.getString("ip"),
                            rs.getString("reason"),
                            rs.getString("banned_by"),
                            rs.getLong("banned_at"),
                            rs.getLong("expires_at"),
                            rs.getBoolean("active"));
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }

        return null;
    }

    public List<AbstractBan> all() {
        List<AbstractBan> bans = new ArrayList<>();
        try (PreparedStatement statement = getConnection()
                .prepareStatement("SELECT * FROM bans;")) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    bans.add(new SimpleBan(rs.getString("uuid"),
                            rs.getString("ip"),
                            rs.getString("reason"),
                            rs.getString("banned_by"),
                            rs.getLong("banned_at"),
                            rs.getLong("expires_at"),
                            rs.getBoolean("active")));
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
        return bans;
    }

    public void delete(@NotNull AbstractBan ban) {
        try (PreparedStatement statement = getConnection()
                .prepareStatement("DELETE FROM bans WHERE uuid = ?;")) {
            statement.setString(1, ban.getUuid());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public void deleteAll() {
        try (PreparedStatement statement = getConnection()
                .prepareStatement("DELETE FROM bans;")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    public void close() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
        this.connection = null;
    }

    public void open(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
