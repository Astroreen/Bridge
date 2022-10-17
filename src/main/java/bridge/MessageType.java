package bridge;

/**
 * Type of messages
 */
public enum MessageType {
    PREFIX("prefix"),
    NO_PERMISSION("no_permission"),
    NEED_TO_BE_PLAYER("need_to_be_player"),
    RELOADED("reloaded"),
    VERSION("version"),
    /**
     * Variables: Disabled plugin
     */
    PLUGIN_DISABLED("plugin_is_disabled"),
    /**
     * Variables: Module name, "ENABLED" or "DISABLED" variable
     */
    MODULE_STATE("module_is"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
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
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
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
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: Current nickname color name
     */
    YOUR_NICKNAME_COLOR("hooks.tab.colors.your_color"),
    /**
     * Variables: New nickname color
     */
    YOUR_NICKNAME_COLOR_CHANGED("hooks.tab.colors.your_color_changed"),
    /**
     * Variables: Current nickname color cost
     */
    YOUR_NICKNAME_COLOR_COST("hooks.tab.colors.your_color_cost"),
    /**
     * Variables: None
     */
    YOUR_NICKNAME_IS_UNIQUE("hooks.tab.colors.your_color_is_unique"),
    /**
     * Variables: Checked player, Current nickname color name
     */
    OTHER_PLAYER_NICKNAME_COLOR("hooks.tab.colors.other_player_color"),
    /**
     * Variables: Player, His new nickname color
     */
    OTHER_PLAYER_NICKNAME_COLOR_CHANGED("hooks.tab.colors.other_player_color_changed"),
    /**
     * Variables: Player, His nickname color cost
     */
    OTHER_PLAYER_NICKNAME_COLOR_COST("hooks.tab.colors.your_color_cost"),
    /**
     * Variables: Player whose color checking
     */
    OTHER_PLAYER_NICKNAME_IS_UNIQUE("hooks.tab.colors.other_player_color_is_unique"),
    /**
     * Variables: Player's amount of stars
     */
    YOUR_NICKNAME_STARS("hooks.tab.colors.your_money"),
    /**
     * Variables: New player's amount of stars
     */
    YOUR_NICKNAME_STARS_CHANGED("hooks.tab.colors.your_money_changed"),
    /**
     * Variables: Player name, Player's amount of stars
     */
    OTHER_PLAYER_NICKNAME_STARS("hooks.tab.colors.other_player_money"),
    /**
     * Variables: Amount what you set, "Self" variable or Player's name
     */
    OTHER_PLAYER_NICKNAME_STARS_CHANGED("hooks.tab.colors.other_player_money_changed"),
    /**
     * Variables: From what Color, To what Color
     */
    REPLACE_COLORS_SUCCESSFULLY("hooks.tab.colors.replace_color_successfully"),
    /**
     * Variables: From what Color, To what Color
     */
    REPLACE_COLORS_ERROR("hooks.tab.colors.replace_color_error"),
    /**
     * Variables: How much money till can buy
     */
    NOT_ENOUGH_STARS("hooks.tab.colors.not_enough_money"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: Arguments
     */
    UNKNOWN_ARGUMENT("unknown_argument"),
    SELF("variables.self"),
    ENABLED("variables.enabled"),
    DISABLED("variables.disabled");

    public final String path;

    MessageType(String path) {
        this.path = path;
    }
}
