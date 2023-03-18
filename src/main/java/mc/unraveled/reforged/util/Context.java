package mc.unraveled.reforged.util;

public class Context<S> {
    private S state;

    public Context(S state) {
        this.state = state;
    }

    public S getState() {
        return state;
    }

    public void setState(S state) {
        this.state = state;
    }
}
