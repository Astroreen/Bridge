package bridge.modules.messenger;

import bridge.Bridge;
import bridge.event.GetServerEvent;
import bridge.event.GetServersEvent;
import bridge.event.RunUpdaterEvent;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.UUID;

public class Messenger implements PluginMessageListener, Listener {

    private final Bridge plugin;
    private final LinkedHashMap<Action, String[]> queue;
    private final AsyncSender sender;

    /**
     * Delay, after which plugin message will be sent (in ticks).
     * In case if player just joined to the server.
     */
    private final int delay = 20;

    public Messenger(final Bridge plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.queue = new LinkedHashMap<>();
        sender = new AsyncSender();
        sender.start();
    }

    public void register() {
        for (Channels channel : Channels.values()) {
            register(channel);
        }
    }

    public void register(final @NotNull Channels channels) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channels.name);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channels.name, this);
    }

    public void unregister() {
        for (Channels channel : Channels.values()) {
            unregister(channel);
        }
    }

    public void unregister(final @NotNull Channels channels) {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, channels.name);
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, channels.name, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (queue.isEmpty()) return;
        for (Action action : queue.keySet()) {
            send(event.getPlayer().getUniqueId(), action, queue.get(action));
        }
    }

    @Override
    public void onPluginMessageReceived(final @NotNull String c, final @NotNull Player player, final byte @NotNull [] data) {
        //get channel
        Channels channel = null;
        for (Channels chan : Channels.values()) {
            if (c.equals(chan.name)) {
                channel = chan;
                break;
            }
        }
        if (channel == null) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(data);
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

        //execute action
        //Bridge messages
        if (channel == Channels.BRIDGE) {
            switch (action) {
                case RUN_UPDATER -> Bukkit.getPluginManager().callEvent(new RunUpdaterEvent());
            }

            //BungeeCord messages
        } else if (channel == Channels.BUNGEECORD) {
            switch (action) {
                case GET_SERVER -> Bukkit.getPluginManager().callEvent(new GetServerEvent(in.readUTF()));
                case GET_SERVERS -> Bukkit.getPluginManager().callEvent(new GetServersEvent(in.readUTF().split(", ")));
            }
        }
    }

    /**
     * Execute action at the earliest opportunity.
     *
     * @param action action that will be executed
     * @param data   data, that will be put to packet
     */
    public void makeReservation(final @NotNull Action action, final String... data) {
        Collection<? extends Player> online = Bukkit.getOnlinePlayers();
        if (!online.isEmpty()) {
            UUID uuid = Iterables.getFirst(online, null).getUniqueId();
            send(uuid, action, data);
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
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        //channel to send to
        final String channel;
        switch (action) {
            case RUN_UPDATER -> {
                channel = Channels.BRIDGE.name;
                out.writeUTF(action.subchannel);
            }
            case GET_SERVER, GET_SERVERS -> {
                channel = Channels.BUNGEECORD.name;
                out.writeUTF(action.subchannel);
            }
            default -> channel = null;
        }


        if(channel == null) return;
        final byte[] info = out.toByteArray();
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> sender.add(new AsyncSender.Record(uuid, channel, info)), delay);
    }

    public Sender getSender() {
        return sender;
    }

    private enum Channels {
        BUNGEECORD("BungeeCord"),
        BRIDGE("Bridge");

        private final String name;

        Channels(String name) {
            this.name = name;
        }
    }
}
