package mc.unraveled.reforged.plugin;

import lombok.SneakyThrows;
import mc.unraveled.reforged.permission.Rank;
import mc.unraveled.reforged.storage.DBConnectionHandler;
import mc.unraveled.reforged.storage.DBGroup;
import mc.unraveled.reforged.storage.DBProperties;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Traverse extends JavaPlugin {

    private DBConnectionHandler handler;

    @Override
    public void onEnable() {
        initDatabaseGroups();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @SneakyThrows
    private void initDatabaseGroups() {
        handler = new DBConnectionHandler(new DBProperties("groups.properties"));
        DBGroup groupHandler = new DBGroup(handler.establish());
        Arrays.stream(Rank.values()).forEach(groupHandler::createTable);
        groupHandler.close();
    }

    public DBConnectionHandler getSQLManager() {
        return handler;
    }
}
