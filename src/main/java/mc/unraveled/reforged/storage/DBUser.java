package mc.unraveled.reforged.storage;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.permission.Rank;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DBUser {
    @Getter
    private Connection connection;

    public DBUser(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    public void createTable() {
        PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS users (uuid VARCHAR(36), username VARCHAR(16), rank VARCHAR(64), play_time BIGINT, coins BIGINT, last_login BIGINT, PRIMARY KEY (uuid));");
        statement.executeUpdate();
    }

    @SneakyThrows
    public void insert(@NotNull PlayerData playerData) {
        PreparedStatement statement = getConnection().prepareStatement("IF NOT EXISTS (SELECT 1 FROM users WHERE uuid = ?) " +
                "BEGIN INSERT INTO users (uuid, username, rank, play_time, coins, last_login) VALUES (?, ?, ?, ?, ?, ?) END " +
                "ELSE BEGIN UPDATE users SET username = ?, rank = ?, play_time = ?, coins = ?, last_login = ? WHERE uuid = ? END;");

        statement.setString(1, playerData.getUuid().toString());
        statement.setString(2, playerData.getUuid().toString());
        statement.setString(3, playerData.getUserName());
        statement.setString(4, playerData.getRank().name());
        statement.setLong(5, playerData.getPlaytime());
        statement.setInt(6, playerData.getCoins());
        statement.setLong(7, playerData.getLastLogin().getTime());
        statement.setString(8, playerData.getUserName());
        statement.setString(9, playerData.getRank().name());
        statement.setLong(10, playerData.getPlaytime());
        statement.setInt(11, playerData.getCoins());
        statement.setLong(12, playerData.getLastLogin().getTime());
        statement.setString(13, playerData.getUuid().toString());

        statement.executeUpdate();
    }

    @SneakyThrows
    public void delete(@NotNull PlayerData playerData) {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM users WHERE uuid = ?;");
        statement.setString(1, playerData.getUuid().toString());
        statement.executeUpdate();
    }

    @SneakyThrows
    public void deleteAll() {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM users;");
        statement.executeUpdate();
    }

    @SneakyThrows
    public List<PlayerData> all() {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM users;");
        ResultSet resultSet = statement.executeQuery();
        List<PlayerData> dataList = new ArrayList<>();
        InfractionData data = InfractionData.getCachedInfractionData(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("uuid"))));

        while (resultSet.next()) {
            PlayerData playerData = new PlayerData(UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("username"),
                    Rank.valueOf(resultSet.getString("rank")),
                    resultSet.getLong("play_time"),
                    resultSet.getInt("coins"),
                    new Date(resultSet.getLong("last_login")),
                    data);
            dataList.add(playerData);
        }
        return dataList;
    }

    @SneakyThrows
    public void close() {
        getConnection().close();
    }
}
