package common.messanger;

import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@CustomLog(topic = "SocketManager")
public abstract class AbstractSocketManager {

    // essential //
    private String ip;
    private int port;
    protected static boolean isServer;
    protected static boolean running;
    protected static int sendTimer = -1;
    protected static final List<Channel> registeredChannels = new ArrayList<>();


    // server //
    protected final HashMap<String, SocketData> ConnectedSockets = new HashMap<>();
    private static final String UNDEFINED_SOCKET = "UndefinedSocket_";
    protected ServerSocket server = null;
    // client //
    protected Socket client = null;

    public AbstractSocketManager(final String ip, final int port) {
        running = false;
        refresh(ip, port);
    }

    public void register() {
        for (Channel channel : Channel.values()) register(channel);
    }

    public void register(final @NotNull Channel channel) {
        registeredChannels.add(channel);
    }

    public void unregister() {
        for (Channel channel : Channel.values()) unregister(channel);
    }

    public void unregister(final @NotNull Channel channel) {
        registeredChannels.remove(channel);
    }

    public void refresh(final String ip, final int port) {
        //data validation
        if (!isIPValid(ip) || !isIPReachable(ip)) {
            LOG.error("IP '" + ip + "' is either not valid or not reachable!");
            return;
        } else if (!isPortValid(port)) {
            LOG.error("PORT '" + port + "' is not valid!");
            return;
        }

        if (running && (this.port != port || !this.ip.equals(ip))) {
            if (isServer) {
                stopServer();
                startServer(port);
            } else {
                stopClient();
                startClient(ip, port);
            }
        }

        this.ip = ip;
        this.port = port;

        if (!running) {
            if (isPortValid(port) && isPortAvailable(port)) startServer(port);
            else if (isIPValid(ip) && isPortValid(port)) startClient(ip, port);
            else {
                LOG.warn("Can't start ServerSocket and ClientSocket. Check your port and ip in config.");
                LOG.warn("Module 'Updater' is disabled until reload.");
            }
            return;
        }
        if (isServer) {
            for (String name : getSocketsName()) {
                //get SocketData
                SocketData SocketData = ConnectedSockets.get(name);
                //is Socket still reachable?
                if (SocketData == null) {
                    ConnectedSockets.remove(name);
                    continue;
                } else if (SocketData.socket() == null || SocketData.socket().isClosed()) {
                    final int ID = SocketData.threadID;
                    if (ID != -1) Bukkit.getScheduler().cancelTask(ID);
                    ConnectedSockets.remove(name, SocketData);
                    continue;
                }
                //get undefined Socket name
                if (name.matches(UNDEFINED_SOCKET))
                    send(SocketData.socket(), Channel.BRIDGE, Action.GET_SERVER);
            }
        } else {
            //checking if port is available, if it is, stopping client and starting server.
            if (isPortValid(port) && isPortAvailable(port)) {
                stopClient();
                startServer(port);
            }
        }

    }

    public void onConnectSocket(final @NotNull Socket socket) {
        //later or now we will get this Socket's name, so Socket's ID isn't really matter.
        ConnectedSockets.put(
                UNDEFINED_SOCKET + ConnectedSockets.size(), //sockets id
                new SocketData(socket, startReadingThread(socket))
        );
        send(socket, Channel.BRIDGE, Action.GET_SERVER);
        LOG.debug("Hey! A new socket joined our party! Cool.");
    }

    /**
     * Forward data to all known and connected sockets.
     *
     * @param channel what {@link Channel} should we use
     * @param action  what {@link Action} should we send to channel
     * @param data    data to send
     */
    public void forward(final @NotNull Channel channel, final @NotNull Action action, final String... data) {
        for (String SocketName : ConnectedSockets.keySet()) {
            SocketData SocketData = ConnectedSockets.get(SocketName);
            if (SocketData == null || SocketData.socket() == null) continue;
            send(SocketData.socket(), channel, action, data);
        }
    }

    /**
     * Send data to the socket using his name.
     *
     * @param socketName name of the socket
     * @param channel    what {@link Channel} should we use
     * @param action     what {@link Action} should we send to channel
     * @param data       data to send
     */
    public void send(final @NotNull String socketName, final @NotNull Channel channel, final @NotNull Action action, final String... data) {
        if (!ConnectedSockets.containsKey(socketName)) return;
        SocketData SocketData = ConnectedSockets.get(socketName);
        if (SocketData == null || SocketData.socket() == null) return;
        send(SocketData.socket(), channel, action, data);
    }

    /**
     * Send data to the socket.
     *
     * @param socket  {@link Socket} to send data to.
     * @param channel what {@link Channel} should we use
     * @param action  what {@link Action} should we send to channel
     * @param data    data to send
     */
    protected abstract void send(final @NotNull Socket socket, final @NotNull Channel channel, final @NotNull Action action, final String... data);

    /**
     * Starts {@link ServerSocket}.
     *
     * @param port the port number, or 0 to use a port number that is automatically allocated.
     * @throws SecurityException        if a security manager exists and its checkListen method doesn't allow the operation.
     * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values, which is between 0 and 65535,
     */
    protected abstract void startServer(final int port) throws SecurityException, IllegalArgumentException;

    /**
     * Closes server.
     */
    protected abstract void stopServer();


    /**
     * Starts a new {@link Socket}.
     *
     * @param ip   the host name, or null for the loopback address.
     * @param port the port number.
     * @throws SecurityException        if a security manager exists and its checkConnect method doesn't allow the operation.
     * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values, which is between 0 and 65535, inclusive.
     */
    protected abstract void startClient(final String ip, final int port) throws SecurityException, IllegalArgumentException;

    /**
     * Closes client.
     */
    protected abstract void stopClient();

    protected abstract @Nullable Integer startReadingThread(final @NotNull Socket socket);

    /**
     * Is this plugin is the "boss" of Sockets connection
     * and the only one listening to the port.
     *
     * @return true if it can listen to the port.
     */
    public static boolean isServer() {
        return isServer;
    }

    /**
     * Set of Sockets names.
     *
     * @return set of names
     */
    public Set<String> getSocketsName() {
        return ConnectedSockets.keySet();
    }

    private boolean isIPValid(String ip) {
        final Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return PATTERN.matcher(ip).matches();
    }

    private boolean isPortValid(int port) {
        return port >= 0 && port <= 65535;
    }

    private boolean isIPReachable(String ip) {
        try {
            return InetAddress.getByName(ip).isReachable(3000);
        } catch (IOException e) {
            return false;
        }

    }


    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    private boolean isPortAvailable(int port) {
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Invalid start port: " + port);
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            LOG.error("There was an exception while trying to check if port is available", e);
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                    LOG.error("Unexpected exception occurred while tried closing ServerSocket.");
                    LOG.reportException(e);
                }
            }
        }
        return false;
    }

    public record SocketData(Socket socket, Integer threadID) {
    }
}
