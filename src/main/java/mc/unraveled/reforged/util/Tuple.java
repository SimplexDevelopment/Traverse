package mc.unraveled.reforged.util;

public final class Tuple<S, T, U> {
    private S first;
    private T second;
    private U third;

    public Tuple(S first, T second, U third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public S getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public U getThird() {
        return third;
    }

    public void setFirst(S first) {
        this.first = first;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public void setThird(U third) {
        this.third = third;
    }
}
