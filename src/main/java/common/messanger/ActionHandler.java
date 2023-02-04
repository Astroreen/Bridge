package common.messanger;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ActionHandler implements Runnable {

    private final Channel channel;
    private final Action action;
    private final ConcurrentLinkedQueue<String> data;

    protected ActionHandler(Channel channel, Action action, ConcurrentLinkedQueue<String> data) {
        this.channel = channel;
        this.action = action;
        this.data = data;
        schedule(this);
    }

    protected abstract void schedule(final @NotNull Runnable task);

    @Override
    public void run() {
        if (channel.equals(Channel.BRIDGE))
            ServerHandler(action, data);
        else if (channel.equals(Channel.BUNGEECORD))
            ProxyHandler(action, data);
    }

    protected abstract void ServerHandler(final @NotNull Action action, final ConcurrentLinkedQueue<String> data);

    protected abstract void ProxyHandler(final @NotNull Action action, final ConcurrentLinkedQueue<String> data);
}
