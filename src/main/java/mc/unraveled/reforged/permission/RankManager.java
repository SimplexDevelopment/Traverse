package mc.unraveled.reforged.permission;

import lombok.Getter;
import mc.unraveled.reforged.api.Baker;
import mc.unraveled.reforged.api.Locker;
import mc.unraveled.reforged.plugin.Traverse;
import mc.unraveled.reforged.storage.DBGroup;
import mc.unraveled.reforged.util.Tuple;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankManager implements Baker, Locker {
    @Getter
    private final Traverse plugin;
    private List<Tuple<Rank, List<OfflinePlayer>, List<String>>> rankQueue = new ArrayList<>();
    private List<Group> bakedGroups = new ArrayList<>();
    private boolean baked = false;

    public RankManager(@NotNull Traverse plugin) {
        this.plugin = plugin;

        lock().notify();

        DBGroup groupHandler = new DBGroup(plugin.getSQLManager().establish());
        Arrays.stream(Rank.values()).forEach(groupHandler::createTable);
        Rank[] ranks = Rank.values();

        for (Rank rank : ranks) {
            Tuple<Rank, List<OfflinePlayer>, List<String>> rankTuple;
            List<OfflinePlayer> playerList;
            List<String> permissionList;

            playerList = groupHandler.getPlayers(rank);
            permissionList = groupHandler.getPermissions(rank);

            rankTuple = new Tuple<>(rank, playerList, permissionList);
            rankQueue.add(rankTuple);
        }

        groupHandler.close();
        bake();
    }

    public void insert(Rank rank, List<OfflinePlayer> player, List<String> args) {
        rankQueue.add(new Tuple<>(rank, player, args));
    }

    public void eject(Rank rank, List<OfflinePlayer> player, List<String> args) {
        rankQueue.remove(new Tuple<>(rank, player, args));
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

    @Override
    public void bake() {
        if (baked) return;

        synchronized (lock()) {
            rankQueue.forEach(tuple -> {
                Rank rank = tuple.getFirst();
                List<OfflinePlayer> players = tuple.getSecond();
                List<String> permissions = tuple.getThird();

                Group group = new Group(rank);
                for (OfflinePlayer p : players) {
                    group.insert(p);
                }
                for (String arg : permissions) {
                    group.insert(arg);
                }
                bakedGroups.add(group);
            });
        }

        baked = true;
        lock().notify();
    }

    @Override
    public void unbake() {
        if (!baked) return;

        synchronized (lock()) {
            bakedGroups.clear();
        }

        baked = false;
        lock().notify();
    }
}
