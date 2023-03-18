package mc.unraveled.reforged.util;

import org.bukkit.entity.Player;

public class Lock {
    private volatile boolean locked = false;
    private final Player player;

    public Lock(Player player) {
        this.player = player;
    }

    public void lock() {
        locked = true;

        while (locked) {
            Thread.onSpinWait();
            player.openInventory(player.getInventory());
            player.closeInventory();
        }
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }
}
