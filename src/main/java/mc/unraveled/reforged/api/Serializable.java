package mc.unraveled.reforged.api;

public interface Serializable<T> {
    String serialize();

    T deserialize(String formatted);
}
