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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@CustomLog
public class NicknameColorManager {

    private Connector con;
    private ConfigurationFile colorConfig;
    private String defaultColor;
    private int taskID = -1;
    private List<String> disabledGroups;
    private boolean whitelist;
    private static Bridge instance;
    private static Saver saver;
    private final HashMap<String, List<PlayerColor>> ramColors;
    private final HashMap<UUID, PlayerColor> playerHex;
    private final Runnable runnable;

    //TODO fix bug with checking color name from hex, using 1 option of database suggestion below
    //TODO check if hex has name by making table in database for it
    //like:
    //color     stars   name->name/custom
    //#cfcfcf   0       gray->none/true
    protected NicknameColorManager (Bridge plugin) {
        instance = plugin;
        saver = instance.getSaver();
        con = new Connector();
        ramColors = new HashMap<>();
        playerHex = new HashMap<>();
        runnable = playerHex::clear;
        try {
            colorConfig = ConfigurationFile.create(new File(instance.getDataFolder(), "color-config.yml"), instance, "color-config.yml");
        } catch (final InvalidConfigurationException | FileNotFoundException e) {
            LOG.warn(e.getMessage(), e);
        }
        whitelist = colorConfig.getBoolean("settings.WhitelistMode", false);
        reload();
    }

    /**
     * Get group of the color.
     *
     * @param color the color to fond the group from
     * @return the group if found one
     */
    public @Nullable String getColorGroup(final String color) {
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
    public @Nullable String getColorHex(final String color) {
        for (String group : getGroups()) {
            for (String colors : getGroupColors(group)) {
                if (colors.equalsIgnoreCase(color)) return getColorHex(group, colors);
            }
        }
        return null;
    }

    /**
     * Get first to match color cost from color name
     *
     * @param color the color
     * @return cost of the color from color-config.yml
     */
    public int getColorCost(final String color) {
        for (String group : getGroups()) {
            for (String colors : getGroupColors(group)) {
                if (colors.equalsIgnoreCase(color)) return getColorCost(group, color);
            }
        }
        return -1;
    }

    /**
     * Get cost to be able to apply color to nickname.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color color for nickname that you can get from {@code getGroupColors} method
     * @return cost amount; -1 if something went wrong or group doesn't exist
     */
    public int getColorCost(final String group, final String color) {
        if(whitelist && !disabledGroups.contains(group)) return -1;
        else if(!whitelist && disabledGroups.contains(group)) return -1;
        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return -1;
            return Integer.parseInt(setting.split(":")[1], 10);
        }
        if (!ramColors.containsKey(group)) return -1;
        for (PlayerColor playerColor : ramColors.get(group)) {
            if (playerColor.color().equalsIgnoreCase(color)) return playerColor.cost();
        }
        return -1;
    }

    /**
     * Get first to match color cost from color hex
     *
     * @param hex hex color
     * @return cost of the color from color-config.yml
     */
    public int getHexColorCost(final String hex) {
        if (ColorCodes.isHexValid(hex)) {
            String color = getColorNameByHex(hex);
            if (color == null) return -1;
            return getColorCost(color);
        }
        return -1;
    }

    /**
     * Get all colors names from all groups
     *
     * @return list of colors names
     */
    public @NotNull List<String> getAllColorsName() {
        List<String> list = new ArrayList<>();
        for (String group : getGroups()) {
            list.addAll(getGroupColors(group));
        }
        return list;
    }

    /**
     * Applies hex to the {@link TabPlayer} nickname.
     * <p>
     * Requires TAB and UnlimitedNameTagMode to be enabled.
     *
     * @param p    the {@link Player }
     * @param hex  the hex color to apply
     * @param save save hex to database or not
     */
    public void applyNicknameColor(@NotNull final Player p, final String hex, final boolean save) {
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
                final String color = getColorNameByHex(hex);
                final int cost = getHexColorCost(hex);
                final int stars = getPlayerStars(uuid);
                if (color == null
                        || cost == -1
                        || stars == -1) return;
                playerHex.put(uuid, new PlayerColor(color, hex, cost, stars));
            }
        }.runTaskAsynchronously(instance);
        //save to database async
        if (save) saver.add(new Saver.Record(UpdateType.UPDATE_COLOR, hex, uuid.toString()));
    }


    /**
     * Find color name by hex.
     *
     * @param hex hex color to find in {@code color-config.yml}.
     * @return color name if exist or null if not
     */
    public @Nullable String getColorNameByHex(final String hex) {
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
    public @Nullable String getPlayerColor(@NotNull final UUID uuid, final boolean returnHex) {
        String hex = null;
        if (playerHex.containsKey(uuid)) hex = playerHex.get(uuid).hex();
        else {
            ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
            try {
                if (rs.next()) hex = rs.getString("color");
            } catch (SQLException e) {
                LOG.error("There was an exception with SQL", e);
                return null;
            }
            if (hex == null) return null;
            if (!returnHex) {
                final String finalHex = hex;
                final String color = getColorNameByHex(hex);
                if (color != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            final int cost = getHexColorCost(finalHex);
                            final int stars = getPlayerStars(uuid);
                            if (cost == -1 || stars == -1) return;
                            playerHex.put(uuid,
                                    new PlayerColor(color, finalHex, cost, stars)
                            );
                        }
                    }.runTaskAsynchronously(instance);
                }
                return color;

            }
        }
        return hex;
    }

    /**
     * Replaces players hex colors in database. (async)
     *
     * @param fromColor which hex color replace
     * @param toColor   to what hex color replace
     */
    public boolean globallyReplaceColors(String fromColor, String toColor) {
        if (!ColorCodes.isHexValid(fromColor) || !ColorCodes.isHexValid(toColor)) return false;
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
    }

    public @Nullable PlayerColor getPlayerInfo(final UUID uuid) {
        if (playerHex.containsKey(uuid)) return playerHex.get(uuid);
        else {
            final String hex = getPlayerColor(uuid, true);
            final String color = getColorNameByHex(hex);
            final int cost = getHexColorCost(hex);
            final int stars = getPlayerStars(uuid);
            if (hex == null || color == null || cost == -1 || stars == -1) return null;
            PlayerColor answer = new PlayerColor(color, hex, cost, stars);
            playerHex.put(uuid, answer);
            return answer;
        }
    }

    /**
     * Get all group that was defined in {@code color-config.yml}.
     *
     * @return list go groups
     */
    protected @NotNull List<String> getGroups() {
        if (ramColors.isEmpty()) {
            ConfigurationSection groups = colorConfig.getConfigurationSection("groups");
            if (groups != null) {
                List<String> arr = new ArrayList<>();
                for (String group : groups.getKeys(false)) {
                    if(whitelist && !disabledGroups.contains(group)) continue;
                    else if(!whitelist && disabledGroups.contains(group)) continue;
                    arr.add(group);
                }
                return arr;
            } else return List.of();
        } else return ramColors.keySet().stream().toList();
    }

    /**
     * Get all colors names from group that were defined in {@code color-config.yml}.
     *
     * @param group group that you can get from {@code getGroup} method
     * @return empty if not exist or list of group colors
     */
    protected @NotNull List<String> getGroupColors(@NotNull final String group) {
        if(whitelist && !disabledGroups.contains(group)) return List.of();
        else if(!whitelist && disabledGroups.contains(group)) return List.of();

        if (ramColors.isEmpty()) {
            ConfigurationSection colors =
                    colorConfig.getConfigurationSection(String.format("groups.%s", group));
            if (colors != null) {
                return colors.getKeys(false).stream().toList();
            } else return List.of();
        }
        List<String> list = new ArrayList<>();
        for (PlayerColor color : ramColors.get(group)) {
            if (color != null) list.add(color.color());
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
    protected @Nullable String getColorHex(final String group, final String color) {
        if(whitelist && !disabledGroups.contains(group)) return null;
        else if(!whitelist && disabledGroups.contains(group)) return null;

        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return null;
            return setting.split(":", 2)[0];
        }
        for (PlayerColor playerColor : ramColors.get(group)) {
            if (playerColor.color().equalsIgnoreCase(color)) return playerColor.hex();
        }
        return null;
    }

    private int getPlayerStars(@NotNull final UUID uuid) {
        if (playerHex.containsKey(uuid)) return playerHex.get(uuid).stars();
        ResultSet rs = con.querySQL(QueryType.SELECT_STARS, uuid.toString());
        try {
            return rs.getInt("stars");
        } catch (SQLException e) {
            return -1;
        }
    }

    private @Unmodifiable List<Player> getPlayersWhoHasHexColor(String color) throws SQLException {
        if (!ColorCodes.isHexValid(color)) return List.of();
        List<String> uuids = new ArrayList<>();
        ResultSet rs = con.querySQL(QueryType.SELECT_UUID_BY_COLOR, color);
        while (rs.next()) uuids.add(rs.getString("playerID"));

        List<Player> has = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers())
            if (uuids.contains(p.getUniqueId().toString())) has.add(p);
        return has;
    }

    /**
     * @return true if successfully reload
     */
    public boolean reload () {
        con = new Connector();
        try {
            colorConfig.reload();
            final String color = colorConfig.getString("default", "#CFCFCF");
            if (ColorCodes.isHexValid(color)) {
                defaultColor = color;
                LOG.debug("Default color now is: " + color);
            } else {
                defaultColor = "#CFCFCF";
                LOG.error("Wrong default color in color-config. Using default #CFCFCF");
            }
        } catch (IOException e) {
            LOG.error("Could not load the config.yml file!", e);
            return false;
        }

        ramColors.clear();
        playerHex.clear();

        if (taskID != -1) {
            LOG.debug("Canceling repeating task..");
            Bukkit.getScheduler().cancelTask(taskID);
        }

        long updateTime = colorConfig.getLong("settings.UpdateTime") * 1200;
        disabledGroups = colorConfig.getStringList("settings.disabledGroups");
        whitelist = colorConfig.getBoolean("settings.WhitelistMode", false);
        LOG.debug("Creating new repeating task");
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, runnable, updateTime, updateTime).getTaskId();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (String group : getGroups()) {
                    if(whitelist && !disabledGroups.contains(group)) continue;
                    else if (!whitelist && disabledGroups.contains(group)) continue;
                    List<String> groupColors = getGroupColors(group);
                    List<PlayerColor> list = new ArrayList<>();
                    for (String groupColor : groupColors) {
                        final String hex = getColorHex(group, groupColor);
                        final int cost = getColorCost(group, groupColor);
                        if (groupColor == null
                                || hex == null
                                || cost == -1) continue;
                        list.add(new PlayerColor(
                                groupColor,
                                hex,
                                cost,
                                0
                        ));
                    }
                    if (list.isEmpty()) continue;
                    ramColors.put(group, list);
                }
                ramColors.put("default",
                        Collections.singletonList(new PlayerColor("default", defaultColor, 0, 0)));
            }
        }.runTaskAsynchronously(instance);
        return true;
    }

    /**
     * Get default hex color.
     *
     * @return hex color (example: #FFFFFF)
     */
    public String getDefaultColor() {
        return defaultColor;
    }
    @Contract(pure = true)
    protected @NotNull Set<Map.Entry<UUID, PlayerColor>> getLatelyUsedPlayers() {
        return playerHex.entrySet();
    }

    record PlayerColor(String color, String hex, int cost, int stars) {
        public PlayerColor(final String color, final String hex, final int cost, final int stars) {
            this.color = color;
            this.hex = ColorCodes.isHexValid(hex) ? hex : null;
            this.cost = cost < 0 ? cost * (-1) : cost;
            this.stars = Math.max(stars, 0);
        }
    }
}
