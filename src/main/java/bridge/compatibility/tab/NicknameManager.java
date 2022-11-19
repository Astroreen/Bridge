package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.compatibility.Compatibility;
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
public class NicknameManager {

    private final Connector con;
    private ConfigurationFile ColorConfig;
    private static String defaultNickColor;
    private static String defaultTextColor;
    private int taskID = -1;
    private static List<String> disabledGroups;
    private static boolean whitelist;
    private static Bridge plugin;
    private final Saver saver;
    private static NicknameManager instance;
    private final HashMap<String, List<PlayerColor>> LoadedColors;
    private final HashMap<UUID, PlayerColor> LoadedPlayers;
    private final Runnable runnable;

    protected NicknameManager(@NotNull Bridge plugin) {
        NicknameManager.plugin = plugin;
        try {
            ColorConfig = ConfigurationFile.create(new File(plugin.getDataFolder(), "color-config.yml"), plugin, "color-config.yml");
        } catch (final InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Could not load the color-config.yml file!", e);
        }
        con = new Connector();
        saver = plugin.getSaver();
        LoadedColors = new HashMap<>();
        LoadedPlayers = new HashMap<>();
        runnable = LoadedPlayers::clear;
        instance = this;
        whitelist = ColorConfig.getBoolean("settings.WhitelistMode", false);
        //registering placeholders if plugin is enabled
        if (Compatibility.getHooked().contains("PlaceholderAPI")) {
            NicknamePlaceholders placeholders = new NicknamePlaceholders();
            placeholders.setup(this);
            placeholders.register();
        }
        reload();
    }

    /**
     * Applies color to the {@link TabPlayer} nickname.
     * <p>
     * Requires TAB and UnlimitedNameTagMode to be enabled.
     *
     * @param p        the {@link Player }
     * @param gradient the color to apply
     * @param save     save color to database or not
     */
    public void applyColor(@NotNull final Player p, final String gradient, final boolean save) {
        UUID uuid = p.getUniqueId();
        TabPlayer player = TabAPI.getInstance().getPlayer(uuid);
        UnlimitedNametagManager nameTagManager =
                TabAPI.getInstance().getTeamManager() instanceof UnlimitedNametagManager
                        ? (UnlimitedNametagManager) TabAPI.getInstance().getTeamManager() : null;
        if (nameTagManager == null) return;
        final String[] hex;
        if (isGradient(gradient)) hex = gradient.toLowerCase().split(">", 2);
        else if (ColorCodes.isHexValid(gradient)) hex = new String[]{gradient.toLowerCase(), gradient.toLowerCase()};
        else return;

        String name = ColorCodes.generateColoredMessage(ColorConfig, hex[0], hex[1], p.getName());

        //Applying to nickname
        nameTagManager.setName(player, name + ChatColor.RESET);
        //Applying to TAB
        TabAPI.getInstance().getTablistFormatManager().setName(player, name + ChatColor.RESET);
        //Applying to chat (not working right now)
        //p.displayName(Component.text(p.getName()).color(TextColor.fromHexString(gradient)));
        new BukkitRunnable() {
            @Override
            public void run() {
                final String ColorName = getColorName(gradient);
                if(ColorName == null) return;
                final String texthex = getTextHex(ColorName);
                final Integer cost = getColorCost(ColorName);
                if(cost == null) return;
                final Integer stars = getPlayerStars(uuid);
                if(stars == null) return;
                LoadedPlayers.put(uuid, new PlayerColor(ColorName, gradient.toLowerCase(), texthex, cost, stars));
            }
        }.runTaskAsynchronously(plugin);
        //save to database async
        if (save) saver.add(new Saver.Record(UpdateType.UPDATE_COLOR, gradient.toLowerCase(), uuid.toString()));
    }

    /**
     * Replaces players hex colors in database. (async)
     *
     * @param fromColor which hex replace
     * @param toColor   to what hex replace
     */
    public boolean globallyReplaceColors(String fromColor, String toColor) {
        if (!isGradient(fromColor) || !isGradient(toColor)) return false;
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    List<Player> list = getPlayersWhoHasHexColor(fromColor);
                    for (Player p : list) applyColor(p, toColor, true);
                } catch (SQLException e) {
                    LOG.error("There was an exception with SQL", e);
                }
            }
        }.runTaskAsynchronously(plugin);
        saver.add(new Saver.Record(UpdateType.CHANGE_ALL_COLORS, fromColor, toColor));
        return true;
    }

    /**
     * Get all group that was defined in {@code name-config.yml}.
     *
     * @return list go groups
     */
    protected @NotNull List<String> getGroups() {
        if (!LoadedColors.isEmpty()) return LoadedColors.keySet().stream().toList();
        ConfigurationSection groups = ColorConfig.getConfigurationSection("groups");
        if (groups == null) return List.of();
        List<String> arr = new ArrayList<>();
        for (String group : groups.getKeys(false)) {
            if (isGroupNotInWhitelist(group)) continue;
            arr.add(group.toLowerCase());
        }
        return arr;
    }

    /**
     * Get all colors names from group that were defined in {@code name-config.yml}.
     *
     * @param group group that you can get from {@code getGroup} method
     * @return list of group colors or empty if not exist
     */
    protected @NotNull List<String> getGroupColors(@NotNull final String group) {
        if (isGroupNotInWhitelist(group)) return List.of();
        if (LoadedColors.isEmpty()) {
            ConfigurationSection colors =
                    ColorConfig.getConfigurationSection(String.format("groups.%s", group));
            if (colors == null) return List.of();
            List<String> list = new ArrayList<>();
            colors.getKeys(false).forEach(c -> list.add(c.toLowerCase()));
            return list;
        }
        if (!LoadedColors.containsKey(group)) return List.of();
        List<String> list = new ArrayList<>();
        for (PlayerColor color : LoadedColors.get(group)) {
            if (color != null) list.add(color.name());
        }
        return list;
    }

    /**
     * Get first to match color cost from color name
     *
     * @param ColorName color name
     * @return cost amount or null
     */
    public @Nullable Integer getColorCost(final @NotNull String ColorName) {
        for (String group : getGroups()) {
            for (String colors : getGroupColors(group)) {
                if (colors.equals(ColorName.toLowerCase())) return getColorCost(group, ColorName);
            }
        }
        return null;
    }

    /**
     * Get cost to be able to apply name to nickname.
     *
     * @param group     group that you can get from {@code getGroup} method
     * @param ColorName color name
     * @return cost amount or null if the color doesn't exist
     */
    public @Nullable Integer getColorCost(final String group, final String ColorName) {
        if (isGroupNotInWhitelist(group)) return null;
        if (LoadedColors.isEmpty()) {
            String setting = ColorConfig.getString(String.format("groups.%s.%s", group, ColorName));
            if (setting == null) return null;
            return Integer.parseInt(setting.toLowerCase().split(":")[1], 10);
        }
        if (!LoadedColors.containsKey(group)) return null;
        for (PlayerColor playerColor : LoadedColors.get(group)) {
            if (playerColor.name().equals(ColorName)) return playerColor.cost();
        }
        return null;
    }

    public @Nullable String getGradient(final String ColorName) {
        for (String group : getGroups()) {
            for (String color : getGroupColors(group)) {
                if (color.equals(ColorName)) {
                    return getGradient(group, color);
                }
            }
        }
        return null;
    }

    /**
     * Get hex from given group and name.
     *
     * @param group     group that you can get from {@code getGroup} method
     * @param ColorName name for nickname. May be null. You can get this value from {@code getGroupColors} method
     * @return name or null
     */
    protected @Nullable String getGradient(final String group, final String ColorName) {
        if (isGroupNotInWhitelist(group)) return null;
        if (LoadedColors.isEmpty()) {
            String setting = ColorConfig.getString(String.format("groups.%s.%s", group, ColorName));
            if (setting == null) return null;
            final String color = setting.split(":")[0].toLowerCase();
            if (isGradient(color)) return color;
            else if (ColorCodes.isHexValid(color)) return color + ">" + color;
            else return null;
        }
        if (!LoadedColors.containsKey(group)) return null;
        for (PlayerColor playerColor : LoadedColors.get(group)) {
            if (playerColor.name().equals(ColorName)) return playerColor.gradient();
        }
        return null;
    }

    /**
     * Find color name by hex.
     *
     * @param color color to find in {@code name-config.yml}.
     * @return color name or null if not exist
     */
    public @Nullable String getColorName(@NotNull String color) {
        color = color.toLowerCase();
        if (ColorCodes.isHexValid(color)) color = color + ">" + color;
        else if (!isGradient(color)) return null;
        for (String group : getGroups()) {
            for (String ColorName : getGroupColors(group)) {
                String found = getGradient(group, ColorName);
                if (color.equals(found)) return ColorName;
            }
        }
        return null;
    }

    public @Nullable String getTextHex(final String color) {
        for (String group : getGroups()) {
            for (String colors : getGroupColors(group)) {
                if (colors.equals(color)) return getTextHex(group, color);
            }
        }
        return null;
    }

    public @Nullable String getTextHex(final String group, final String color) {
        if (isGroupNotInWhitelist(group)) return null;

        if (LoadedColors.isEmpty()) {
            String setting = ColorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return null;
            final String textcolor = setting.split(":")[2];
            return ColorCodes.isHexValid(textcolor) ? textcolor.toLowerCase() : null;
        }
        if (!LoadedColors.containsKey(group)) return null;
        for (PlayerColor playerColor : LoadedColors.get(group)) {
            if (playerColor.name().equals(color)) return playerColor.texthex();
        }
        return null;
    }


    public @Nullable PlayerColor getPlayerInfo(final UUID uuid) {
        if (LoadedPlayers.containsKey(uuid)) return LoadedPlayers.get(uuid);
        else {
            final String gradient = getPlayerColor(uuid);
            if(gradient == null) return null;
            final String ColorName = getColorName(gradient);
            if(ColorName == null) return null;
            final String textcolor = getTextHex(ColorName);
            final Integer cost = getColorCost(ColorName);
            if(cost == null) return null;
            final Integer stars = getPlayerStars(uuid);
            if(stars == null) return null;
            PlayerColor answer = new PlayerColor(ColorName, gradient, textcolor, cost, stars);
            LoadedPlayers.put(uuid, answer);
            return answer;
        }
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

    private @Unmodifiable List<Player> getPlayersWhoHasHexColor(String color) throws SQLException {
        if (!isGradient(color)) return List.of();
        List<String> uuids = new ArrayList<>();
        ResultSet rs = con.querySQL(QueryType.SELECT_UUID_BY_COLOR, color);
        while (rs.next()) uuids.add(rs.getString("playerID"));

        List<Player> has = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers())
            if (uuids.contains(p.getUniqueId().toString())) has.add(p);
        return has;
    }

    private boolean isGroupNotInWhitelist(String group) {
        if (whitelist && !disabledGroups.contains(group)) return true;
        else return (!whitelist && disabledGroups.contains(group));
    }

    /**
     * Gets player name.
     *
     * @param uuid Player's {@link UUID}
     * @return gradient
     */
    public @Nullable String getPlayerColor(@NotNull final UUID uuid) {
        if (LoadedPlayers.containsKey(uuid)) return LoadedPlayers.get(uuid).gradient();
        else {
            final ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
            try {
                if (rs.next()) return rs.getString("color");
            } catch (SQLException e) {
                LOG.error("There was an exception with SQL", e);
                return null;
            }
        }
        return null;
    }

    private @Nullable Integer getPlayerStars(@NotNull final UUID uuid) {
        if (LoadedPlayers.containsKey(uuid)) return LoadedPlayers.get(uuid).stars();
        final ResultSet rs = con.querySQL(QueryType.SELECT_STARS, uuid.toString());
        try {
            if (rs.next()) return rs.getInt("stars");
        } catch (SQLException e) {
            LOG.error("There was an exception with SQL", e);
        }
        return null;
    }

    /**
     * @return true if successfully reload
     */
    public boolean reload() {
        con.refresh();
        try {
            ColorConfig.reload();
        } catch (IOException e) {
            LOG.error("Could not load the color-config.yml file!", e);
            return false;
        }
        final String[] before = ColorConfig.getString("default", "#CFCFCF:#545454").split(":");
        final String nick = before[0];
        final String text = before[1];
        if (ColorCodes.isHexValid(nick)) defaultNickColor = nick + ">" + nick;
        else if (isGradient(nick)) defaultNickColor = nick;
        else {
            defaultNickColor = "#CFCFCF>#CFCFCF";
            LOG.error("Wrong default nickcolor in color-config!");
        }
        LOG.debug("Default nickcolor is: " + defaultNickColor);
        if (ColorCodes.isHexValid(text)) {
            defaultTextColor = text;
            LOG.debug("Default textcolor is: " + text);
        } else {
            defaultTextColor = "#545454";
            LOG.error("Wrong default textcolor in color-config. Using default #545454.");
        }

        LoadedColors.clear();
        LoadedPlayers.clear();

        if (taskID != -1) Bukkit.getScheduler().cancelTask(taskID);

        final long updateTime = ColorConfig.getLong("settings.UpdateTime") * 1200;
        disabledGroups = ColorConfig.getStringList("settings.disabledGroups");
        whitelist = ColorConfig.getBoolean("settings.WhitelistMode", false);
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, updateTime, updateTime).getTaskId();
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<String, List<PlayerColor>> temp = new HashMap<>();
                StringBuilder builder = new StringBuilder();
                temp.put("default",
                        Collections.singletonList(new PlayerColor("default", getDefaultNickColor(), getDefaultTextColor(), 0, 0)));
                for (String group : getGroups()) {
                    List<PlayerColor> list = new ArrayList<>();
                    for (String ColorName : getGroupColors(group)) {
                        if (ColorName == null) continue;
                        final String gradient = getGradient(group, ColorName);
                        final String texthex = getTextHex(ColorName);
                        final Integer cost = getColorCost(group, ColorName);
                        if (texthex == null || gradient == null || cost == null) continue;
                        list.add(new PlayerColor(ColorName, gradient, texthex, cost, 0));
                        builder.append(ColorName).append(", ");
                    }
                    if (list.isEmpty()) continue;
                    temp.put(group, list);
                }
                builder.append("default");
                LoadedColors.putAll(temp);
                LOG.debug("Colors added to the memory: " + builder + ".");
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }

    public void setPlayerStars(UUID uuid, int stars) {
        if (stars > 0 && LoadedPlayers.containsKey(uuid)) {
            PlayerColor info = LoadedPlayers.get(uuid);
            LoadedPlayers.put(uuid, new PlayerColor(info.name(), info.gradient(), info.texthex(), info.cost(), stars));
        }
    }

    /**
     * Get default nick hex.
     *
     * @return gradient name (example: #FFFFFF)
     */
    public static String getDefaultNickColor() {
        return defaultNickColor;
    }

    /**
     * Get default text hex.
     *
     * @return gradient name (example: #FFFFFF)
     */
    public static String getDefaultTextColor() {
        return defaultTextColor;
    }
    public ConfigurationFile getConfig() {return ColorConfig;}

    @Contract(pure = true)
    public boolean isGradient(@NotNull String color) {
        return color.matches("^#([A-Fa-f\\d]{6})>#([A-Fa-f\\d]{6})$");
    }

    @Contract(pure = true)
    protected Set<UUID> getLatelyUsedPlayers() {
        return LoadedPlayers.keySet();
    }

    protected static NicknameManager getInstance() {
        return instance;
    }

    public record PlayerColor(String name, String gradient, String texthex, int cost, int stars) {
        public PlayerColor(final String name, final String gradient, final String texthex, final int cost, final int stars) {
            final NicknameManager manager = NicknameManager.getInstance();
            this.name = name == null ? null : name.toLowerCase();
            this.gradient = manager.isGradient(gradient) ? gradient.toLowerCase() : null;
            this.texthex = ColorCodes.isHexValid(texthex) ? texthex : getDefaultTextColor();
            this.cost = cost < 0 ? cost * (-1) : cost;
            this.stars = Math.max(stars, 0);
        }
    }
}
