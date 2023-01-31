package mc.unraveled.reforged.storage;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.LoginInfo;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.permission.Rank;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DBUser {
    @Getter
    private Connection connection;

    public DBUser(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    public void createTable() {
        PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS users (uuid VARCHAR(36), username VARCHAR(16), rank VARCHAR(64), play_time BIGINT, coins BIGINT, last_login BIGINT, login_message VARCHAR(64), PRIMARY KEY (uuid));");
        statement.executeUpdate();
    }

    @SneakyThrows
    public void insert(@NotNull PlayerData playerData) {
        PreparedStatement statement = getConnection().prepareStatement("IF NOT EXISTS (SELECT 1 FROM users WHERE uuid = ?) " +
                "BEGIN INSERT INTO users (uuid, username, rank, play_time, coins, last_login, login_message) VALUES (?, ?, ?, ?, ?, ?, ?) END " +
                "ELSE BEGIN UPDATE users SET username = ?, rank = ?, play_time = ?, coins = ?, last_login = ?, login_message = ?, WHERE uuid = ? END;");

        statement.setString(1, playerData.getUuid().toString());
        statement.setString(2, playerData.getUuid().toString());
        statement.setString(3, playerData.getUserName());
        statement.setString(4, playerData.getRank().name());
        statement.setLong(5, playerData.getPlaytime());
        statement.setInt(6, playerData.getCoins());
        statement.setLong(7, playerData.getLastLogin().getTime());
        statement.setString(8, playerData.getLoginMessage());
        statement.setString(9, playerData.getUserName());
        statement.setString(10, playerData.getRank().name());
        statement.setLong(11, playerData.getPlaytime());
        statement.setInt(12, playerData.getCoins());
        statement.setLong(13, playerData.getLastLogin().getTime());
        statement.setString(14, playerData.getLoginMessage());
        statement.setString(15, playerData.getUuid().toString());

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
    public void setLastLogin(String uuid, long lastLogin) {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET last_login = ? WHERE uuid = ?;");
        statement.setLong(1, lastLogin);
        statement.setString(2, uuid);
        statement.executeUpdate();
    }

    @SneakyThrows
    public Date getLastLogin(String uuid) {
        PreparedStatement statement = getConnection().prepareStatement("SELECT last_login FROM users WHERE uuid = ?;");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();
        long lastLogin = 0;

        while (resultSet.next()) {
            lastLogin = resultSet.getLong("last_login");
        }

        return new Date(lastLogin);
    }

    @SneakyThrows
    public void setCoins(String uuid, int coins) {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET coins = ? WHERE uuid = ?;");
        statement.setInt(1, coins);
        statement.setString(2, uuid);
        statement.executeUpdate();
    }

    @SneakyThrows
    public int getCoins(String uuid) {
        PreparedStatement statement = getConnection().prepareStatement("SELECT coins FROM users WHERE uuid = ?;");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();
        int coins = 0;

        while (resultSet.next()) {
            coins = resultSet.getInt("coins");
        }

        return coins;
    }

    @SneakyThrows
    public void setRank(String uuid, @NotNull Rank rank) {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET rank = ? WHERE uuid = ?;");
        statement.setString(1, rank.name());
        statement.setString(2, uuid);
        statement.executeUpdate();
    }

    @SneakyThrows
    public Rank getRank(String uuid) {
        PreparedStatement statement = getConnection().prepareStatement("SELECT rank FROM users WHERE uuid = ?;");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();
        Rank rank = Rank.NON_OP;

        while (resultSet.next()) {
            rank = Rank.valueOf(resultSet.getString("rank"));
        }

        return rank;
    }

    @SneakyThrows
    public void setLoginMessage(String uuid, String message) {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET login_message = ? WHERE uuid = ?;");
        statement.setString(1, message);
        statement.setString(2, uuid);
        statement.executeUpdate();
    }

    @SneakyThrows
    public Map<OfflinePlayer, LoginInfo> getLoginMessages() {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM users;");
        ResultSet resultSet = statement.executeQuery();
        Map<OfflinePlayer, LoginInfo> loginInfoMap = new HashMap<>();

        while (resultSet.next()) {
            OfflinePlayer player = Bukkit.getPlayer(UUID.fromString(resultSet.getString("uuid")));
            LoginInfo loginInfo = new LoginInfo();
            loginInfoMap.put(player, loginInfo);
        }
        return loginInfoMap;
    }

    @SneakyThrows
    public LoginInfo getLoginInfo(String uuid) {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ?;");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();
        LoginInfo loginInfo = new LoginInfo();

        while (resultSet.next()) {
            Component c = Component.text(resultSet.getString("login_message"));
            loginInfo.setLoginMessage(c);
        }

        return loginInfo;
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
                    resultSet.getString("login_message"),
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
