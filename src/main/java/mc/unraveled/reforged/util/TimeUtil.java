package mc.unraveled.reforged.util;

public class TimeUtil {
    public static final long SECOND = 1000L;
    public static final long MINUTE = SECOND * 60L;
    public static final long HOUR = MINUTE * 60L;
    public static final long DAY = HOUR * 24L;
    public static final long WEEK = DAY * 7L;
    public static final long MONTH = DAY * 30L;
    public static final long YEAR = DAY * 365L;
    public static final long TICK = SECOND / 20L;

    public static long parse(String input) {
        long duration = 0L;
        StringBuilder number = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                String str = number.toString();
                if (str.isEmpty()) continue;
                long parsed = Long.parseLong(str);
                number = new StringBuilder();
                switch (c) {
                    case 's' -> duration += parsed * SECOND;
                    case 'm' -> duration += parsed * MINUTE;
                    case 'h' -> duration += parsed * HOUR;
                    case 'd' -> duration += parsed * DAY;
                    case 'w' -> duration += parsed * WEEK;
                    case 'M' -> duration += parsed * MONTH;
                    case 'y' -> duration += parsed * YEAR;
                }
            }
        }
        return duration;
    }
}
