package bridge;

/**
 * Type of messages
 */
public enum MessageType {
    PREFIX("prefix"),
    NO_PERMISSION("no_permission"),
    NEED_TO_BE_PLAYER("need_to_be_player"),
    RELOADED("reloaded"),
    /**
     * Variables: Current language
     */
    CURRENT_LANGUAGE("language.current_language"),
    /**
     * Variables: Current language
     */
    ALREADY_LANGUAGE("language.already_language"),
    /**
     * Variables: Typed language
     */
    NO_SUCH_LANGUAGE("language.no_such_language"),
    /**
     * Variables: New language
     */
    SET_LANGUAGE_SUCCESSFULLY("language.set_language_successfully"),
    /**
     * Variables: Current debug mode
     */
    DEBUGGING("debug.current_debug"),
    /**
     * Variables: Is debugging
     */
    ALREADY_DEBUGGING("debug.already_debugging"),
    /**
     * Variables: Enabled/Disabled
     */
    SET_DEBUG_SUCCESSFULLY("debug.set_debug_successfully"),
    SET_DEBUG_ERROR("debug.set_debug_error"),
    /**
     * Variables: Current nickname color color, Current nickname hex color
     */
    YOUR_CURRENT_NICKNAME_COLOR("hooks.tab.colors.your_color"),
    /**
     * Variables: Checked player, Current nickname color color, Current nickname hex color
     */
    CURRENT_NICKNAME_COLOR("hooks.tab.colors.current_player_color"),
    /**
     * Variables: New nickname color
     */
    YOUR_NICKNAME_COLOR_CHANGED("hooks.tab.colors.your_color_changed"),
    /**
     * Variables: Player, New nickname color
     */
    SET_NICKNAME_COLOR_SUCCESSFULLY("hooks.tab.colors.set_color_successfully"),
    /**
     * Variables: How much money till can buy
     */
    NOT_ENOUGH_STARS("hooks.tab.colors.not_enough_money"),
    VERSION("version"),
    /**
     * Variables: Arguments
     */
    UNKNOWN_ARGUMENT("variables.unknown_argument"),
    ENABLED("variables.enabled"),
    DISABLED("variables.disabled");

    public final String path;

    MessageType(String path) {
        this.path = path;
    }
}
