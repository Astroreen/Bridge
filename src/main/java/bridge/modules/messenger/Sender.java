package bridge.modules.messenger;

import java.util.UUID;

public interface Sender {

    /**
     * Adds new record to the queue, where it will be saved to the database.
     *
     * @param rec Record to save
     */
    void add(Record rec);

    /**
     * Ends this saver's job, letting it save all remaining data.
     */
    void end();

    /**
     * Holds the data and the method of executing them.
     */
    record Record (UUID uuid, String channel, byte[] msg){
        /**
         * Creates new Record, which can be saved to the database using
         * {@code Sender.add()}.
         *
         * @param uuid player uuid to send data from
         * @param channel to which message will be sent
         * @param msg the plugin message
         */
        public Record {
        }
    }
}
