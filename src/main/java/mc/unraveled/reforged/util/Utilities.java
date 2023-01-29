package mc.unraveled.reforged.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class Utilities {

    /**
     * A simple method to serialize a list of pairs into a string.
     * The second object in the pair can be of any type. However,
     * the second object MUST be serializable. The first object is a denotation device,
     * which will only use the first letter of your string. This is to save space in the final output.
     * For Example:
     * <p></p>
     * If you were to have a Pair of "name" and "Bob", a pair of "age" and 20, and a pair of "isAlive" and true,
     * the output would be
     * <p>
     * <b>n:Bob;a:20;i:true;</b></p>
     * The separator between each string and object is a colon, and the separator between each pair is a semicolon.
     * <p></p>
     *
     * @param objectPairs The list of pairs to serialize.
     * @param <T>         The type of the second object in the pair.
     * @return The serialized string.
     */
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

    /**
     * Convenient method to parse a date from a String which contains a numerical value.
     * It's important to note that the contents of the string is converted to a LONG, and thus it is
     * subject to the same limitations as a long, i.e. {@link Long#MAX_VALUE} and {@link Long#MIN_VALUE}.
     * <p>
     *
     * @param duration The duration to parse.
     * @return The parsed date.
     */
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

    /**
     * Converts the date into a readable string, with the format <b>dd/MM/yyyy HH:mm:ss</b>.
     * <p>
     *
     * @param date The date to convert.
     * @return The converted date.
     */
    public static String parseDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }

    /**
     * Converts seconds into ticks. One second = 20 ticks.
     * 1 tick = 1/20th of a second.
     * <p>
     *
     * @param seconds The amount in seconds to convert.
     * @return The amount in ticks.
     */
    public static long parseSecondsToTicks(long seconds) {
        return seconds * 20;
    }

    /**
     * Converts ticks into seconds. One second = 20 ticks.
     * 1 tick = 1/20th of a second.
     *
     * @param ticks The amount in ticks to convert.
     * @return The amount in seconds.
     */
    public static long parseTicksToSeconds(long ticks) {
        return ticks / 20;
    }

    /**
     * This method is necessary because Bukkit's {@link Bukkit#getOfflinePlayer(String)} method
     * returns an {@link OfflinePlayer} object, but may execute a blocking web request to retrieve
     * the UUID in question. To avoid this, we are wrapping this call in a {@link Mono} object.
     *
     * @param name The name of the player to retrieve.
     * @return A {@link Mono} object containing the {@link OfflinePlayer} object.
     */
    public static Mono<OfflinePlayer> getOfflinePlayer(String name) {
        return Mono.create(sink -> sink.success(Bukkit.getOfflinePlayer(name)));
    }
}
