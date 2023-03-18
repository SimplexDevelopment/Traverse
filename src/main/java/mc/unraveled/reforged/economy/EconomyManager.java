package mc.unraveled.reforged.economy;

import mc.unraveled.reforged.plugin.Traverse;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EconomyManager {
    private final RegisteredServiceProvider<Economy> rsp;
    private final Map<UUID, List<EconomyRequest>> requests = new HashMap<>(); // This is NOT persistent.
    private final Economy economy;

    public EconomyManager(@NotNull Traverse plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null)
            throw new MissingResourceException("Vault is REQUIRED!", "Vault", "VaultAPI");

        rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) throw new MissingResourceException("Economy not found", Economy.class.getName(), "Economy");

        economy = rsp.getProvider();
    }

    public RegisteredServiceProvider<Economy> getRsp() {
        return rsp;
    }

    public Map<UUID, List<EconomyRequest>> getRequests() {
        return requests;
    }

    public Economy getEconomy() {
        return economy;
    }

    public EconomyResponse newAccount(OfflinePlayer player) {
        return economy.createBank(player.getName(), player);
    }

    public EconomyResponse balance(@NotNull OfflinePlayer player) {
        return economy.bankBalance(player.getName());
    }

    public EconomyRequest request(@NotNull OfflinePlayer from, @NotNull OfflinePlayer to, int amount) {
        EconomyRequest request = new EconomyRequest(this, (Player) from, (Player) to, amount);

        if (requests.containsKey(to.getUniqueId())) {
            requests.get(to.getUniqueId()).add(request);
        } else {
            requests.put(to.getUniqueId(), List.of(request));
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
