package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.config.ConfigurationFile;
import bridge.database.Connector;
import bridge.database.QueryType;
import bridge.database.Saver;
import bridge.database.UpdateType;
import bridge.utils.ColorCodes;
import lombok.CustomLog;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.team.UnlimitedNametagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CustomLog
public class NicknameColorManager {

    private static Connector con;
    private static ConfigurationFile colorConfig;
    private static long UpdateTime;
    private static final Bridge instance = Bridge.getInstance();
    private static final Saver saver = instance.getSaver();
    private static final HashMap<String, List<PlayerColor>> ramColors = new HashMap<>();
    private static final HashMap<UUID, PlayerColor> playerHex = new HashMap<>();
    private static BukkitTask task;
    private static final BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            playerHex.clear();
        }
    };

    /**
     * Creating a color-config to use for color groups.
     *
     * @param con {@link Connector} instance
     * @return true if successful
     */
    protected static boolean setup(final Connector con) {
        NicknameColorManager.con = con;
        try {
            colorConfig = ConfigurationFile.create(new File(instance.getDataFolder(), "color-config.yml"), instance, "color-config.yml");
        } catch (final InvalidConfigurationException | FileNotFoundException e) {
            LOG.warn(e.getMessage(), e);
            return false;
        }
        UpdateTime = colorConfig.getLong("settings.UpdateTime") * 1200;
        task = runnable.runTaskTimerAsynchronously(instance, UpdateTime, UpdateTime);
        return true;
    }

    /**
     * Get group of the color.
     *
     * @param color the color to fond the group from
     * @return the group if found one
     */
    public static @Nullable String getColorGroup(final String color) {
        for (String group : getGroups()) {
            for (String colors : getGroupColors(color)) {
                if (colors.equalsIgnoreCase(color)) return group;
            }
        }
        return null;
    }

    /**
     * Get hex from given color.
     *
     * @param color color for nickname. May be null. You can get this value from {@code getGroupColors} method
     * @return color or null
     */
    public static @Nullable String getColorHex(final String color) {
        for (String group : getGroups()) {
            for (String colors : getGroupColors(group)) {
                if (colors.equalsIgnoreCase(color)) return getColorHex(group, colors);
            }
        }
        return null;
    }

    /**
     * Get first to match color cost from color color
     *
     * @param color color color
     * @return cost of the color from color-config.yml
     */
    public static int getColorCost(final String color) {
        for (String group : getGroups()) {
            for (PlayerColor playerColor : ramColors.get(group)) {
                if (playerColor.color().equalsIgnoreCase(color)) {
                    return playerColor.cost();
                }
            }
        }
        return 0;
    }

    /**
     * Get first to match color cost from color hex
     *
     * @param hex hex color
     * @return cost of the color from color-config.yml
     */
    public static int getHexColorCost(final String hex) {
        if (ColorCodes.isHexValid(hex)) {
            String color = getColorNameByHex(hex);
            if(color == null) return -1;
            return getColorCost(color);
        }
        return -1;
    }

    /**
     * Get all colors names from all groups
     *
     * @return list of colors names
     */
    public static @NotNull List<String> getAllColorsName() {
        List<String> list = new ArrayList<>();
        for (String group : getGroups()) {
            list.addAll(getGroupColors(group));
        }
        return list;
    }

    /**
     * Applies hex to the {@link Player} nickname.
     * <p>
     * Requires TAB and UnlimitedNameTagMode to be enabled.
     *
     * @param p    the {@link Player }
     * @param hex  the hex color to apply
     * @param save save hex to database or not
     */
    public static void applyNicknameColor(@NotNull final Player p, final String hex, final boolean save) {
        UUID uuid = p.getUniqueId();
        TabPlayer player = TabAPI.getInstance().getPlayer(uuid);
        UnlimitedNametagManager nameTagManager =
                TabAPI.getInstance().getTeamManager() instanceof UnlimitedNametagManager
                        ? (UnlimitedNametagManager) TabAPI.getInstance().getTeamManager() : null;
        if (nameTagManager == null || !ColorCodes.isHexValid(hex)) return;
        //Applying to nickname
        nameTagManager.setName(player, ChatColor.of(hex) + p.getName() + ChatColor.RESET);
        //Applying to TAB
        TabAPI.getInstance().getTablistFormatManager().setName(player, ChatColor.of(hex) + p.getName() + ChatColor.RESET);
        //Applying to chat
        p.displayName(Component.text(p.getName()).color(TextColor.fromHexString(hex)));
        new BukkitRunnable() {
            @Override
            public void run() {
                playerHex.put(uuid, new PlayerColor(getColorNameByHex(hex), hex, getHexColorCost(hex), getPlayerStars(uuid)));
            }
        }.runTaskAsynchronously(instance);
        //save to database async
        if (save) saver.add(new Saver.Record(UpdateType.UPDATE_COLOR, hex, uuid.toString()));
    }


    /**
     * Find color color by hex.
     *
     * @param hex hex color to find in {@code color-config.yml}.
     * @return color color if exist or null if not
     */
    public static @Nullable String getColorNameByHex(final String hex) {
        for (String group : getGroups()) {
            for (String color : getGroupColors(group)) {
                String found = getColorHex(group, color);
                if (found != null && found.equalsIgnoreCase(hex)) return found;
            }
        }
        return null;
    }

    /**
     * Gets player color.
     *
     * @param uuid      Player's {@link UUID}
     * @param returnHex return hex or color name
     * @return color hex or name
     */
    public static @Nullable String getPlayerColor(@NotNull final UUID uuid, final boolean returnHex) {
        String hex = null;
        if (playerHex.containsKey(uuid)) hex = playerHex.get(uuid).hex();
        else {
            ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
            try {
                if(rs.next()) hex = rs.getString("color");
            } catch (SQLException e) {
                LOG.error("There was an exception with SQL", e);
                return null;
            }
            if(hex == null) return null;
            final String finalHex = hex;
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerHex.put(uuid,
                            new PlayerColor(
                                    getColorNameByHex(finalHex),
                                    finalHex,
                                    getHexColorCost(finalHex),
                                    getPlayerStars(uuid))
                    );
                }
            }.runTaskAsynchronously(instance);
        }

        if (returnHex) return hex;
        else return getColorNameByHex(hex);
    }

    /**
     * Replaces players hex colors in database. (async)
     *
     * @param fromColor which hex color replace
     * @param toColor   to what hex color replace
     */
    public static boolean globallyReplaceColors(String fromColor, String toColor) {
        if (ColorCodes.isHexValid(fromColor) && ColorCodes.isHexValid(toColor)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        List<Player> list = getPlayersWhoHasHexColor(fromColor);
                        for (Player p : list) applyNicknameColor(p, toColor, true);
                    } catch (SQLException e) {
                        LOG.error("There was an exception with SQL", e);
                    }
                }
            }.runTaskAsynchronously(instance);
            saver.add(new Saver.Record(UpdateType.CHANGE_ALL_COLORS, fromColor, toColor));
            return true;
        } else return false;
    }

    public static PlayerColor getPlayerInfo(final UUID uuid) {
        if (playerHex.containsKey(uuid)) return playerHex.get(uuid);
        else {
            String hex = getPlayerColor(uuid, true);
            PlayerColor answer = new PlayerColor(
                    getColorNameByHex(hex),
                    hex,
                    getHexColorCost(hex),
                    getPlayerStars(uuid)
            );
            playerHex.put(uuid, answer);
            return answer;
        }
    }

    /**
     * Get all group that was defined in {@code color-config.yml}.
     *
     * @return list go groups
     */
    protected static @NotNull List<String> getGroups() {
        if (ramColors.isEmpty()) return colorConfig.getStringList("groups");
        else return ramColors.keySet().stream().toList();
    }

    /**
     * Get all colors names from group that were defined in {@code color-config.yml}.
     *
     * @param group group that you can get from {@code getGroup} method
     * @return list of group colors
     */
    protected static @NotNull List<String> getGroupColors(@NotNull final String group) {
        if (ramColors.isEmpty()) return colorConfig.getStringList(String.format("groups.%s", group));
        if (!ramColors.containsKey(group)) return List.of();
        List<String> list = new ArrayList<>();
        for (PlayerColor color : ramColors.get(group)) {
            list.add(color.color);
        }
        return list;
    }

    /**
     * Get hex from given group and color.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color color for nickname. May be null. You can get this value from {@code getGroupColors} method
     * @return color or null
     */
    protected static @Nullable String getColorHex(final String group, final String color) {
        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return null;
            return setting.split(" ", 2)[0];
        }
        if (!ramColors.containsKey(group)) return null;
        for (PlayerColor playerColor : ramColors.get(group)) {
            if (playerColor.color().equalsIgnoreCase(color)) return playerColor.hex();
        }
        return null;
    }

    /**
     * Get cost to be able to apply color to nickname.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color color for nickname that you can get from {@code getGroupColors} method
     * @return cost amount
     */
    protected static int getColorCost(final String group, final String color) {
        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return -1;
            return Integer.parseInt(setting.split(" ", 2)[1], 10);
        }
        if (ramColors.containsKey(group)) return -1;
        for (PlayerColor playerColor : ramColors.get(group)) {
            if (playerColor.color().equalsIgnoreCase(color)) return playerColor.cost();
        }
        return -1;
    }

    /**
     * Get default hex color.
     *
     * @return hex color (example: #FFFFFF)
     */
    protected static String getDefaultColor() {
        return colorConfig.getString("default", "#CFCFCF");
    }

    protected static void reload() {
        ramColors.clear();
        playerHex.clear();
        task.cancel();
        UpdateTime = colorConfig.getLong("settings.UpdateTime") * 1200;
        if(task.isCancelled()) task = runnable.runTaskTimerAsynchronously(instance, UpdateTime, UpdateTime);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.isConfigSet()) {
                    List<String> disabledGroups = colorConfig.getStringList("settings.disabledGroups");
                    for (String group : getGroups()) {
                        if(disabledGroups.contains(group)) continue;
                        List<String> groupColors = getGroupColors(group);
                        List<PlayerColor> list = new ArrayList<>();
                        for (int i = 0; i <= groupColors.size(); i++) {
                            list.add(new PlayerColor(
                                    groupColors.get(i),
                                    getColorHex(group, groupColors.get(i)),
                                    getColorCost(group, groupColors.get(i)),
                                    0
                            ));
                        }
                        ramColors.put(group, list);
                    }
                }
            }
        }.runTaskAsynchronously(instance);
    }

    private static int getPlayerStars(@NotNull final UUID uuid) {
        if (playerHex.containsKey(uuid)) return playerHex.get(uuid).stars();
        ResultSet rs = con.querySQL(QueryType.SELECT_STARS, uuid.toString());
        try {
            return rs.getInt("stars");
        } catch (SQLException e) {
            return -1;
        }
    }

    private static @Unmodifiable List<Player> getPlayersWhoHasHexColor(String color) throws SQLException {
        if (!ColorCodes.isHexValid(color)) return List.of();
        List<String> uuids = new ArrayList<>();
        ResultSet rs = con.querySQL(QueryType.SELECT_UUID_BY_COLOR, color);
        while (rs.next()) uuids.add(rs.getString("playerID"));

        List<Player> has = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers())
            if (uuids.contains(p.getUniqueId().toString())) has.add(p);
        return has;
    }

    public record PlayerColor(String color, String hex, int cost, int stars) {
        public PlayerColor(final String color, final String hex, final int cost, final int stars) {
            this.color = color;
            this.hex = ColorCodes.isHexValid(hex) ? hex : null;
            this.cost = cost < 0 ? cost * (-1) : cost;
            this.stars = Math.max(stars, 0);
        }
    }
}
