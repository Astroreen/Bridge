package common.database;

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
     * Select player stars. playerID
     */
    SELECT_STARS(prefix -> "SELECT stars FROM " + prefix + "nickname WHERE playerID = ?;"),

    /**
     * Select all uuids from nickname table.
     */
    LOAD_ALL_NICKNAME_UUIDS(prefix -> "SELECT playerID FROM " + prefix + "nickname"),
    /**
     * Select playerID and color from nickname table.
     */
    LOAD_ALL_NICKNAME_COLORS(prefix -> "SELECT playerID,color FROM " + prefix + "nickname"),

    /**
     * Select kill and death count. playerID
     */
    SELECT_FFA_DATA(prefix -> "SELECT deaths,kills FROM " + prefix + "ffa WHERE playerID = ?;"),

    LOAD_ALL_FFA_UUIDS(prefix -> "SELECT playerID FROM " + prefix + "ffa"),
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
