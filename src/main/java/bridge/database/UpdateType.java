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
