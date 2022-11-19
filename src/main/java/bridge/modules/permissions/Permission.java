package bridge.modules.permissions;

public enum Permission {

    /// /// COMMANDS /// ///
    COMMAND_RELOAD("bridge.command.reload", false),
    COMMAND_DEBUG("bridge.command.debug", false),
    COMMAND_LANGUAGE("bridge.command.language", false),
    COMMAND_NICKNAME("bridge.command.nickname", true),

    /// /// NICKNAME /// ///
        ///  COLOR   ///
    NICKNAME_COLOR_HAVE_OWN("bridge.nickname.color.have", true),
    NICKNAME_COLOR_HAVE_OTHER("bridge.nickname.color.have.other", false),
    NICKNAME_COLOR_COST_OWN("bridge.nickname.color.cost", false),
    NICKNAME_COLOR_COST_OTHER("bridge.nickname.color.cost.other", false),
    NICKNAME_COLOR_SET_OWN("bridge.nickname.color.set", true),
    NICKNAME_COLOR_SET_HEX("bridge.nickname.color.set.hex", false),
    NICKNAME_COLOR_SET_OTHER("bridge.nickname.color.set.other", false),
    NICKNAME_COLOR_REPLACE("bridge.nickname.color.replace", false),
        ///  STARS   ///
    NICKNAME_STARS_HAVE_OWN("bridge.nickname.stars.have", false),
    NICKNAME_STARS_HAVE_OTHER("bridge.nickname.stars.have.other", false),
    NICKNAME_STARS_SET_OWN("bridge.nickname.stars.set", false),
    NICKNAME_STARS_SET_OTHER("bridge.nickname.stars.set.other",false),
    NICKNAME_STARS_ADD_OWN("bridge.nickname.stars.add",false),
    NICKNAME_STARS_ADD_OTHER("bridge.nickname.stars.add.other",false)
    ;


    public final String perm;
    public final boolean active;
    Permission(final String perm, final boolean active) {
        this.perm = perm;
        this.active = active;
    }
}
