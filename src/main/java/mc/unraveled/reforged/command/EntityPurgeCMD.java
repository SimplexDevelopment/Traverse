package mc.unraveled.reforged.command;

import mc.unraveled.reforged.api.annotations.CommandInfo;
import mc.unraveled.reforged.command.base.AbstractCommandBase;
import mc.unraveled.reforged.plugin.Traverse;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

@CommandInfo(name = "entitypurge",
        usage = "/entitypurge",
        description = "Purge entities",
        aliases = {"ep", "ew", "mp", "mw", "mobpurge", "entitywipe", "mobwipe"})
public class EntityPurgeCMD extends AbstractCommandBase {
    public EntityPurgeCMD(Traverse plugin) {
        super(plugin, "entitypurge", false);
    }

    @Override
    public Component cmd(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        World world = player.getWorld();
        int count = 0;
        for (Entity entity : world.getEntities()) {
            if ((entity instanceof Player) ||
                    (entity instanceof Tameable) ||
                    (entity instanceof Hanging) ||
                    (entity instanceof Sittable) ||
                    (entity instanceof Steerable)) continue;

            entity.remove();
            count++;
        }
        return Component.text("Removed " + count + " entities");
    }
}
