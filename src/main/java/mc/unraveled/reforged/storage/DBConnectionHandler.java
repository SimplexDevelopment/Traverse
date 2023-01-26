package mc.unraveled.reforged.storage;

import lombok.SneakyThrows;

import java.sql.*;

public class DBConnectionHandler {
    private final DBProperties properties;

    public DBConnectionHandler(DBProperties properties) throws SQLException {
        this.properties = properties;
    }

    @SneakyThrows
    public Connection establish() {
        return DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword());
    }

    @SneakyThrows
    public ContextConnection establishContext() {
        return new ContextConnection(DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword()));
    }

    public record ContextConnection(Connection connection) implements AutoCloseable {
        @SneakyThrows
        @Override
        public void close() {
            connection.close();
        }
    }
}
