package bridge.compatibility;

public enum CompatiblePlugin {

    TAB("TAB"),
    PLACEHOLDERAPI("PlaceholderAPI"),
    LUCKPERMS("LuckPerms"),
    ITEMSADDER("ItemsAdder"),
    FAWE("FastAsyncWorldEdit"),
    PROTOCOLLIB("ProtocolLib"),
    ;

    public final String name;
    CompatiblePlugin(final String name) {
        this.name = name;
    }
}
