package mc.unraveled.reforged.plugin;

import lombok.Getter;
import lombok.SneakyThrows;
import mc.unraveled.reforged.api.Locker;
import mc.unraveled.reforged.banning.BanManager;
import mc.unraveled.reforged.command.*;
import mc.unraveled.reforged.command.base.CommandLoader;
import mc.unraveled.reforged.config.YamlManager;
import mc.unraveled.reforged.data.DataManager;
import mc.unraveled.reforged.data.LoginManager;
import mc.unraveled.reforged.economy.EconomyManager;
import mc.unraveled.reforged.listening.InfractionListener;
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
    @Getter
    private EconomyManager economyManager;
    @Getter
    private LoginManager loginManager;
    @Getter
    private YamlManager yamlManager;

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
        this.economyManager = new EconomyManager(this);
        this.loginManager = new LoginManager(this);
        this.yamlManager = new YamlManager(this);

        registerCommands();
        registerListeners();
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
            getCommandLoader().register(
                    new BanCMD(this),
                    new BankCMD(this),
                    new EntityPurgeCMD(this),
                    new GroupCMD(this),
                    new MuteCMD(this),
                    new PardonCMD(this),
                    new TraverseCMD(this),
                    new UnmuteCMD(this)
            );
            lock().wait(1000);
        }

        lock().notify();
        getCommandLoader().load();
    }

    public void registerListeners() {
        new InfractionListener(this);
    }
}
