package bridge.modules.messenger;

public enum Channel {
    BUNGEECORD("bridge:bungeecord"),
    BRIDGE("bridge:main");

    public final String name;

    Channel(String name) {
        this.name = name;
    }
}
