package mc.unraveled.reforged.api;

public interface Baker {
    /**
     * This method should force immutability on all lists, maps, and sets.
     * This method should also compile raw data into a compound data format,
     * and mark that data as immutable.
     */
    void bake();

    /**
     * This method should force mutability on all lists, maps, and sets.
     * This method should also decompile compound data formats into raw data,
     * and mark that data as mutable.
     */
    void unbake();
}
