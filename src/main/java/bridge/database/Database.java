package bridge.database;

import bridge.Bridge;
import lombok.CustomLog;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 */
@CustomLog
public abstract class Database {

    protected Plugin plugin;
    protected String prefix;
    protected Connection con;

    protected Database(final @NotNull Bridge plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getPluginConfig().getString("mysql.prefix", "");
    }

    public Connection getConnection() {
        try {
            //3 seconds to validate connection
            if (con == null || con.isClosed() || !con.isValid(3)) con = openConnection();
        } catch (SQLException e){
            LOG.error("There was an exception with SQL", e);
        }

        return con;
    }

    protected abstract Connection openConnection();

    public void closeConnection() {
        try {
            con.close();
        } catch (final SQLException e) {
            LOG.error("There was an exception with SQL", e);
        }
    }

    /**
     * Modify existing table and add new column to it.
     *
     * @param tableName      color of table without prefix from config
     * @param columnName     color of new column
     * @param columnDataType data type of new column (e.g. VARCHAR(30), INT, TEXT)
     */
    public abstract void createColumn(final String tableName, final String columnName, final String columnDataType);

    public void createTables(final boolean isMySQLUsed) {
        final String autoIncrement = isMySQLUsed ? "AUTO_INCREMENT" : "AUTOINCREMENT";

        try {
            getConnection().createStatement()
                    .executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "nickname (id INTEGER PRIMARY KEY "
                            + autoIncrement + ", playerID VARCHAR(256) NOT NULL, color VARCHAR(15) "
                            + "NOT NULL, stars INT NOT NULL);");
        } catch (final SQLException e) {
            LOG.error("There was an exception with SQL", e);
        }
    }
}
