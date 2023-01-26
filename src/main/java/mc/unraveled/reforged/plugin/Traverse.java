package mc.unraveled.reforged.plugin;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.banning.BanManager;
import mc.unraveled.reforged.command.base.CommandLoader;
import mc.unraveled.reforged.data.DataManager;
import mc.unraveled.reforged.permission.RankManager;
import mc.unraveled.reforged.storage.DBConnectionHandler;
import mc.unraveled.reforged.storage.DBProperties;
import org.bukkit.plugin.java.JavaPlugin;

public final class Traverse extends JavaPlugin {

    // Primary variable declaration.
    @Getter
    private DBConnectionHandler SQLManager;
    @Getter
    private DataManager dataManager;
    @Getter
    private CommandLoader commandLoader;
    @Getter
    private BanManager banManager;
    @Getter
    private RankManager rankManager;

    @Override
    @SneakyThrows
    public void onEnable() {
        this.SQLManager = new DBConnectionHandler(new DBProperties("db.properties"));
        this.dataManager = new DataManager(this);
        this.commandLoader = new CommandLoader(this, "TRAVERSE");
        this.banManager = new BanManager(this);
        this.rankManager = new RankManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
