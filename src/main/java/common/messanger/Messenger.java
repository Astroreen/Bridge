package common.messanger;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Messenger {

    /**
     * Register channel for plugin messaging.
     *
     * @param channel plugin channel
     */
    void register(final @NotNull Channel channel);

    /**
     * Register <b>all existing channels<b/> for plugin messaging.
     */
    default void register() {
        for (final @NotNull Channel channel : Channel.values())
            register(channel);
    }

    /**
     * Unregister channel for plugin messaging.
     *
     * @param channel plugin channel
     */
    void unregister(final @NotNull Channel channel);

    /**
     * Unregister <b>all existing channels<b/> for plugin messaging.
     */
    default void unregister() {
        for (final @NotNull Channel channel : Channel.values())
            unregister(channel);
    }

    /**
     * Send plugin message to exact player.
     *
     * @param uuid   uuid of the player
     * @param action action that will be executed
     * @param data   data that will be put to packet
     */
    void send(final @NotNull UUID uuid, final @NotNull Action action, final String... data);

    /**
     * Execute action at the earliest opportunity.
     *
     * @param action action that will be executed
     * @param data   data, that will be put to packet
     */
    void reserve(final @NotNull Action action, final String... data);

    void reload();

    void disable();
}
