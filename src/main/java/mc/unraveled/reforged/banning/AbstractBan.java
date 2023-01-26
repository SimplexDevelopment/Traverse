package mc.unraveled.reforged.banning;

import lombok.Getter;
import mc.unraveled.reforged.api.Serializable;
import mc.unraveled.reforged.util.Pair;
import mc.unraveled.reforged.util.Utilities;

import java.util.List;

@Getter
public abstract class AbstractBan implements Serializable<AbstractBan> {
    private final String uuid;
    private final String ip;
    private final String source;
    private final String reason;
    private final long propogated;
    private final long expiry;
    private final List<Pair<String, String>> contentPairs;
    private boolean active;

    public AbstractBan(String uuid, String ip, String source, String reason, long propogated, long expiry, boolean active) {
        this.uuid = uuid;
        this.ip = ip;
        this.source = source;
        this.reason = reason;
        this.propogated = propogated;
        this.expiry = expiry;
        this.active = active;

        this.contentPairs = List.of(
                new Pair<>("uuid", uuid),
                new Pair<>("ip", ip),
                new Pair<>("source", source),
                new Pair<>("reason", reason),
                new Pair<>("propogated", String.valueOf(propogated)),
                new Pair<>("expiry", String.valueOf(expiry)),
                new Pair<>("active", String.valueOf(active))
        );
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String serialize() {
        return Utilities.serialize(contentPairs);
    }

    @Override
    public AbstractBan deserialize(String formatted) {
        char delimiter = ':';
        char end = ';';
        char uuid = 'u';
        char ip = 'i';
        char reason = 'r';
        char source = 's';
        char propogated = 'p';
        char expiry = 'e';
        char active = 'a';

        String uuidString = formatted.substring(formatted.indexOf(uuid) + 1, formatted.indexOf(end));
        String ipString = formatted.substring(formatted.indexOf(ip) + 1, formatted.indexOf(end));
        String reasonString = formatted.substring(formatted.indexOf(reason) + 1, formatted.indexOf(end));
        String sourceString = formatted.substring(formatted.indexOf(source) + 1, formatted.indexOf(end));
        String propogatedString = formatted.substring(formatted.indexOf(propogated) + 1, formatted.indexOf(end));
        String expiryString = formatted.substring(formatted.indexOf(expiry) + 1, formatted.indexOf(end));
        String activeString = formatted.substring(formatted.indexOf(active) + 1, formatted.indexOf(end));

        return new SimpleBan(uuidString,
                ipString,
                reasonString,
                sourceString,
                Long.parseLong(propogatedString),
                Long.parseLong(expiryString),
                Boolean.parseBoolean(activeString));
    }
}
