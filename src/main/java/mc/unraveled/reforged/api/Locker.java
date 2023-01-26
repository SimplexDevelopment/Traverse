package mc.unraveled.reforged.api;

public interface Locker {
    Object lock = new Object();

    default Object lock() {
        return lock;
    }
}
