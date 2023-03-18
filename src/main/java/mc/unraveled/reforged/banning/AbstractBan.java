package mc.unraveled.reforged.banning;

import mc.unraveled.reforged.api.Serializable;
import mc.unraveled.reforged.util.Pair;
import mc.unraveled.reforged.util.Utilities;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractBan implements Serializable<AbstractBan> {
    private final String uuid;
    private final String ip;
    private final String source;
    private final String reason;
    private final long propagated;
    private final long expiry;
    private final List<Pair<String, String>> contentPairs;
    private boolean active;

    public AbstractBan(String uuid, String ip, String source, String reason, long propagated, long expiry, boolean active) {
        this.uuid = uuid;
        this.ip = ip;
        this.source = source;
        this.reason = reason;
        this.propagated = propagated;
        this.expiry = expiry;
        this.active = active;

        this.contentPairs = List.of(
                new Pair<>("uuid", uuid),
                new Pair<>("ip", ip),
                new Pair<>("source", source),
                new Pair<>("reason", reason),
                new Pair<>("propagated", String.valueOf(propagated)),
                new Pair<>("expiry", String.valueOf(expiry)),
                new Pair<>("active", String.valueOf(active))
        );
    }

    public String getUuid() {
        return uuid;
    }

    public String getIp() {
        return ip;
    }

    public String getSource() {
        return source;
    }

    public String getReason() {
        return reason;
    }

    public long getPropagated() {
        return propagated;
    }

    public long getExpiry() {
        return expiry;
    }

    public List<Pair<String, String>> getContentPairs() {
        return contentPairs;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String serialize() {
        return Utilities.serialize(contentPairs);
    }

    @Override
    public AbstractBan deserialize(@NotNull String formatted) {
        char end = ';';
        char uuid = 'u';
        char ip = 'i';
        char reason = 'r';
        char source = 's';
        char propagated = 'p';
        char expiry = 'e';
        char active = 'a';

        String uuidString = formatted.substring(formatted.indexOf(uuid) + 1, formatted.indexOf(end));
        String ipString = formatted.substring(formatted.indexOf(ip) + 1, formatted.indexOf(end));
        String reasonString = formatted.substring(formatted.indexOf(reason) + 1, formatted.indexOf(end));
        String sourceString = formatted.substring(formatted.indexOf(source) + 1, formatted.indexOf(end));
        String propagatedString = formatted.substring(formatted.indexOf(propagated) + 1, formatted.indexOf(end));
        String expiryString = formatted.substring(formatted.indexOf(expiry) + 1, formatted.indexOf(end));
        String activeString = formatted.substring(formatted.indexOf(active) + 1, formatted.indexOf(end));

        return new SimpleBan(uuidString,
                ipString,
                reasonString,
                sourceString,
                Long.parseLong(propagatedString),
                Long.parseLong(expiryString),
                Boolean.parseBoolean(activeString));
    }
}
