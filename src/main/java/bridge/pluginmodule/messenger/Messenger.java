package bridge.pluginmodule.messenger;

import bridge.Bridge;
import bridge.listeners.ListenerManager;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import common.messanger.Action;
import common.messanger.Channel;
import common.messanger.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Messenger for writing a message both as
 * SocketManager and PluginMessage, and then
 *
 */
public class Messenger implements PluginMessageListener, Listener {

    private static Bridge plugin;
    private final LinkedHashMap<Action, String[]> queue;
    private final AsyncSender sender;
    private final SocketManager manager;

    /**
     * Delay, after which plugin message will be sent (in ticks).
     * In case if player just joined to the server.
     */
    private final int delay = 20;

    public Messenger(final @NotNull Bridge plugin) {
        Messenger.plugin = plugin;
        ListenerManager.register("Messenger", this);
        this.queue = new LinkedHashMap<>();
        final String ip = plugin.getPluginConfig().getString("settings.modules.updater.ip", "localhost");
        final int port = plugin.getPluginConfig().getInt("settings.modules.updater.port", 3820);
        this.manager = new SocketManager(ip, port);
        sender = new AsyncSender();
        sender.start();
    }

    public void register() {
        for (Channel channel : Channel.values())
            register(channel);
        manager.register();
    }

    public void register(final @NotNull Channel channel) {
        if(isPluginMessagingEnabled()){
            plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel.name);
            plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel.name, this);
        }
        manager.register(channel);
    }

    public void unregister() {
        for (Channel channel : Channel.values())
            unregister(channel);
        manager.unregister();
    }

    public void unregister(final @NotNull Channel channel) {
        if(isPluginMessagingEnabled()){
            plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, channel.name);
            plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, channel.name, this);
        }
        manager.unregister(channel);
    }

    public void stop() {
        if (SocketManager.isServer()) {
            manager.stopServer();
        } else {
            manager.stopClient();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (queue.isEmpty()) return;
        for (Action action : queue.keySet()) {
            send(event.getPlayer().getUniqueId(), action, queue.get(action));
        }
    }

    @Override
    public void onPluginMessageReceived(final @NotNull String c, final @NotNull Player player, final byte @NotNull [] msg) {
        //get channel
        Channel channel = null;
        for (Channel chan : Channel.values()) {
            if (c.equals(chan.name)) {
                channel = chan;
                break;
            }
        }
        if (channel == null) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(msg);
        final String subchannel = in.readUTF();

        //get action
        Action action = null;
        for (Action a : Action.values()) {
            if (subchannel.equals(a.subchannel)) {
                action = a;
                break;
            }
        }
        if (action == null) return;


        ConcurrentLinkedQueue<String> data = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < action.lines; i++) data.add(in.readUTF());
        //handle data
        new ActionHandler(channel, action, data);
    }

    /**
     * Execute action at the earliest opportunity.
     *
     * @param action action that will be executed
     * @param data   data, that will be put to packet
     */
    public void makeReservation(final @NotNull Action action, final String... data) {
        if (ServerHavePeople()) {
            UUID uuid = Iterables.getFirst(Bukkit.getOnlinePlayers(), null).getUniqueId();
            send(uuid, action, data);
            //TODO delete from queue if answer came from SocketData
        } else queue.put(action, data);
    }

    /**
     * Send plugin message to exact player.
     *
     * @param uuid   uuid of the player
     * @param action action that will be executed
     * @param data   data that will be put to packet
     */
    public void send(final @NotNull UUID uuid, final @NotNull Action action, final String... data) {
        if(!isPluginMessagingEnabled()) return;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        //firstly, writing channel name based on action
        final String channel;
        switch (action) {
            case RUN_UPDATER -> {
                channel = Channel.BRIDGE.name;
                out.writeUTF(action.subchannel);
            }
            case GET_SERVER, GET_SERVERS -> {
                channel = Channel.BUNGEECORD.name;
                out.writeUTF(action.subchannel);
            }
            default -> channel = null;
        }

        //then, writing information based on how much action should contain data
        for (int i = 0; i < action.lines; i++) out.writeUTF(data[i]);

        if(channel == null) return;
        final byte[] info = out.toByteArray();
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> sender.add(new AsyncSender.Record(uuid, channel, info)), delay);
    }

    public void reload() {
        final String ip = plugin.getPluginConfig().getString("settings.modules.updater.ip", "localhost");
        final int port = plugin.getPluginConfig().getInt("settings.modules.updater.port", 3820);
        manager.refresh(ip, port);
    }

    public Sender getSender() {
        return sender;
    }

    public SocketManager getSocketManager() {return manager;}

    public static boolean ServerHavePeople(){
        return !Bukkit.getOnlinePlayers().isEmpty();
    }
    //TODO make answering plugin on bungeecord side.
    private boolean isPluginMessagingEnabled() {return false;}
}
