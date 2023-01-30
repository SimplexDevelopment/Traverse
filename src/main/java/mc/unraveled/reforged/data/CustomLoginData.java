package mc.unraveled.reforged.data;

import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@Data
public class CustomLoginData {
    private final Player player;
    private final Component loginMessage;
}
