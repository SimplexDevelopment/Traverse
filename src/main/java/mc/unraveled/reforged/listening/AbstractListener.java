package mc.unraveled.reforged.listening;

import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.event.Listener;

public class AbstractListener implements Listener {
    private final Traverse plugin;

    public AbstractListener(Traverse plugin) {
        this.plugin = plugin;
    }

    public Traverse getPlugin() {
        return plugin;
    }
}
