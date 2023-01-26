package mc.unraveled.reforged.util;

public final class Unary<T> {
    private T value;

    public Unary(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
