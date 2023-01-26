package mc.unraveled.reforged.permission;

import net.kyori.adventure.text.format.TextColor;

public class RankAttachment {
    private final String name;
    private final String tag;
    private final TextColor color;
    private final int weight;

    protected RankAttachment(String name, String tag, TextColor color, int weight) {
        this.name = name;
        this.tag = tag;
        this.color = color;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public TextColor getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }
}
