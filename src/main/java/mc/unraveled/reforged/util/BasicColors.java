package mc.unraveled.reforged.util;

import net.kyori.adventure.text.format.TextColor;

public enum BasicColors {
    RED(TextColor.color(255, 0, 0)),
    DARK_RED(TextColor.color(127, 0, 0)),
    ORANGE(TextColor.color(255, 165, 0)),
    DARK_ORANGE(TextColor.color(255, 140, 0)),
    YELLOW(TextColor.color(255, 255, 0)),
    GOLD(TextColor.color(255, 215, 0)),
    GREEN(TextColor.color(0, 255, 0)),
    DARK_GREEN(TextColor.color(0, 127, 0)),
    BLUE(TextColor.color(0, 0, 255)),
    DARK_BLUE(TextColor.color(0, 0, 127)),
    AQUA(TextColor.color(0, 255, 255)),
    DARK_AQUA(TextColor.color(0, 127, 127)),
    PURPLE(TextColor.color(255, 0, 255)),
    DARK_PURPLE(TextColor.color(127, 0, 127)),
    PINK(TextColor.color(255, 105, 180)),
    DARK_PINK(TextColor.color(231, 84, 128)),
    WHITE(TextColor.color(255, 255, 255)),
    BLACK(TextColor.color(0, 0, 0)),
    LIGHT_GRAY(TextColor.color(127, 127, 127)),
    DARK_GRAY(TextColor.color(65, 65, 65));

    final TextColor color;

    BasicColors(TextColor color) {
        this.color = color;
    }

    public TextColor getColor() {
        return color;
    }
}