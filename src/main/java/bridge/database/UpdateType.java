package bridge.database;

import java.util.function.Function;

/**
 * Type of the update
 */
public enum UpdateType {

    /**
     * Adds player to nickname table. PlayerID, color.
     */
    ADD_NICKNAME(prefix -> "INSERT INTO " + prefix + "nickname (playerID, color, stars) VALUES (?, ?, 0)"),

    /**
     * Updates nickname color. Color, playerID.
     */
    UPDATE_COLOR(prefix -> "UPDATE " + prefix + "nickname SET color = ? WHERE playerID = ?"),
    /**
     * Updates nickname color. Stars, playerID.
     */
    UPDATE_STARS(prefix -> "UPDATE " + prefix + "nickname SET stars = ? WHERE playerID = ?"),

    /**
     * Globally change nickname color.
     * From <b>color</b>,to <b>color</b>.
     */
    CHANGE_ALL_COLORS(prefix -> "UPDATE " + prefix + "nickname SET color = ? WHERE color = ?"),

    /**
     * Globally deletes all duplicates,
     * leaving only those, who have more stars.
     */
    DELETE_NICKNAME_DUPLICATES(prefix -> "DELETE t1 FROM " + prefix + "nickname t1 INNER JOIN " + prefix + "nickname t2 WHERE ( t1.id < t2.id AND t1.stars = t2.stars AND t1.playerID = t2.playerID ) OR (t1.stars < t2.stars AND t1.playerID = t2.playerID)"),

    /**
     * Insert new instance if ffa player. playerID.
     */
    ADD_FFA(prefix -> "INSERT INTO " + prefix + "ffa (playerID, kills, deaths) VALUES (?, 0, 0)"),
    /**
     * Increments player's kills. playerID.
     */
    INCREMENT_FFA_KILLS(prefix -> "UPDATE " + prefix + "ffa SET kills = kills + 1 WHERE playerID = ?"),

    /**
     * Increments player's deaths. playerID.
     */
    INCREMENT_FFA_DEATHS(prefix -> "UPDATE " + prefix + "ffa SET deaths = deaths + 1 WHERE playerID = ?"),

    /**
     * Set player's deaths. Deaths amount, playerID.
     */
    SET_FFA_DEATHS(prefix -> "UPDATE " + prefix + "ffa SET deaths = ? WHERE playerID = ?"),

    /**
     * Set player's kills. Kills amount, playerID.
     */
    SET_FFA_KILLS(prefix -> "UPDATE " + prefix + "ffa SET kills = ? WHERE playerID = ?"),

    /**
     * Add player's deaths. Add amount, playerID.
     */
    ADD_FFA_DEATHS(prefix -> "UPDATE " + prefix + "ffa SET deaths = deaths + ? WHERE playerID = ?"),

    /**
     * Add player's kills. Add amount, playerID.
     */
    ADD_FFA_KILLS(prefix -> "UPDATE " + prefix + "ffa SET kills = kills + ? WHERE playerID = ?"),
    ;


    /**
     * Function to create the SQL code from a prefix.
     */
    private final Function<String, String> statementCreator;

    UpdateType(final Function<String, String> sqlTemplate) {
        this.statementCreator = sqlTemplate;
    }

    /**
     * Create the SQL code for the given table prefix.
     *
     * @param tablePrefix table prefix to use
     * @return SQL-code for the update
     */
    public String createSql(final String tablePrefix) {
        return statementCreator.apply(tablePrefix);
    }
}
