package mc.unraveled.reforged.storage;

import lombok.SneakyThrows;
import mc.unraveled.reforged.data.InfractionData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBInfraction {
    private final String INSERT = "INSERT INTO infractions (uuid, infractions, muted, frozen, locked, jailed) VALUES (?, ?, ?, ?, ?, ?);";
    private final String UPDATE = "UPDATE infractions SET infractions = ?, muted = ?, frozen = ?, locked = ?, jailed = ? WHERE uuid = ?;";
    private final String SELECT = "SELECT * FROM infractions;";
    private final String SELECT_UUID = "SELECT * FROM infractions WHERE uuid = ?;";
    private final String DELETE = "DELETE FROM infractions WHERE uuid = ?;";
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS infractions (uuid VARCHAR(36), infractions INT, muted BOOLEAN, frozen BOOLEAN, locked BOOLEAN, jailed BOOLEAN, PRIMARY KEY (uuid));";

    private final Connection connection;

    public DBInfraction(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    public void createTable() {
        PreparedStatement statement = connection.prepareStatement(CREATE_TABLE);
        statement.executeUpdate();
    }

    @SneakyThrows
    public void insert(Player player, InfractionData data) {
        PreparedStatement statement = connection.prepareStatement(SQLConst.IF("infractions", "uuid") +
                SQLConst.BEGIN +
                INSERT +
                SQLConst.END_SPACE +
                SQLConst.ELSE +
                UPDATE +
                SQLConst.END +
                SQLConst.SEMICOLON);
        statement.setString(1, player.getUniqueId().toString());
        statement.setInt(2, data.getInfractions());
        statement.setBoolean(3, data.isMuted());
        statement.setBoolean(4, data.isFrozen());
        statement.setBoolean(5, data.isLocked());
        statement.setBoolean(6, data.isJailed());
        statement.executeUpdate();

    }

    @SneakyThrows
    public void eject(Player player) {
        PreparedStatement statement = connection.prepareStatement(DELETE);
        statement.setString(1, player.getUniqueId().toString());
        statement.executeUpdate();
    }

    @SneakyThrows
    public List<InfractionData> getStoredInfractionsFromUUID() {
        List<InfractionData> temp = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement(SELECT);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
            int infractions = rs.getInt("infractions");
            boolean muted = rs.getBoolean("muted");
            boolean frozen = rs.getBoolean("frozen");
            boolean locked = rs.getBoolean("locked");
            boolean jailed = rs.getBoolean("jailed");

            temp.add(new InfractionData(player, infractions, muted, frozen, locked, jailed));
        }
        return temp;
    }

    @SneakyThrows
    public InfractionData getInfraction(String uuid) {
        PreparedStatement statement = connection.prepareStatement(SELECT_UUID);
        statement.setString(1, uuid);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return new InfractionData(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid"))), rs.getInt("infractions"), rs.getBoolean("muted"), rs.getBoolean("frozen"), rs.getBoolean("locked"), rs.getBoolean("jailed"));
        }
        return new InfractionData(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
    }

    @SneakyThrows
    public void close() {
        connection.close();
    }
}
