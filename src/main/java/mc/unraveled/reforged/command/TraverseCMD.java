package mc.unraveled.reforged.command;

import mc.unraveled.reforged.command.base.AbstractCommandBase;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class TraverseCMD extends AbstractCommandBase {
    public TraverseCMD() {
        super("traverse.cmd", "You do not have permission to use this command!", true);
    }

    @Override
    public Component run(CommandSender sender, String[] args) {
        return Component.text("Hello!");
    }
}
