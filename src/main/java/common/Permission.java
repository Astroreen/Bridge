package common;

public interface Permission {

    String
    /// /// COMMANDS /// ///
    COMMAND_RELOAD = "bridge.command.reload",
    COMMAND_DEBUG = "bridge.command.debug",
    COMMAND_LANGUAGE = "bridge.command.language",
    COMMAND_NICKNAME = "bridge.command.nickname",
    COMMAND_FFA = "bridge.command.ffa",
    COMMAND_GUILD = "bridge.command.guild",

    /// /// NICKNAME /// ///
        ///  COLOR   ///
    NICKNAME_COLOR_HAVE_OWN = "bridge.nickname.color.have",
    NICKNAME_COLOR_HAVE_OTHER = "bridge.nickname.color.have.other",
    NICKNAME_COLOR_COST_OWN = "bridge.nickname.color.cost",
    NICKNAME_COLOR_COST_OTHER = "bridge.nickname.color.cost.other",
    NICKNAME_COLOR_SET_OWN = "bridge.nickname.color.set",
    NICKNAME_COLOR_SET_HEX = "bridge.nickname.color.set.hex",
    NICKNAME_COLOR_SET_OTHER = "bridge.nickname.color.set.other",
    NICKNAME_COLOR_REPLACE = "bridge.nickname.color.replace",
        ///  STARS   ///
    NICKNAME_STARS_HAVE_OWN = "bridge.nickname.stars.have",
    NICKNAME_STARS_HAVE_OTHER = "bridge.nickname.stars.have.other",
    NICKNAME_STARS_SET_OWN = "bridge.nickname.stars.set",
    NICKNAME_STARS_SET_OTHER = "bridge.nickname.stars.set.other",
    NICKNAME_STARS_ADD_OWN = "bridge.nickname.stars.add",
    NICKNAME_STARS_ADD_OTHER = "bridge.nickname.stars.add.other",
    /// /// FFA /// ///
       /// ARENA ///
    FFA_ARENA_LOAD = "bridge.ffa.arena.load",
    FFA_ARENA_UNLOAD = "bridge.ffa.arena.unload",
    FFA_ARENA_TELEPORT = "bridge.ffa.arena.teleport",
        /// KIT ///
    FFA_KIT_CREATE = "bridge.ffa.kit.create",
    /// DEATHS/KILLS ///
    FFA_MANAGE_INFO = "bridge.ffa.manage.info",

    /// HEADSHOT ///
    HEADSHOT_MAKE_HEADSHOT = "bridge.headshot"
    ;
}
