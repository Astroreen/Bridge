package bridge.database;

import java.util.function.Function;

/**
 * Type of the query.
 */
public enum QueryType {
    /**
     * Select player nickname color. PlayerID
     */
    SELECT_COLOR(prefix -> "SELECT color FROM " + prefix + "nickname WHERE playerID = ?;"),
    /**
     * Select uuids where nickname color is (YOUR COLOR). Hex color
     */
    SELECT_UUID_BY_COLOR(prefix -> "SELECT playerID FROM " + prefix + "nickname WHERE color = ?;"),
    /**
     * Select player stars. PlayerID
     */
    SELECT_STARS(prefix -> "SELECT stars FROM " + prefix + "nickname WHERE playerID = ?;"),

    /**
     * Select all from nickname table.
     */
    LOAD_ALL_NICKNAME_COLORS(prefix -> "SELECT playerID,color FROM " + prefix + "nickname"),
    ;

    /**
     * Function to create the SQL code from a prefix.
     */
    private final Function<String, String> statementCreator;

    QueryType(final Function<String, String> sqlTemplate) {
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
