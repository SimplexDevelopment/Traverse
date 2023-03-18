package mc.unraveled.reforged.permission;

import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBGroup;
import mc.unraveled.reforged.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RankManager implements Baker {

    private final Traverse plugin;
    private final List<Tuple<Rank, List<UUID>, List<String>>> rankQueue = new ArrayList<>();
    private final List<Group> bakedGroups = new ArrayList<>();
    private boolean baked = false;

    public RankManager(@NotNull Traverse plugin) {
        this.plugin = plugin;

        DBGroup groupHandler = new DBGroup(plugin.getSQLManager().establish());
        Arrays.stream(Rank.values()).forEach(groupHandler::createTable);
        Rank[] ranks = Rank.values();

        for (Rank rank : ranks) {
            Tuple<Rank, List<UUID>, List<String>> rankTuple;
            List<UUID> playerList;
            List<String> permissionList;

            playerList = groupHandler.getPlayers(rank);
            permissionList = groupHandler.getPermissions(rank);

            rankTuple = new Tuple<>(rank, playerList, permissionList);
            rankQueue.add(rankTuple);
        }

        groupHandler.close();
        bake();
    }

    public Traverse getPlugin() {
        return plugin;
    }

    public void insert(Rank rank, List<UUID> playerList, List<String> args) {
        rankQueue.add(new Tuple<>(rank, playerList, args));
    }

    public void eject(Rank rank, List<UUID> playerList, List<String> args) {
        rankQueue.remove(new Tuple<>(rank, playerList, args));
    }

    public Rank getRank(CommandSender player) {
        if (player instanceof ConsoleCommandSender) return Rank.CONSOLE;

        Player c = (Player) player;

        return rankQueue.stream()
                .filter(tuple -> tuple.getSecond().contains(c.getUniqueId()))
                .findFirst()
                .map(Tuple::getFirst)
                .orElse(Rank.NON_OP);
    }

    public void insertPermission(Rank rank, String permission) {
        rankQueue.stream()
                .filter(tuple -> tuple.getFirst().equals(rank))
                .forEach(tuple -> tuple.getThird().add(permission));
    }

    public void ejectPermission(Rank rank, String permission) {
        rankQueue.stream()
                .filter(tuple -> tuple.getFirst().equals(rank))
                .forEach(tuple -> tuple.getThird().remove(permission));
    }

    public void insertPlayer(Rank rank, OfflinePlayer player) {
        rankQueue.stream()
                .filter(tuple -> tuple.getFirst().equals(rank))
                .forEach(tuple -> tuple.getSecond().add(player.getUniqueId()));
    }

    public void ejectPlayer(Rank rank, OfflinePlayer player) {
        rankQueue.stream()
                .filter(tuple -> tuple.getFirst().equals(rank))
                .forEach(tuple -> tuple.getSecond().remove(player.getUniqueId()));
    }

    public void save() {
        if (!baked) throw new IllegalStateException("You cannot save a rank manager that has not been baked!");

        DBGroup groupHandler = new DBGroup(plugin.getSQLManager().establish());
        bakedGroups.forEach(group -> {
            for (OfflinePlayer p : group.getPlayers()) {
                groupHandler.addPlayer(group.getRank(), p);
            }

            for (String arg : group.getPermissions()) {
                groupHandler.addPermission(group.getRank(), arg);
            }
        });
        groupHandler.close();
    }

    @Override
    public void bake() {
        if (baked) return;

        rankQueue.forEach(tuple -> {
            Rank rank = tuple.getFirst();
            List<UUID> players = tuple.getSecond();
            List<String> permissions = tuple.getThird();

            Group group = new Group(rank);
            for (UUID p : players) {
                group.insert(Bukkit.getOfflinePlayer(p));
            }
            for (String arg : permissions) {
                group.insert(arg);
            }
            bakedGroups.add(group);
        });

        baked = true;
    }

    @Override
    public void unbake() {
        if (!baked) return;

        bakedGroups.clear();

        baked = false;
    }
}
