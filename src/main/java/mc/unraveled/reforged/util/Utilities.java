package mc.unraveled.reforged.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public static Date parseDate(String duration) {
        TimeUnit unit;
        long amount;

        unit = switch (duration.charAt(duration.length() - 1)) {
            case 's' -> TimeUnit.SECONDS;
            case 'h' -> TimeUnit.HOURS;
            case 'd' -> TimeUnit.DAYS;
            default -> TimeUnit.MINUTES;
        };

        amount = Long.parseLong(duration.substring(0, duration.length() - 1));

        return new Date(System.currentTimeMillis() + unit.toMillis(amount));
    }

    public static String parseDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }
}
