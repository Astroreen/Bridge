package bridge;

/**
 * Type of messages
 */
public enum MessageType {
    PREFIX("prefix"),
    NO_PERMISSION("no-permission"),
    NEED_TO_BE_PLAYER("need-to-be-player"),
    RELOADED("reloaded"),
    VERSION("version"),
    /**
     * Variables: Disabled plugin
     */
    PLUGIN_DISABLED("plugin-is-disabled"),
    /**
     * Variables: Module name, "ENABLED" or "DISABLED" variable
     */
    MODULE_STATE("module-is"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: Current language
     */
    CURRENT_LANGUAGE("language.current-language"),
    /**
     * Variables: Current language
     */
    ALREADY_LANGUAGE("language.already-language"),
    /**
     * Variables: Typed language
     */
    NO_SUCH_LANGUAGE("language.no-such-language"),
    /**
     * Variables: New language
     */
    SET_LANGUAGE_SUCCESSFULLY("language.set-language-successfully"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: Current debug mode
     */
    DEBUGGING("debug.current-debug"),
    /**
     * Variables: Is debugging
     */
    ALREADY_DEBUGGING("debug.already-debugging"),
    /**
     * Variables: Enabled/Disabled
     */
    SET_DEBUG_SUCCESSFULLY("debug.set-debug-successfully"),
    SET_DEBUG_ERROR("debug.set-debug-error"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: Current nickname color name
     */
    YOUR_NICKNAME_COLOR("module.tab.colors.your-color"),
    /**
     * Variables: New nickname color
     */
    YOUR_NICKNAME_COLOR_CHANGED("module.tab.colors.your-color-changed"),
    /**
     * Variables: Current nickname color cost
     */
    YOUR_NICKNAME_COLOR_COST("module.tab.colors.your-color-cost"),
    /**
     * Variables: None
     */
    YOUR_NICKNAME_IS_UNIQUE("module.tab.colors.your-color-is-unique"),
    /**
     * Variables: Checked player, Current nickname color name
     */
    OTHER_PLAYER_NICKNAME_COLOR("module.tab.colors.other-player-color"),
    /**
     * Variables: Player, His new nickname color
     */
    OTHER_PLAYER_NICKNAME_COLOR_CHANGED("module.tab.colors.other-player-color-changed"),
    /**
     * Variables: Player, His nickname color cost
     */
    OTHER_PLAYER_NICKNAME_COLOR_COST("module.tab.colors.other-player-color-cost"),
    /**
     * Variables: Player whose color checking
     */
    OTHER_PLAYER_NICKNAME_IS_UNIQUE("module.tab.colors.other-player-color-is-unique"),
    /**
     * Variables: Player's amount of stars
     */
    YOUR_NICKNAME_STARS("module.tab.colors.your-money"),
    /**
     * Variables: New player's amount of stars
     */
    YOUR_NICKNAME_STARS_CHANGED("module.tab.colors.your-money-changed"),
    /**
     * Variables: Player name, Player's amount of stars
     */
    OTHER_PLAYER_NICKNAME_STARS("module.tab.colors.other-player-money"),
    /**
     * Variables: Amount what you set, "Self" variable or Player's name
     */
    OTHER_PLAYER_NICKNAME_STARS_CHANGED("module.tab.colors.other-player-money-changed"),
    /**
     * Variables: From what Color, To what Color
     */
    REPLACE_COLORS_SUCCESSFULLY("module.tab.colors.replace-color-successfully"),
    /**
     * Variables: From what Color, To what Color
     */
    REPLACE_COLORS_ERROR("module.tab.colors.replace-color-error"),
    /**
     * Variables: How much money till can buy
     */
    NOT_ENOUGH_STARS("module.tab.colors.not-enough-money"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: arena name
     */
    ARENA_CREATED_SUCCESSFULLY("module.arena.created-successfully"),
    /**
     * Variables: arena name
     */
    ARENA_CREATED_ERROR("module.arena.created-error"),
    /**
     * Variables: arena name
     */
    ARENA_LOADED_SUCCESSFULLY("module.arena.loaded-successfully"),
    /**
     * Variables: arena name
     */
    ARENA_LOADED_ERROR("module.arena.loaded-error"),
    /**
     * Variables: arena name
     */
    ARENA_UNLOADED_SUCCESSFULLY("module.arena.unloaded-successfully"),
    /**
     * Variables: arena name
     */
    ARENA_UNLOADED_ERROR("module.arena.unloaded-error"),
    /**
     * Variables: schematic name, arena name
     */
    SCHEMATIC_LOAD_ERROR("module.arena.schematic-load-error"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: kit name
     */
    KIT_NOT_CREATED("module.kit.not-created"),
    /**
     * Variables:
     */
    NO_ITEM_IN_HAND("module.kit.no-item-in-hand"),
    /**
     * Variables: item name
     */
    FFA_KIT_CREATED_SUCCESSFULLY("module.kit.created-successfully"),
    /**
     * Variables: item name
     */
    FFA_KIT_CREATED_ERROR("module.kit.created-error"),
    /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///
    /**
     * Variables: Arguments
     */
    UNKNOWN_ARGUMENT("unknown-argument"),
    SELF("variables.self"),
    ENABLED("variables.enabled"),
    DISABLED("variables.disabled");

    public final String path;

    MessageType(String path) {
        this.path = path;
    }
}
