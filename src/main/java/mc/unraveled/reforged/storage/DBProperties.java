package mc.unraveled.reforged.storage;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Getter
public class DBProperties {
    private final Properties properties;
    private final String url;
    private final String driver;
    private final String databaseType;
    private final String databaseFile;
    private final String host;
    private final String port;
    private final String username;
    private final String password;

    public DBProperties(String fileName) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        databaseType = properties.getProperty("databaseType");
        databaseFile = properties.getProperty("databaseFile");
        host = properties.getProperty("host");
        port = properties.getProperty("port");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        url = driver + ":" + databaseType + "://" + host + ":" + port + "/" + databaseFile;
    }
}
