package mc.unraveled.reforged.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public abstract class AbstractInventory {
    private final Inventory inventory;

    protected AbstractInventory(String title, InventoryType type) {
        this.inventory = Bukkit.createInventory(null, type, Component.text(title));
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract void update();
}
