package velocity.pluginmodule.messenger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import common.messanger.Action;
import common.messanger.Channel;
import common.messanger.Messenger;
import org.jetbrains.annotations.NotNull;
import velocity.listener.ListenerManager;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessengerImpl implements Messenger {

    private final ProxyServer proxy;
    public MessengerImpl(final @NotNull ProxyServer proxy) {
        this.proxy = proxy;
        ListenerManager.register("Messenger", this);
    }
    @Override
    public void register(final @NotNull Channel channel) {
        proxy.getChannelRegistrar().register(() -> channel.name);
    }

    @Override
    public void unregister(final @NotNull Channel channel) {
        proxy.getChannelRegistrar().unregister(() -> channel.name);
    }

    @Subscribe @SuppressWarnings("UnstableApiUsage")
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

    @Override
    public void send(final @NotNull UUID uuid, final @NotNull Action action, final String... data) {
        //TODO make plugin message send function
    }

    @Override
    public void reserve(final @NotNull Action action, final String... data) {
//TODO make plugin message reserve function
    }

    @Override
    public void reload() {
//TODO make plugin message reload function
    }

    @Override
    public void disable() {
//TODO make plugin message disable function
    }
}
