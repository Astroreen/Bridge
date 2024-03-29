package bridge.database;

import bridge.Bridge;
import common.database.Connector;
import common.database.Saver;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Saves the data to the database asynchronously.
 */
@CustomLog
public class AsyncSaver extends Thread implements Listener, Saver {

    /**
     * The connector that connects to the database.
     */
    private final Connector con;

    /**
     * The queue of records to be saved to the database.
     */
    private final ConcurrentLinkedQueue<java.lang.Record> queue;

    /**
     * Whether the saver is currently running or not.
     */
    private boolean running;

    /**
     * Creates new database saver thread.
     */
    public AsyncSaver() {
        super();
        this.con = new Connector(Bridge.getInstance().getDB());
        this.queue = new ConcurrentLinkedQueue<>();
        this.running = true;
        Bukkit.getPluginManager().registerEvents(this, Bridge.getInstance());
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
                        LOG.error("There was a exception with SQL", e);
                    }
                }
            }
            if (!active) {
                con.refresh();
                active = true;
            }
            final Record rec = (Record) queue.poll();
            if (rec != null) {
                con.updateSQL(rec.type(), rec.args());
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
