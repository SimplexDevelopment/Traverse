package mc.unraveled.reforged.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.List;

public final class Utilities {
    public static <T> @NotNull String serialize(@NotNull List<Pair<String, T>> objectPairs) {
        char delimiter = ':';
        char end = ';';
        StringBuilder builder = new StringBuilder();
        for (Pair<String, ?> pair : objectPairs) {
            builder.append(pair.getFirst().charAt(0));
            builder.append(delimiter);
            builder.append(pair.getSecond());
            builder.append(end);
        }
        return builder.toString();
    }
}
