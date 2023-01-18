package velocity.pluginmodule.messenger;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import common.messanger.Action;
import common.messanger.Channel;
import common.messanger.Messenger;
import org.jetbrains.annotations.NotNull;
import velocity.listener.ListenerManager;

import java.util.UUID;

public class MessengerImpl implements Messenger {

    private final ProxyServer proxy;
    public MessengerImpl(final @NotNull ProxyServer proxy) {
        this.proxy = proxy;
        ListenerManager.register("Messenger", this);
    }
    @Override
    public void register(@NotNull Channel channel) {

    }

    @Override
    public void unregister(@NotNull Channel channel) {

    }

    @Subscribe
    public void onPluginMessageEvent(final @NotNull PluginMessageEvent event) {

    }

    @Override
    public void send(@NotNull UUID uuid, @NotNull Action action, String... data) {

    }

    @Override
    public void reserve(@NotNull Action action, String... data) {

    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }
}
