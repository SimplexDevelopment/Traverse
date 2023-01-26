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
    public void addBan(AbstractBan ban) {
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO bans (uuid, ip, reason, banned_by, banned_at, expires_at, active) VALUES (?, ?, ?, ?, ?, ?, ?);");
        statement.setString(1, ban.getUuid());
        statement.setString(2, ban.getIp());
        statement.setString(3, ban.getReason());
        statement.setString(4, ban.getSource());
        statement.setLong(5, ban.getPropogated());
        statement.setLong(6, ban.getExpiry());
        statement.setBoolean(7, ban.isActive());
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
    public void close() {
        getConnection().close();
    }



}
