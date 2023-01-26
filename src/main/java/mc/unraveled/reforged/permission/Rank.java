package mc.unraveled.reforged.permission;

import mc.unraveled.reforged.util.BasicColors;
import net.kyori.adventure.text.format.TextColor;

public enum Rank {
    EXECUTIVE("executive", "Exec", BasicColors.DARK_RED.getColor(), 7),
    DEV("developer", "Dev", BasicColors.PURPLE.getColor(), 6),
    ADMIN("admin", "Admin", BasicColors.GOLD.getColor(), 5),
    MOD("mod", "Mod", BasicColors.GREEN.getColor(), 4),
    BUILDER("builder", "Bldr", BasicColors.AQUA.getColor(), 3),
    VIP("vip", "VIP", BasicColors.PINK.getColor(), 2),
    OP("op", "OP", BasicColors.RED.getColor(), 1),
    NON_OP("guest", "", BasicColors.WHITE.getColor(), 0);

    final RankAttachment attachment;

    Rank(String name, String tag, TextColor color, int weight) {
        this.attachment = new RankAttachment(name, tag, color, weight);
    }

    public RankAttachment getAttachment() {
        return attachment;
    }
}
