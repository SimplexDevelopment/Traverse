package mc.unraveled.reforged.economy;

public class Coin {
    private final String name;
    private final String symbol;
    private final double value;

    public Coin(String name, String symbol, double value) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getValue() {
        return value;
    }
}
