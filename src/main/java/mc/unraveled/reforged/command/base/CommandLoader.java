package mc.unraveled.reforged.command.base;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLoader {
    private final Traverse plugin;
    private final List<DummyCommand> commandList = new ArrayList<>();
    private final String FALLBACK_PREFIX;

    /**
     * @param plugin         Your plugin instance
     * @param fallbackPrefix The fallback prefix to use in case your plugin fails to provide a namespace.
     */
    public CommandLoader(Traverse plugin, String fallbackPrefix) {
        this.FALLBACK_PREFIX = fallbackPrefix;
        this.plugin = plugin;
    }

    /**
     * This method will register your command internally within the CommandLoader.
     * Instances of commands will be cached and readied for loading into Paper.
     * You should always run this before using {@link CommandLoader#load()}, otherwise your commands will not be loaded.
     *
     * @param command A new instance of your command which should extend CommandBase.
     */
    public void register(AbstractCommandBase command) {
        Class<? extends AbstractCommandBase> cmd = command.getClass();
        if (cmd.getDeclaredAnnotation(CommandInfo.class) != null) {
            CommandInfo info = cmd.getDeclaredAnnotation(CommandInfo.class);
            DummyCommand dummy = new DummyCommand(plugin,
                    command,
                    info.name(),
                    info.description(),
                    info.usage(),
                    Arrays.asList(info.aliases()));
            dummy.setPermission(command.getPermission());
            dummy.permissionMessage(Component.text(command.getPermissionMessage()));
            commandList.add(dummy);
        } else {
            throw new RuntimeException("Missing a required annotation! Unable to load the command.");
        }
    }

    /**
     * This will run a loop for each command instance you input and register them all within one method.
     * This simply runs the array through a stream and uses a lambda reference to {@link CommandLoader#register(CommandBase)} in order to register the commands.
     * Each command will be loaded in the order it is provided.
     *
     * @param commands An indefinite amount of command instances to be registered.
     */
    public void register(AbstractCommandBase... commands) {
        Arrays.stream(commands).forEachOrdered(this::register);
    }

    /**
     * This will load your commands and initialize them within Paper's CommandMap.
     */
    public void load() {
        commandList.forEach(cmd -> Bukkit.getCommandMap().register(cmd.getName(), FALLBACK_PREFIX, cmd));
    }

    /**
     * This will effectively register all your commands and then load them into the Paper CommandMap.
     *
     * @param commands An indefinite amount of command instances to be registered and loaded.
     */
    public void registerAndLoad(AbstractCommandBase... commands) {
        register(commands);
        load();
    }
}
