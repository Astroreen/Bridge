package bridge.pluginmodule.messenger;

import bridge.Bridge;
import common.messanger.AbstractSocketManager;
import common.messanger.Action;
import common.messanger.Channel;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

@CustomLog(topic = "SocketManager")
public class SocketManager extends AbstractSocketManager {
    // essential //
    private static Bridge plugin;
    private static int runningTaskID = -1;
    // client //
    private int ClientReadingThread = -1;

    public SocketManager(final String ip, final int port) {
        super(ip, port);
        plugin = Bridge.getInstance();
    }

    /**
     * Send data to the socket.
     *
     * @param socket  {@link Socket} to send data to.
     * @param channel what {@link Channel} should we use
     * @param action  what {@link Action} should we send to channel
     * @param data    data to send
     */
    @Override
    protected void send(final @NotNull Socket socket, final @NotNull Channel channel, final @NotNull Action action, final String... data) {
        if (socket.isClosed()) return;
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
    @Override
    protected void startServer(final int port) throws SecurityException, IllegalArgumentException {
        LOG.debug("Starting ServerSocket.");
        running = true;
        isServer = true;
        runningTaskID = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (server != null) server.close();
                server = new ServerSocket(port);
                while (running) {
                    //method accept() will block the path until someone connects.
                    Socket client = null; // The client is the sender.
                    try {
                        client = server.accept();
                    } catch (IOException ex) {
                        //nothing
                    }
                    if (client != null && !client.isClosed()) {
                        client.setKeepAlive(true);
                        onConnectSocket(client);
                    }
                }
            } catch (IOException e) {
                LOG.error("There was an exception with ServerSocket", e);
                if (running) stopServer();
            }
        }).getTaskId();
        LOG.debug("Done.");
    }

    /**
     * Closes server.
     */
    @Override
    protected void stopServer() {
        isServer = false;
        running = false;
        LOG.debug("Closing ServerSocket...");
        //closing sockets before closing server
        for (String name : getSocketsName()) {
            SocketData SocketData = ConnectedSockets.get(name);
            if (SocketData == null) {
                ConnectedSockets.remove(name, null);
            } else {
                try {
                    final int id = SocketData.threadID();
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
    @Override
    protected void startClient(final String ip, final int port)
            throws SecurityException, IllegalArgumentException {
        LOG.debug("Starting ClientSocket.");
        isServer = false;
        running = true;
        runningTaskID = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (client != null) client.close();
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
    @Override
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
    @Override
    protected @NotNull Integer startReadingThread(final @NotNull Socket socket) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                //"The InputStream returned from Socket on both ends will block if there is no data to be read"
                //https://stackoverflow.com/questions/28137972/is-there-an-event-in-java-socket-when-socket-receive-data
                final InputStream in = socket.getInputStream();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
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
                    if (channel == null || !registeredChannels.contains(channel)) return;

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
                    final ConcurrentLinkedQueue<String> data = new ConcurrentLinkedQueue<>();
                    for (int i = 0; i < action.lines; i++) data.add(reader.readLine());
                    //handle action
                    new ServerActionHandler(channel, action, data);
                }
                //closing InputStream if client or server stops working
                in.close();
                reader.close();
            } catch (IOException e) {
                LOG.error("There was an exception while trying to read SocketData data", e);
            }

        }).getTaskId();
    }
}
