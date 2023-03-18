package mc.unraveled.reforged.storage;

import mc.unraveled.reforged.data.InfractionData;
import mc.unraveled.reforged.data.LoginInfo;
import mc.unraveled.reforged.data.PlayerData;
import mc.unraveled.reforged.data.PlayerDataBuilder;
import mc.unraveled.reforged.permission.Rank;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class DBUser {
    public static final String LOGIN_MESSAGE = "login_message";
    private final String COIN_LITERAL = "coins";

    private final Connection connection;

    public DBUser(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        try (PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS users (uuid VARCHAR(36), username VARCHAR(16), rank VARCHAR(64), play_time BIGINT, coins BIGINT, last_login BIGINT, login_message VARCHAR(64), PRIMARY KEY (uuid));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not create users table!");
        }
    }

    public void insert(@NotNull PlayerData playerData) {
        try (PreparedStatement statement = getConnection().prepareStatement("IF NOT EXISTS (SELECT 1 FROM users WHERE uuid = ?) " +
                "BEGIN INSERT INTO users (uuid, username, rank, play_time, coins, last_login, login_message) VALUES (?, ?, ?, ?, ?, ?, ?) END " +
                "ELSE BEGIN UPDATE users SET username = ?, rank = ?, play_time = ?, coins = ?, last_login = ?, login_message = ?, WHERE uuid = ? END;")) {
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
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not insert player data into users table!");
        }
    }

    public void delete(@NotNull PlayerData playerData) {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM users WHERE uuid = ?;")) {
            statement.setString(1, playerData.getUuid().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not delete player data from users table!");
        }
    }

    public void deleteAll() {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM users;")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not delete all player data from users table!");
        }
    }

    public void setLastLogin(String uuid, long lastLogin) {
        try (PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET last_login = ? WHERE uuid = ?;")) {
            statement.setLong(1, lastLogin);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not update last login for player " + uuid + "!");
        }
    }

    public Date getLastLogin(String uuid) {
        AtomicLong lastLogin = new AtomicLong(0L);

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT last_login FROM users WHERE uuid = ?;")) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lastLogin.set(resultSet.getLong("last_login"));
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not get last login for player " + uuid + "!");
        }

        return new Date(lastLogin.get());
    }

    public void setCoins(String uuid, int coins) {
        try (PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET coins = ? WHERE uuid = ?;")) {
            statement.setInt(1, coins);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not update coins for player " + uuid + "!");
        }
    }

    public int getCoins(String uuid) {
        AtomicInteger coins = new AtomicInteger(0);

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT coins FROM users WHERE uuid = ?;")) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    coins.set(resultSet.getInt(COIN_LITERAL));
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not get coins for player " + uuid + "!");
        }

        return coins.get();
    }

    public void setRank(String uuid, @NotNull Rank rank) {
        try (PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET rank = ? WHERE uuid = ?;")) {
            statement.setString(1, rank.name());
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not update rank for player " + uuid + "!");
        }
    }

    public Rank getRank(String uuid) {
        AtomicReference<Rank> rank = new AtomicReference<>(Rank.NON_OP);

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT rank FROM users WHERE uuid = ?;")) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rank.set(Rank.valueOf(resultSet.getString("rank")));
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not get rank for player " + uuid + "!");
        }

        return rank.get();
    }

    public void setLoginMessage(String uuid, String message) {
        try (PreparedStatement statement = getConnection().prepareStatement("UPDATE users SET login_message = ? WHERE uuid = ?;")) {
            statement.setString(1, message);
            statement.setString(2, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not update login message for player " + uuid + "!");
        }
    }

    public Map<OfflinePlayer, LoginInfo> getLoginMessages() {
        Map<OfflinePlayer, LoginInfo> loginInfoMap = new HashMap<>();

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM users;")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OfflinePlayer player = Bukkit.getPlayer(UUID.fromString(resultSet.getString("uuid")));
                    LoginInfo loginInfo = new LoginInfo();
                    loginInfo.setLoginMessage(Component.text(resultSet.getString(LOGIN_MESSAGE)));
                    loginInfoMap.put(player, loginInfo);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not get login messages!");
        }

        return loginInfoMap;
    }

    public LoginInfo getLoginInfo(String uuid) {
        LoginInfo loginInfo = new LoginInfo();

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ?;")) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Component c = Component.text(resultSet.getString(LOGIN_MESSAGE));
                    loginInfo.setLoginMessage(c);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not get login message for player " + uuid + "!");
        }

        return loginInfo;
    }

    public List<PlayerData> all() {
        List<PlayerData> dataList = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM users;")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PlayerDataBuilder playerDataBuilder = new PlayerDataBuilder().setUuid(UUID.fromString(resultSet.getString("uuid"))).setUserName(resultSet.getString("username")).setRank(Rank.valueOf(resultSet.getString("rank"))).setPlaytime(resultSet.getLong("play_time")).setCoins(resultSet.getInt(COIN_LITERAL)).setLastLogin(new Date(resultSet.getLong("last_login"))).setLoginMessage(resultSet.getString(LOGIN_MESSAGE)).setInfractionData(InfractionData.getCachedInfractionData(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("uuid"))))).compile();
                    PlayerData playerData = new PlayerData(playerDataBuilder);
                    dataList.add(playerData);
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not get all player data from users table!");
        }

        return dataList;
    }

    public void close() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Could not close database connection!");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
