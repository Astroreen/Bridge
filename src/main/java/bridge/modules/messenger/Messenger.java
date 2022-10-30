package bridge.modules.messenger;

import bridge.Bridge;
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

import java.util.LinkedHashMap;
import java.util.UUID;

public class Messenger implements PluginMessageListener, Listener {

    private final Bridge plugin;
    private final LinkedHashMap<Actions, String[]> queue;
    private final AsyncSender sender;

    public Messenger(Bridge plugin) {
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

    public void register(@NotNull Channels channels) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channels.name);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channels.name, this);
    }

    public void unregister() {
        for (Channels channel : Channels.values()) {
            unregister(channel);
        }
    }

    public void unregister(@NotNull Channels channels) {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, channels.name);
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, channels.name, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (queue.isEmpty()) return;
        for (Actions action : queue.keySet()) {
            send(event.getPlayer().getUniqueId(), action, queue.get(action));
        }
    }

    @Override
    public void onPluginMessageReceived(@NotNull String c, @NotNull Player player, byte @NotNull [] data) {
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
        Actions action = null;
        for (Actions a : Actions.values()) {
            if (subchannel.equals(a.subchannel)) {
                action = a;
                break;
            }
        }
        if (action == null) return;

        //execute action
        if (channel == Channels.BRIDGE) {
            switch (action) {
                case RUN_UPDATER -> {
                    //TODO make executable action
                }
            }
        }
    }

    public void makeReservation(@NotNull Actions action, String... data) {
        if (data != null) queue.put(action, data);
    }

    public void send(@NotNull UUID uuid, @NotNull Actions action, @NotNull String... data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        String channel = null;
        switch (action) {
            case RUN_UPDATER -> {
                channel = Channels.BRIDGE.name;
                out.writeUTF("BridgeUpdater");
                if (data.length != 3) return;

                out.writeUTF(data[0]);
            }
        }
        sender.add(new AsyncSender.Record(uuid, channel, out.toByteArray()));
    }

    public Sender getSender() {
        return sender;
    }

    public enum Actions {
        //TODO make Forward action (this is bungeecord subchannel)
        RUN_UPDATER("BridgeUpdater");

        private final String subchannel;

        Actions(String subchannel) {
            this.subchannel = subchannel;
        }
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
