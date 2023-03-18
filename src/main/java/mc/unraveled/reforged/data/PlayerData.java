package mc.unraveled.reforged.data;

import mc.unraveled.reforged.permission.Rank;

import java.util.Date;
import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private String userName;
    private Rank rank;
    private long playtime;
    private int coins;
    private Date lastLogin;
    private String loginMessage;
    private InfractionData infractionData;

    public PlayerData(PlayerDataBuilder builder) {
        this.uuid = builder.getUuid();
        this.userName = builder.getUserName();
        this.rank = builder.getRank();
        this.playtime = builder.getPlaytime();
        this.coins = builder.getCoins();
        this.lastLogin = builder.getLastLogin();
        this.loginMessage = builder.getLoginMessage();
        this.infractionData = builder.getInfractionData();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    public InfractionData getInfractionData() {
        return infractionData;
    }

    public void setInfractionData(InfractionData infractionData) {
        this.infractionData = infractionData;
    }
}
