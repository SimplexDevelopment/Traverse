package mc.unraveled.reforged.economy;

import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class EconomyRequest {

    private final Player sender;
    private final Player target;
    private final int amount;
    private final EconomyManager economy;

    public EconomyRequest(EconomyManager economy, Player sender, Player target, int amount) {
        this.sender = sender;
        this.target = target;
        this.amount = amount;
        this.economy = economy;
    }

    public Player getSender() {
        return sender;
    }

    public Player getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }

    public EconomyResponse accept() {
        EconomyResponse r = economy.withdraw(target, amount);
        if (r.transactionSuccess()) {
            r = economy.deposit(sender, amount);
            if (!r.transactionSuccess()) {
                economy.deposit(target, amount);
            }
        }
        sender.sendMessage(Component.text("Your request has been accepted."));
        target.sendMessage(Component.text("You have accepted " + sender.getName() + "'s request."));

        economy.getRequests().get(target.getUniqueId()).remove(this);

        return r;
    }

    public void deny() {
        sender.sendMessage(Component.text("Your request has been denied."));
        target.sendMessage(Component.text("You have denied " + sender.getName() + "'s request."));

        economy.getRequests().get(target.getUniqueId()).remove(this);
    }
}
