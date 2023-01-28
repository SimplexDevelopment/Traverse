package mc.unraveled.reforged.storage;

public final class SQLConst {
    private SQLConst() {
        throw new AssertionError();
    }

    public static final String BEGIN = "BEGIN ";
    public static final String END = "END";
    public static final String END_SPACE = "END ";
    public static final String SEMICOLON = ";";
    public static final String ELSE = "ELSE ";

    public static String IF(String v) {
        return "IF NOT EXISTS (SELECT * FROM " + v + ") ";
    }

    public static String IF(String v, String u) {
        return "IF NOT EXISTS (SELECT * FROM " + v + " WHERE " + u + " = ?) ";
    }
}
