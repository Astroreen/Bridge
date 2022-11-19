package bridge.modules.messenger;

import bridge.Bridge;
import bridge.event.ConnectingSocketEvent;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

@CustomLog(topic = "SocketManager")
public class SocketManager implements Listener {
    //essential
    private static Bridge plugin;
    private String ip;
    private int port;
    private static boolean isServer;
    private static boolean running;
    private static int runningTaskID = -1;
    private static int sendTimer = -1;
    private static final List<Channel> registered = new ArrayList<>();


    // server //
    private static final HashMap<String, SocketData> ConnectedSockets = new HashMap<>();
    private final String UNDEFINED_SOCKET = "UndefinedSocket_";
    private ServerSocket server = null;
    // client //
    private Socket client = null;
    private int ClientReadingThread = -1;

    SocketManager(final String ip, final int port) {
        plugin = Bridge.getInstance();
        plugin.getListenerManager().registerListener("SocketManager", this);
        running = false;
        refresh(ip, port);
    }

    public void register() {
        for (Channel channel : Channel.values()) register(channel);
    }

    public void register(Channel channel) {
        registered.add(channel);
    }

    public void unregister() {
        for (Channel channel : Channel.values()) unregister(channel);
    }

    public void unregister(Channel channel) {
        registered.remove(channel);
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
            for (String name : ConnectedSockets.keySet()) {
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onConnectingSocketEvent(@NotNull ConnectingSocketEvent event) {
        if (event.isCancelled()) return;

        Socket socket = event.getSocket();
        if (socket == null) return;
        //later or now we will get this Socket's name, so Socket's ID isn't really matter.
        ConnectedSockets.put(UNDEFINED_SOCKET + ConnectedSockets.size(), new SocketData(socket, startReadingThread(socket)));
        send(socket, Channel.BRIDGE, Action.GET_SERVER);
        LOG.debug("Hey! A new socket joined our party! Cool.");
    }

    /**
     * Forward data to all known and connected sockets.
     *
     * @param channel what {@link Channel} should we use
     * @param action what {@link Action} should we send to channel
     * @param data data to send
     */
    public void forward(final @NotNull Channel channel, final @NotNull Action action, final String... data){
        for (String SocketName : ConnectedSockets.keySet()){
            SocketData SocketData = ConnectedSockets.get(SocketName);
            if(SocketData == null || SocketData.socket() == null) continue;
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
    public void send(final String socketName, final @NotNull Channel channel, final @NotNull Action action, final String... data) {
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
    private void send(final @NotNull Socket socket, final @NotNull Channel channel, final @NotNull Action action, final String... data) {
        if(socket.isClosed()) return;
        try {
            final OutputStream out = socket.getOutputStream();
            final PrintWriter writer = new PrintWriter(out);

            writer.write(channel.name);
            writer.write(action.subchannel);
            for (int i = 0; i < action.lines; i++) writer.write(data[i]);
            writer.flush();

            if (sendTimer != -1) Bukkit.getScheduler().cancelTask(sendTimer);
            //restart timer to close OutputStream after 5 min
            sendTimer = Bukkit.getScheduler().runTaskLaterAsynchronously(Bridge.getInstance(), () -> {
                writer.close();
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error("There was an exception closing socket writer", e);
                }
                sendTimer = -1;
                //5 minutes
            }, 20 * 60 * 5).getTaskId();
        } catch (IOException e) {
            LOG.error("There was an exception sending data through socket", e);
        }
    }

    /**
     * Starts {@link ServerSocket}.
     *
     * @param port the port number, or 0 to use a port number that is automatically allocated.
     * @throws SecurityException        if a security manager exists and its checkListen method doesn't allow the operation.
     * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values, which is between 0 and 65535,
     */
    private void startServer(final int port) throws SecurityException, IllegalArgumentException {
        LOG.debug("Starting ServerSocket.");
        running = true;
        isServer = true;
        runningTaskID = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if(server != null) server.close();
                server = new ServerSocket(port);
                while (running) {
                    //method accept() will block the path until someone connects.
                    Socket client = null; // The client is the sender.
                    try {
                        client = server.accept();
                    } catch (IOException ex) {
                        //nothing
                    }
                    if(client != null && !client.isClosed()){
                        client.setKeepAlive(true);
                        Bukkit.getPluginManager().callEvent(new ConnectingSocketEvent(client));
                    }
                }
            } catch (IOException e) {
                LOG.error("There was an exception with ServerSocket", e);
                if(running) stopServer();
            }
        }).getTaskId();
        LOG.debug("Done.");
    }

    /**
     * Closes server.
     */
    protected void stopServer() {
        isServer = false;
        running = false;
        LOG.debug("Closing ServerSocket...");
        //closing sockets before closing server
        for (String name : ConnectedSockets.keySet()) {
            SocketData SocketData = ConnectedSockets.get(name);
            if (SocketData == null) {
                ConnectedSockets.remove(name, null);
            } else {
                try {
                    final int id = SocketData.threadID;
                    if (id != -1) Bukkit.getScheduler().cancelTask(id);
                    SocketData.socket().close();
                } catch (IOException e) {
                    LOG.error("There was an exception while trying to close SocketData " + name, e);
                }
            }
        }
        ConnectedSockets.clear();
        //closing server
        try {
            if (server != null) {
                server.close();
                server = null;
            }
        } catch (IOException e) {
            LOG.error("An unexpected exception occurred while trying to close running SocketServer", e);
        }
        if (runningTaskID != -1) Bukkit.getScheduler().cancelTask(runningTaskID);
        LOG.debug("Done.");
    }


    /**
     * Starts a new {@link Socket}.
     *
     * @param ip   the host name, or null for the loopback address.
     * @param port the port number.
     * @throws SecurityException        if a security manager exists and its checkConnect method doesn't allow the operation.
     * @throws IllegalArgumentException if the port parameter is outside the specified range of valid port values, which is between 0 and 65535, inclusive.
     */
    private void startClient(final String ip, final int port)
            throws SecurityException, IllegalArgumentException {
        LOG.debug("Starting ClientSocket.");
        isServer = false;
        running = true;
        runningTaskID = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if(client != null ) client.close();
                client = new Socket(ip, port);
                ClientReadingThread = startReadingThread(client);
            } catch (IOException e) {
                LOG.error("There was an exception with ClientSocket", e);
                stopClient();
                refresh(ip, port);
            }
        }).getTaskId();
        LOG.debug("Done.");
    }

    /**
     * Closes client.
     */
    protected void stopClient() {
        isServer = false;
        running = false;
        LOG.debug("Closing ClientSocket...");
        try {
            if (ClientReadingThread != -1)
                Bukkit.getScheduler().cancelTask(ClientReadingThread);
            if (client != null) {
                client.close();
                client = null;
            }
        } catch (IOException e) {
            LOG.error("An unexpected exception occurred while trying to close running ClientSocket", e);
        }
        if (runningTaskID != -1) Bukkit.getScheduler().cancelTask(runningTaskID);
        LOG.debug("Done.");
    }

    /**
     * Start a reading thread using bukkit.
     *
     * @param socket SocketData to start a thread for.
     * @return TaskId from scheduler.
     */
    private int startReadingThread(final Socket socket) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                //"The InputStream returned from Socket on both ends will block if there is no data to be read"
                //https://stackoverflow.com/questions/28137972/is-there-an-event-in-java-socket-when-socket-receive-data
                InputStream in = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while (running) {

                    //get channel
                    String rawChannel = reader.readLine();
                    if (rawChannel == null) {
                        LOG.debug("Received channel was null. Is somebody else listening to this port?");
                        return;
                    }
                    Channel channel = null;
                    for (Channel c : Channel.values()) {
                        if (rawChannel.equals(c.name())) {
                            channel = c;
                            break;
                        }
                    }
                    if (channel == null || !registered.contains(channel)) return;

                    //get action
                    String rawAction = reader.readLine();
                    if (rawAction == null) {
                        LOG.warn("Received action was null, but channel was correct.");
                        return;
                    }
                    Action action = null;
                    for (Action a : Action.values()) {
                        if (rawAction.equals(a.subchannel)) {
                            action = a;
                            break;
                        }
                    }
                    if (action == null) return;

                    //get necessary data to run action
                    ConcurrentLinkedQueue<String> data = new ConcurrentLinkedQueue<>();
                    for (int i = 0; i < action.lines; i++) data.add(reader.readLine());
                    //handle action
                    new ActionHandler(channel, action, data);
                }
                //closing InputStream if client or server stops working
                in.close();
                reader.close();
            } catch (IOException e) {
                LOG.error("There was an exception while trying to read SocketData data", e);
            }

        }).getTaskId();
    }

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
    public Set<String> getSockets() {
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

    record SocketData(Socket socket, int threadID) {
    }
}
