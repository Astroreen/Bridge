package velocity.pluginmodule.messenger;

import common.messanger.Action;
import common.messanger.ActionHandler;
import common.messanger.Channel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentLinkedQueue;

public class VelocityActionHandler extends ActionHandler {

    private final Channel channel;
    private final Action action;
    private final ConcurrentLinkedQueue<String> data;

    protected VelocityActionHandler(Channel channel, Action action, ConcurrentLinkedQueue<String> data) {
        super(channel, action, data);
        this.channel = channel;
        this.action = action;
        this.data = data;
    }

    @Override
    protected void schedule(@NotNull Runnable task) {
        new Thread(this).start();
    }

    @Override
    protected void ServerHandler(@NotNull Action action, ConcurrentLinkedQueue<String> data) {
        //TODO fill with actions
    }

    @Override
    protected void ProxyHandler(@NotNull Action action, ConcurrentLinkedQueue<String> data) {
        //TODO fill with actions
    }
}
