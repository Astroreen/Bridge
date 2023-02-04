package common.messanger;

public enum Action {

    //TODO make Forward action (this is bungeecord subchannel)
    /**
     * Send: nothing.
     * Get: nothing.
     */
    RUN_UPDATER("RunBridgeUpdater", 0),
    /**
     * Send: nothing.
     * Get: server name.
     */
    GET_SERVER("GetServer", 1),
    /**
     * Send: nothing.
     * Get: list of servers (example: Server1, Server2, ...).
     */
    GET_SERVERS("GetServers", 1);

    public final String subchannel;
    public final int lines;

    Action(final String subchannel, final int lines) {
        this.subchannel = subchannel;
        this.lines = Math.max(lines, 0);
    }
}
