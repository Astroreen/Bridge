package bridge.modules.messenger;

public enum Action {

    //TODO make Forward action (this is bungeecord subchannel)
    /**
     * Send: nothing.
     * Get: nothing.
     */
    RUN_UPDATER("RunBridgeUpdater"),
    /**
     * Send: nothing.
     * Get: server name.
     */
    GET_SERVER("GetServer"),
    /**
     * Send: nothing.
     * Get: list of servers (example: Server1, Server2, ...).
     */
    GET_SERVERS("GetServers");

    public final String subchannel;

    Action(String subchannel) {
        this.subchannel = subchannel;
    }
}
