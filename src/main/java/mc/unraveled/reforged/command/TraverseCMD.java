package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "traverse",
        usage = "/traverse",
        description = "Traverse command")
public class TraverseCMD extends AbstractCommandBase {
    public TraverseCMD(Traverse plugin) {
        super(plugin, "traverse", "You do not have permission to use this command!", true);
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        return Component.text("Hello!");
    }
}
