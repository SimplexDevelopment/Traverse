package mc.unraveled.reforged.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import mc.unraveled.reforged.permission.Rank;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerData {
    private UUID uuid;
    private String userName;
    private Rank rank;
    private long playtime;
    private int coins;
    private Date lastLogin;
    private String loginMessage;
    private InfractionData infractionData;
}
