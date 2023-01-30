package mc.unraveled.reforged.economy;

import lombok.Getter;
import mc.unraveled.reforged.plugin.Traverse;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

@Getter
public class EconomyManager {
    private final RegisteredServiceProvider<Economy> rsp;
    private final Map<Player, List<EconomyRequest>> requests = new HashMap<>(); // This is NOT persistent.
    private final Economy economy;
    private final Coin coin;

    public EconomyManager(@NotNull Traverse plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null)
            throw new RuntimeException("Vault is REQUIRED!");

        rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) throw new MissingResourceException("Economy not found", Economy.class.getName(), "Economy");

        economy = rsp.getProvider();

        coin = new Coin("Sheccies", "$", 1);
    }

    public EconomyResponse newAccount(OfflinePlayer player) {
        return economy.createBank(player.getName(), player);
    }

    public EconomyResponse balance(@NotNull OfflinePlayer player) {
        return economy.bankBalance(player.getName());
    }

    public EconomyRequest request(@NotNull OfflinePlayer from, @NotNull OfflinePlayer to, int amount) {
        EconomyRequest request = new EconomyRequest(this, (Player) from, (Player) to, amount);

        if (requests.containsKey(to)) {
            requests.get(to).add(request);
        } else {
            requests.put((Player) to, List.of(request));
        }

        return request;
    }

    public EconomyResponse deposit(@NotNull OfflinePlayer player, double amount) {
        return economy.bankDeposit(player.getName(), amount);
    }

    public EconomyResponse withdraw(@NotNull OfflinePlayer player, double amount) {
        return economy.bankWithdraw(player.getName(), amount);
    }

    public EconomyResponse transfer(OfflinePlayer from, OfflinePlayer to, double amount) {
        EconomyResponse response = withdraw(from, amount);
        if (response.transactionSuccess()) {
            response = deposit(to, amount);
            if (!response.transactionSuccess()) {
                deposit(from, amount);
            }
        }
        return response;
    }

    public EconomyResponse delete(@NotNull OfflinePlayer player) {
        return economy.deleteBank(player.getName());
    }

    public EconomyResponse has(@NotNull OfflinePlayer player, double amount) {
        return economy.bankHas(player.getName(), amount);
    }

    public boolean hasAccount(@NotNull OfflinePlayer player) {
        return economy.hasBankSupport() && economy.hasAccount(player);
    }
}
