package mc.unraveled.reforged.listening;

import lombok.AllArgsConstructor;
import lombok.Data;
import mc.unraveled.reforged.plugin.Traverse;
import org.bukkit.event.Listener;

@AllArgsConstructor
@Data
public class AbstractListener implements Listener {
    private final Traverse plugin;
}
