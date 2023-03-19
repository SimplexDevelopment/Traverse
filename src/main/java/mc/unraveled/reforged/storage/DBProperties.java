package mc.unraveled.reforged.storage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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

    public DBProperties(Plugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            if (file.createNewFile()) {
                Bukkit.getLogger().info("Created new properties file.");
                plugin.saveResource("db.properties", true);
            }

            properties.load(fileInputStream);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Error loading properties file!" + e.getMessage());
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

    public Properties getProperties() {
        return properties;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getDatabaseFile() {
        return databaseFile;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
