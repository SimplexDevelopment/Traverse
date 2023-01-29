package mc.unraveled.reforged.plugin;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.api.Locker;
import mc.unraveled.reforged.banning.BanManager;
import mc.unraveled.reforged.command.BanCMD;
import mc.unraveled.reforged.command.GroupCMD;
import mc.unraveled.reforged.command.TraverseCMD;
import mc.unraveled.reforged.command.base.CommandLoader;
import mc.unraveled.reforged.data.DataManager;
import mc.unraveled.reforged.permission.RankManager;
import mc.unraveled.reforged.service.base.Scheduling;
import mc.unraveled.reforged.service.base.ServicePool;
import mc.unraveled.reforged.storage.DBConnectionHandler;
import mc.unraveled.reforged.storage.DBProperties;
import org.bukkit.plugin.java.JavaPlugin;

public final class Traverse extends JavaPlugin implements Locker {

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
    @Getter
    private Scheduling scheduler;
    @Getter
    private ServicePool PIPELINE;

    @Override
    @SneakyThrows
    public void onEnable() {
        this.SQLManager = new DBConnectionHandler(new DBProperties("db.properties"));
        this.dataManager = new DataManager(this);
        this.commandLoader = new CommandLoader(this, "TRAVERSE");
        this.banManager = new BanManager(this);
        this.rankManager = new RankManager(this);
        this.scheduler = new Scheduling(this);
        this.PIPELINE = new ServicePool("PIPELINE", this);
    }

    @Override
    public void onDisable() {
        this.banManager.save();
        this.dataManager.saveCacheToDB();
        this.PIPELINE.recycle();
        this.rankManager.save();
        // Plugin shutdown logic
    }

    @SneakyThrows
    public void registerCommands() {
        synchronized (lock()) {
            getCommandLoader().register(new TraverseCMD(this),
                    new BanCMD(this),
                    new GroupCMD(this));
            lock().wait(1000);
        }

        lock().notify();
        getCommandLoader().load();
    }
}
