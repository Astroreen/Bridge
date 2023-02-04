package velocity.pluginmodule.messenger;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import common.messanger.Action;
import common.messanger.Channel;
import common.messanger.Messenger;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import velocity.listener.ListenerManager;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessengerImpl implements Messenger {

    private final ProxyServer proxy;
    private final LinkedHashMap<Action, String[]> queue;
    private final AsyncSender sender;

    public MessengerImpl(final @NotNull ProxyServer proxy) {
        this.proxy = proxy;
        ListenerManager.register("Messenger", this);
        this.queue = new LinkedHashMap<>();
        sender = new AsyncSender();
        sender.start();
    }

    @Override
    public void register(final @NotNull Channel channel) {
        proxy.getChannelRegistrar().register(() -> channel.name);
    }

    @Override
    public void unregister(final @NotNull Channel channel) {
        proxy.getChannelRegistrar().unregister(() -> channel.name);
    }

    @Subscribe
    public void onPlayerJoinEvent(final @NotNull ServerConnectedEvent event){
        if(queue.isEmpty()) return;
        for(final @NotNull Action action : queue.keySet())
            send(event.getPlayer().getUniqueId(), action, queue.get(action));

    }

    @Subscribe
    @SuppressWarnings("UnstableApiUsage")
    public void onPluginMessageEvent(final @NotNull PluginMessageEvent event) {
        //get channel
        Channel channel = null;
        for (final @NotNull Channel chan : Channel.values()) {
            if (chan.name.equals(event.getIdentifier().getId())) {
                channel = chan;
                break;
            }
        }
        if (channel == null) return;

        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
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

        final ConcurrentLinkedQueue<String> data = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < action.lines; i++) data.add(in.readUTF());
        //handle action
        new VelocityActionHandler(channel, action, data);
    }

    @Override @SuppressWarnings("UnstableApiUsage")
    public void send(final @NotNull UUID uuid, final @NotNull Action action, final String... data) {
        //TODO make plugin message send function
        if(!isPluginMessagingEnabled()) return;
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

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

        sender.add(new AsyncSender.Record(uuid, channel, info));
    }

    @Override
    public void reserve(final @NotNull Action action, final String... data) {
        if(!proxy.getAllPlayers().isEmpty()){
            final UUID uuid = Iterables.getFirst(Bukkit.getOnlinePlayers(), null).getUniqueId();
            send(uuid, action, data);
        } else queue.put(action, data);
    }

    @Override
    public void reload() {
        //Empty
    }

    @Override
    public void disable() {
        sender.end();
    }

    //TODO get answer from config
    private boolean isPluginMessagingEnabled() {return false;}
}
