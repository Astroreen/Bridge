package velocity.pluginmodule.messenger;

import bridge.Bridge;
import com.velocitypowered.api.proxy.Player;
import common.messanger.Sender;
import lombok.CustomLog;
import velocity.BridgeVelocity;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

@CustomLog
public class AsyncSender extends Thread implements Sender {

    /**
     * The queue of records to be sent by plugin message.
     */
    private final ConcurrentLinkedQueue<java.lang.Record> queue;

    /**
     * Whether the saver is currently running or not.
     */
    private boolean running;

    private static final Bridge plugin = Bridge.getInstance();

    /**
     * Creates new database saver thread.
     */
    public AsyncSender() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.running = true;
        BridgeVelocity.getInstance().getProxy()
                .getEventManager().register(BridgeVelocity.getInstance(), this);
    }

    @Override
    public void run() {
        boolean active = false;
        while (true) {
            while (queue.isEmpty()) {
                if (!running) {
                    return;
                }
                synchronized (this) {
                    try {
                        active = false;
                        wait();
                    } catch (final InterruptedException e) {
                        LOG.error("There was an exception in AsyncSender module", e);
                    }
                }
            }
            if (!active) {
                active = true;
            }
            final Record rec = (Record) queue.poll();
            if (rec != null) {
                final Optional<Player> p = BridgeVelocity.getInstance().getProxy().getPlayer(rec.uuid());
                if (p.isEmpty()) {
                    LOG.warn("Couldn't execute Messenger's action because player was null. Skipping it.");
                    queue.add(rec);
                    return;
                }
                p.get().sendPluginMessage(rec::channel, rec.msg());
            }
        }
    }

    @Override
    public void add(final Record rec) {
        synchronized (this) {
            queue.add(rec);
            notifyAll();
        }
    }


    @Override
    public void end() {
        synchronized (this) {
            running = false;
            notifyAll();
        }
    }
}
