package mc.unraveled.reforged.storage;

import mc.unraveled.reforged.data.InfractionData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBInfraction {

    private final Connection connection;
    private final String INF = "infractions";

    public DBInfraction(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS infractions (uuid VARCHAR(36), infractions INT, muted BOOLEAN, frozen BOOLEAN, locked BOOLEAN, jailed BOOLEAN, PRIMARY KEY (uuid));";
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Error while creating infractions table");
        }
    }

    public void insert(Player player, InfractionData data) {
        String INSERT = "INSERT INTO infractions (uuid, infractions, muted, frozen, locked, jailed) VALUES (?, ?, ?, ?, ?, ?);";
        String UPDATE = "UPDATE infractions SET infractions = ?, muted = ?, frozen = ?, locked = ?, jailed = ? WHERE uuid = ?;";
        try (PreparedStatement statement = connection.prepareStatement(SQLConst.IF(INF, "uuid", player.getUniqueId().toString()) +
                SQLConst.BEGIN +
                INSERT +
                SQLConst.END_SPACE +
                SQLConst.ELSE +
                UPDATE +
                SQLConst.END +
                SQLConst.SEMICOLON)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setInt(2, data.getInfractions());
            statement.setBoolean(3, data.isMuted());
            statement.setBoolean(4, data.isFrozen());
            statement.setBoolean(5, data.isLocked());
            statement.setBoolean(6, data.isJailed());
            statement.setInt(7, data.getInfractions());
            statement.setBoolean(8, data.isMuted());
            statement.setBoolean(9, data.isFrozen());
            statement.setBoolean(10, data.isLocked());
            statement.setBoolean(11, data.isJailed());
            statement.setString(12, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Error while inserting " + player.getName());
        }

    }

    public void eject(Player player) {
        String DELETE = "DELETE FROM infractions WHERE uuid = ?;";
        try (PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Error while ejecting " + player.getName());
        }
    }

    public List<InfractionData> getInfractionsList() {
        List<InfractionData> temp = new ArrayList<>();

        String SELECT = "SELECT * FROM infractions;";
        try (PreparedStatement statement = connection.prepareStatement(SELECT);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                int infractions = rs.getInt(INF);
                boolean muted = rs.getBoolean("muted");
                boolean frozen = rs.getBoolean("frozen");
                boolean locked = rs.getBoolean("locked");
                boolean jailed = rs.getBoolean("jailed");

                temp.add(new InfractionData(player, infractions, muted, frozen, locked, jailed));
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Error while getting infractions list");
        }

        return temp;
    }

    public InfractionData getInfraction(String uuid) {
        String SELECT_UUID = "SELECT * FROM infractions WHERE uuid = ?;";
        try (PreparedStatement statement = connection.prepareStatement(SELECT_UUID)) {
            statement.setString(1, uuid);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new InfractionData(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid"))), rs.getInt(INF), rs.getBoolean("muted"), rs.getBoolean("frozen"), rs.getBoolean("locked"), rs.getBoolean("jailed"));
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Error while getting infraction data for " + uuid);
        }
        return new InfractionData(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Error while closing connection");
        }
    }
}
