package mc.unraveled.reforged.data;

import mc.unraveled.reforged.permission.Rank;

import java.util.Date;
import java.util.UUID;

public class PlayerDataBuilder {
    private UUID uuid;
    private String userName;
    private Rank rank;
    private long playtime;
    private int coins;
    private Date lastLogin;
    private String loginMessage;
    private InfractionData infractionData;

    UUID getUuid() {
        return uuid;
    }

    public PlayerDataBuilder setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    String getUserName() {
        return userName;
    }

    public PlayerDataBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    Rank getRank() {
        return rank;
    }

    public PlayerDataBuilder setRank(Rank rank) {
        this.rank = rank;
        return this;
    }

    long getPlaytime() {
        return playtime;
    }

    public PlayerDataBuilder setPlaytime(long playtime) {
        this.playtime = playtime;
        return this;
    }

    int getCoins() {
        return coins;
    }

    public PlayerDataBuilder setCoins(int coins) {
        this.coins = coins;
        return this;
    }

    Date getLastLogin() {
        return lastLogin;
    }

    public PlayerDataBuilder setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    String getLoginMessage() {
        return loginMessage;
    }

    public PlayerDataBuilder setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
        return this;
    }

    InfractionData getInfractionData() {
        return infractionData;
    }

    public PlayerDataBuilder setInfractionData(InfractionData infractionData) {
        this.infractionData = infractionData;
        return this;
    }

    public PlayerDataBuilder compile() {
        return this;
    }
}