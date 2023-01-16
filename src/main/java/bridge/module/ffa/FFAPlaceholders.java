package bridge.module.ffa;

import bridge.Bridge;
import common.database.Connector;
import common.database.QueryType;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class FFAPlaceholders extends PlaceholderExpansion {

    private static final Bridge plugin = Bridge.getInstance();
    private static final HashMap<UUID, Data> memory = new HashMap<>();
    private static final Runnable runnable = memory::clear;
    private static int task = -1;
    private static Connector con = new Connector();

    public static void reload() {
        con = new Connector();
        memory.clear();
        //clear every 20 minutes
        if(task != -1) Bukkit.getScheduler().cancelTask(task);
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable,20*20*60,20*20*60).getTaskId();
    }

    /**
     * Persist through reloads
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist() {
        return true;
    }

    /**
     * We can always register
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The identifier for PlaceHolderAPI to link to this expansion
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier() {
        return "server/ffa";
    }

    /**
     * Name of person who created the expansion
     *
     * @return The name of the author as a String.
     */
    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * Version of the expansion
     *
     * @return The version as a String.
     */
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * A placeholder request has occurred and needs a value
     *
     * @param p      A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param params A Placeholder.
     * @return possibly-null String of the requested params.
     */
    @Override
    public String onRequest(final OfflinePlayer p, final @NotNull String params) {
        switch (params) {
            case "deaths" -> {
                final UUID uuid = p.getUniqueId();
                if (memory.containsKey(uuid)) return String.valueOf(memory.get(uuid).deaths());
                final int deaths;
                final int kills;
                try (final ResultSet rs = con.querySQL(QueryType.SELECT_FFA_DATA, uuid.toString())){
                    rs.next();
                    deaths = rs.getInt("deaths");
                    kills = rs.getInt("kills");
                } catch (SQLException ignore) {return "";}
                final Data data = new Data(deaths, kills);
                memory.put(uuid, data);
                return String.valueOf(data.deaths());
            }
            case "kills" -> {
                final UUID uuid = p.getUniqueId();
                if (memory.containsKey(uuid)) return String.valueOf(memory.get(uuid).kills());
                final int deaths;
                final int kills;
                try (final ResultSet rs = con.querySQL(QueryType.SELECT_FFA_DATA, uuid.toString())){
                    rs.next();
                    deaths = rs.getInt("deaths");
                    kills = rs.getInt("kills");
                } catch (SQLException ignore) {return "";}
                final Data data = new Data(deaths, kills);
                memory.put(uuid, data);
                return String.valueOf(data.kills());
            }
            case "kd" -> {
                final UUID uuid = p.getUniqueId();
                final Data data;
                if (memory.containsKey(uuid)) {
                     data = memory.get(p.getUniqueId());
                } else {
                    final int deaths;
                    final int kills;
                    try (final ResultSet rs = con.querySQL(QueryType.SELECT_FFA_DATA, uuid.toString())){
                        rs.next();
                        deaths = rs.getInt("deaths");
                        kills = rs.getInt("kills");
                    } catch (SQLException ignore) {return "";}
                    data = new Data(deaths, kills);
                    memory.put(uuid, data);
                }
                //start from 50 deaths
                if(data.deaths >= 50) return new DecimalFormat("##.##").format(data.kills()/data.deaths());
                else return "0";
            }
        }
        return "";
    }

    public static void addKill(UUID uuid) {
        if(!memory.containsKey(uuid)) return;
        final Data data = memory.get(uuid);
        memory.replace(uuid, data, new Data(data.deaths(), data.kills() + 1));
    }

    public static void addDeath(UUID uuid) {
        if(!memory.containsKey(uuid)) return;
        final Data data = memory.get(uuid);
        memory.replace(uuid, data, new Data(data.deaths() + 1, data.kills()));
    }

    record Data(int deaths, int kills){
    }
}