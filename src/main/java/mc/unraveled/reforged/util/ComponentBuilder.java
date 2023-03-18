package mc.unraveled.reforged.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ComponentBuilder {
    private Component master = Component.empty();

    public static ComponentBuilder begin() {
        return new ComponentBuilder();
    }

    public ComponentBuilder append(String text) {
        Component i = Component.text(text);
        master = master.append(i);
        return this;
    }

    public ComponentBuilder append(String text, NamedTextColor color) {
        Component i = Component.text(text, color);
        master = master.append(i);
        return this;
    }

    public ComponentBuilder append(long value) {
        Component i = Component.text(value);
        master = master.append(i);
        return this;
    }

    public ComponentBuilder append(long value, NamedTextColor color) {
        Component i = Component.text(value, color);
        master = master.append(i);
        return this;
    }

    public Component build() {
        return master;
    }
}
