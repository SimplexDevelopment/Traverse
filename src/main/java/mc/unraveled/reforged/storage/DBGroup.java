package mc.unraveled.reforged.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.permission.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBGroup {
    @Getter
    private Connection connection;

    public DBGroup(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    public void createTable(Rank rank) {
        PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + rank.getAttachment().getName() + " (uuid VARCHAR(36), permissions VARCHAR(64));");
        statement.execute();
    }

    @SneakyThrows
    public void addPermission(Rank rank, String permission) {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO " + rank.getAttachment().getName() + " (permissions) VALUES (?);");
        statement.setString(1, permission);
        statement.executeUpdate();
    }

    @SneakyThrows
    public void removePermission(Rank rank, String permission) {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM " + rank.getAttachment().getName() + " WHERE permissions = ?;");
        statement.setString(1, permission);
        statement.executeUpdate();
    }

    @SneakyThrows
    public List<String> getPermissions(Rank rank) {
        List<String> permissions = new ArrayList<>();
        PreparedStatement statement = getConnection().prepareStatement("SELECT permissions FROM " + rank.getAttachment().getName() + ";");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            permissions.add(rs.getString("permissions"));
        }
        return permissions;
    }

    @SneakyThrows
    public void addPlayer(Rank rank, OfflinePlayer player) {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO " + rank.getAttachment().getName() + " (uuid) VALUES (?);");
        statement.setString(1, player.getUniqueId().toString());
        statement.executeUpdate();
    }

    @SneakyThrows
    public void removePlayer(Rank rank, OfflinePlayer player) {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM " + rank.getAttachment().getName() + " WHERE uuid = ?;");
        statement.setString(1, player.getUniqueId().toString());
        statement.executeUpdate();
    }

    @SneakyThrows
    public List<OfflinePlayer> getPlayers(Rank rank) {
        List<OfflinePlayer> players = new ArrayList<>();
        PreparedStatement statement = getConnection().prepareStatement("SELECT uuid FROM " + rank.getAttachment().getName() + ";");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            UUID uuid = UUID.fromString(rs.getString("uuid"));
            players.add(Bukkit.getOfflinePlayer(uuid));
        }
        return players;
    }

    @SneakyThrows
    public void close() {
        getConnection().close();
    }

    public void open(Connection connection) {
        this.connection = connection;
    }
}
