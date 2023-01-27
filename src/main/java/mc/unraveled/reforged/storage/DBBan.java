package mc.unraveled.reforged.storage;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.banning.AbstractBan;
import mc.unraveled.reforged.banning.SimpleBan;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class DBBan {
    @Getter
    private Connection connection;

    public DBBan(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    public void createTable() {
        PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bans (uuid VARCHAR(36), ip VARCHAR(16), reason VARCHAR(64), banned_by VARCHAR(16), banned_at BIGINT, expires_at BIGINT, active BOOLEAN, PRIMARY KEY (uuid));");
        statement.executeUpdate();
    }

    @SneakyThrows
    public void insert(@NotNull AbstractBan ban) {
        PreparedStatement statement = getConnection().prepareStatement("IF NOT EXISTS (SELECT 1 FROM bans WHERE uuid = ?) " +
                "BEGIN INSERT INTO bans (uuid, ip, reason, banned_by, banned_at, expires_at, active) VALUES (?, ?, ?, ?, ?, ?, ?) END " +
                "ELSE BEGIN UPDATE bans SET ip = ?, reason = ?, banned_by = ?, banned_at = ?, expires_at = ?, active = ? WHERE uuid = ? END;");

        statement.setString(1, ban.getUuid());
        statement.setString(2, ban.getUuid());
        statement.setString(3, ban.getIp());
        statement.setString(4, ban.getReason());
        statement.setString(5, ban.getSource());
        statement.setLong(6, ban.getPropogated());
        statement.setLong(7, ban.getExpiry());
        statement.setBoolean(8, ban.isActive());
        statement.setString(9, ban.getIp());
        statement.setString(10, ban.getReason());
        statement.setString(11, ban.getSource());
        statement.setLong(12, ban.getPropogated());
        statement.setLong(13, ban.getExpiry());
        statement.setBoolean(14, ban.isActive());
        statement.setString(15, ban.getUuid());

        statement.executeUpdate();
    }

    @SneakyThrows
    @Nullable
    public AbstractBan getBan(@NotNull UUID uuid) {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM bans WHERE uuid = ?;");
        statement.setString(1, uuid.toString());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return new SimpleBan(rs.getString("uuid"),
                    rs.getString("ip"),
                    rs.getString("reason"),
                    rs.getString("banned_by"),
                    rs.getLong("banned_at"),
                    rs.getLong("expires_at"),
                    rs.getBoolean("active"));
        }
        return null;
    }

    @SneakyThrows
    public List<AbstractBan> all() {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM bans;");
        ResultSet rs = statement.executeQuery();
        List<AbstractBan> bans = new ArrayList<>();
        while (rs.next()) {
            bans.add(new SimpleBan(rs.getString("uuid"),
                    rs.getString("ip"),
                    rs.getString("reason"),
                    rs.getString("banned_by"),
                    rs.getLong("banned_at"),
                    rs.getLong("expires_at"),
                    rs.getBoolean("active")));
        }
        return bans;
    }

    @SneakyThrows
    public void delete(@NotNull AbstractBan ban) {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM bans WHERE uuid = ?;");
        statement.setString(1, ban.getUuid());
        statement.executeUpdate();
    }

    @SneakyThrows
    public void deleteAll() {
        PreparedStatement statement = getConnection().prepareStatement("DELETE FROM bans;");
        statement.executeUpdate();
    }

    @SneakyThrows
    public void close() {
        getConnection().close();
        this.connection = null;
    }

    public void open(Connection connection) {
        this.connection = connection;
    }
}
