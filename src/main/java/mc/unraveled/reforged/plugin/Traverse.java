package mc.unraveled.reforged.plugin;

import mc.unraveled.reforged.banning.BanManager;
import mc.unraveled.reforged.command.*;
import mc.unraveled.reforged.command.base.CommandLoader;
import mc.unraveled.reforged.config.Yaml;
import mc.unraveled.reforged.config.YamlManager;
import mc.unraveled.reforged.data.DataManager;
import mc.unraveled.reforged.data.LoginManager;
import mc.unraveled.reforged.data.StaffChat;
import mc.unraveled.reforged.economy.EconomyManager;
import mc.unraveled.reforged.listening.InfractionListener;
import mc.unraveled.reforged.listening.PlayerDataListener;
import mc.unraveled.reforged.listening.StaffChatListener;
import mc.unraveled.reforged.permission.RankManager;
import mc.unraveled.reforged.service.base.Scheduling;
import mc.unraveled.reforged.service.base.ServicePool;
import mc.unraveled.reforged.storage.DBConnectionHandler;
import mc.unraveled.reforged.storage.DBProperties;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Traverse extends JavaPlugin {

    // Secondary variable declaration.
    private final String CONFIG_FILE = "config.yml";
    private final File CONFIG = new File(getDataFolder(), CONFIG_FILE);
    // Primary variable declaration.

    private DBConnectionHandler SQLManager;

    private DataManager dataManager;

    private CommandLoader commandLoader;

    private BanManager banManager;

    private RankManager rankManager;

    private Scheduling scheduler;

    private ServicePool PIPELINE;

    private EconomyManager economyManager;

    private LoginManager loginManager;

    private YamlManager yamlManager;
    private Yaml yamlConfig;
    private StaffChat staffChat;

    public DBConnectionHandler getSQLManager() {
        return SQLManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public CommandLoader getCommandLoader() {
        return commandLoader;
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public Scheduling getScheduler() {
        return scheduler;
    }

    public ServicePool getPIPELINE() {
        return PIPELINE;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public YamlManager getYamlManager() {
        return yamlManager;
    }

    public Yaml getYamlConfig() {
        return yamlConfig;
    }

    public StaffChat getStaffChat() {
        return staffChat;
    }

    @Override
    public void onEnable() {
        this.SQLManager = new DBConnectionHandler(new DBProperties(this, "db.properties"));
        this.commandLoader = new CommandLoader(this, "TRAVERSE");
        this.dataManager = new DataManager(this);
        this.banManager = new BanManager(this);
        this.rankManager = new RankManager(this);
        this.scheduler = new Scheduling(this);
        this.PIPELINE = new ServicePool("PIPELINE", this);
        this.economyManager = new EconomyManager(this);
        this.loginManager = new LoginManager(this);
        this.yamlManager = new YamlManager(this);
        this.staffChat = new StaffChat(this);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        this.banManager.save();
        this.dataManager.saveCacheToDB();
        this.PIPELINE.recycle();
        this.rankManager.save();
        this.yamlConfig.saveToFile();
        // Plugin shutdown logic
    }

    public void registerCommands() {
        getCommandLoader().register(
                new BanCMD(this),
                new BanInfoCMD(this),
                new BankCMD(this),
                new EntityPurgeCMD(this),
                new GroupCMD(this),
                new KickCMD(this),
                new LockCMD(this),
                new MuteCMD(this),
                new PardonCMD(this),
                new PlayerDataCMD(this),
                new StaffChatCMD(this),
                new TraverseCMD(this),
                new UnmuteCMD(this)
        );

        getCommandLoader().load();
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new InfractionListener(this), this);
        getServer().getPluginManager().registerEvents(new StaffChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDataListener(this), this);
    }

    public void registerConfig() {
        Yaml yaml;
        try {
            if (CONFIG.createNewFile()) {
                yaml = getYamlManager().bldr()
                        .fileName(CONFIG_FILE)
                        .dataFolder(getDataFolder())
                        .copyDefaults(true)
                        .build();
            } else {
                yaml = getYamlManager().bldr()
                        .fileName(CONFIG_FILE)
                        .dataFolder(getDataFolder())
                        .copyDefaults(false)
                        .build();
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not create config.yml!");
            e.printStackTrace();
            return;
        }

        this.yamlConfig = yaml;
        getYamlConfig().loadFromFile();
    }
}
