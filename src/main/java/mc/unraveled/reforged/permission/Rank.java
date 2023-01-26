package mc.unraveled.reforged.permission;

import net.kyori.adventure.text.format.TextColor;

public enum Rank {
    EXECUTIVE("executive", "Exec", TextColor.color(254, 0, 0), 7),
    DEV("developer", "Dev", TextColor.color(165, 0, 218), 6),
    ADMIN("admin", "Admin", TextColor.color(214, 108, 32), 5),
    MOD("mod", "Mod", TextColor.color(0, 198, 98), 4),
    BUILDER("builder", "Bldr", TextColor.color(0, 168, 238), 3),
    VIP("vip", "VIP", TextColor.color(238, 98, 150), 2),
    OP("op", "OP", TextColor.color(198, 64, 64), 1),
    NON_OP("guest", "", TextColor.color(178, 178, 178), 0);

    final RankAttachment attachment;

    Rank(String name, String tag, TextColor color, int weight) {
        this.attachment = new RankAttachment(name, tag, color, weight);
    }

    public RankAttachment getAttachment() {
        return attachment;
    }
}
