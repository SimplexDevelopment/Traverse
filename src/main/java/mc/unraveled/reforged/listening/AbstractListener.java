package mc.unraveled.reforged.listening;

import lombok.Data;
import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.event.Listener;

@Data
public class AbstractListener implements Listener {
    private final Traverse plugin;
}
