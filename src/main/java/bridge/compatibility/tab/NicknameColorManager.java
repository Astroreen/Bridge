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

    private final Connector con;
    private ConfigurationFile colorConfig;
    private String defaultNickColor;
    private String defaultTextColor;
    private int taskID = -1;
    private static List<String> disabledGroups;
    private static boolean whitelist;
    private static Bridge plugin;
    private static NicknameColorManager instance;
    private static Saver saver;
    private final HashMap<String, List<PlayerColor>> ramColors;
    private final HashMap<UUID, PlayerColor> playerHex;
    private final Runnable runnable;

    protected NicknameColorManager(@NotNull Bridge plugin) {
        NicknameColorManager.plugin = plugin;
        try {
            colorConfig = ConfigurationFile.create(new File(plugin.getDataFolder(), "color-config.yml"), plugin, "color-config.yml");
        } catch (final InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Could not load the color-config.yml file!", e);
        }
        saver = NicknameColorManager.plugin.getSaver();
        con = new Connector();
        ramColors = new HashMap<>();
        playerHex = new HashMap<>();
        runnable = playerHex::clear;
        instance = this;
        whitelist = colorConfig.getBoolean("settings.WhitelistMode", false);
        //registering placeholders if plugin is enabled
        if (Compatibility.getHooked().contains("PlaceholderAPI")) {
            NicknamePlaceholders placeholders = new NicknamePlaceholders();
            placeholders.setup(this);
            placeholders.register();
        }
        reload();
    }

    /**
     * Applies hex to the {@link TabPlayer} nickname.
     * <p>
     * Requires TAB and UnlimitedNameTagMode to be enabled.
     *
     * @param p    the {@link Player }
     * @param hex  the hex name to apply
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
                final String nickcolor = getTextHex(color);
                final int cost = getHexColorCost(hex);
                final int stars = getPlayerStars(uuid);
                if (color == null
                        || nickcolor == null
                        || cost == -1
                        || stars == -1) return;
                playerHex.put(uuid, new PlayerColor(color, hex, nickcolor, cost, stars));
            }
        }.runTaskAsynchronously(plugin);
        //save to database async
        if (save) saver.add(new Saver.Record(UpdateType.UPDATE_COLOR, hex, uuid.toString()));
    }

    /**
     * Replaces players hex colors in database. (async)
     *
     * @param fromColor which hex replace
     * @param toColor   to what hex replace
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
        if (!ramColors.isEmpty()) return ramColors.keySet().stream().toList();
        ConfigurationSection groups = colorConfig.getConfigurationSection("groups");
        if (groups == null) return List.of();
        List<String> arr = new ArrayList<>();
        for (String group : groups.getKeys(false).stream().toList()) {
            if (whitelist && !disabledGroups.contains(group)) continue;
            else if (!whitelist && disabledGroups.contains(group)) continue;
            arr.add(group);
        }
        return arr;
    }

    /**
     * Get all colors names from group that were defined in {@code name-config.yml}.
     *
     * @param group group that you can get from {@code getGroup} method
     * @return empty if not exist or list of group colors
     */
    protected @NotNull List<String> getGroupColors(@NotNull final String group) {
        if (whitelist && !disabledGroups.contains(group)) return List.of();
        else if (!whitelist && disabledGroups.contains(group)) return List.of();

        if (ramColors.isEmpty()) {
            ConfigurationSection colors =
                    colorConfig.getConfigurationSection(String.format("groups.%s", group));
            if (colors == null) return List.of();
            return colors.getKeys(false).stream().toList();
        }
        if (!ramColors.containsKey(group)) return List.of();
        List<String> list = new ArrayList<>();
        for (PlayerColor color : ramColors.get(group)) {
            if (color != null) list.add(color.name());
        }
        return list;
    }

    /**
     * Get first to match color cost from color name
     *
     * @param color the name
     * @return cost amount; -1 if something went wrong or group doesn't exist
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
     * Get cost to be able to apply name to nickname.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color name for nickname that you can get from {@code getGroupColors} method
     * @return cost amount; -1 if something went wrong or group doesn't exist
     */
    public int getColorCost(final String group, final String color) {
        if (whitelist && !disabledGroups.contains(group)) return -1;
        else if (!whitelist && disabledGroups.contains(group)) return -1;
        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return -1;
            return Integer.parseInt(setting.split(":")[1], 10);
        }
        if (!ramColors.containsKey(group)) return -1;
        for (PlayerColor playerColor : ramColors.get(group)) {
            if (playerColor.name().equalsIgnoreCase(color)) return playerColor.cost();
        }
        return -1;
    }

    /**
     * Get first to match color name cost from hex
     *
     * @param hex hex color
     * @return cost of the name from name-config.yml
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
     * Get hex from given name.
     *
     * @param color name for nickname. May be null. You can get this value from {@code getGroupColors} method
     * @return name or null if not exist
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
     * Get hex from given group and name.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color name for nickname. May be null. You can get this value from {@code getGroupColors} method
     * @return name or null
     */
    protected @Nullable String getColorHex(final String group, final String color) {
        if (whitelist && !disabledGroups.contains(group)) return null;
        else if (!whitelist && disabledGroups.contains(group)) return null;

        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return null;
            final String nickhex = setting.split(":")[0];
            return ColorCodes.isHexValid(nickhex) ? nickhex : null;
        }
        if (!ramColors.containsKey(group)) return null;
        for (PlayerColor playerColor : ramColors.get(group)) {
            if (playerColor.name().equalsIgnoreCase(color)) return playerColor.nickhex();
        }
        return null;
    }


    /**
     * Find color name by hex.
     *
     * @param hex color name to find in {@code name-config.yml}.
     * @return color name or null if not exist
     */
    public @Nullable String getColorNameByHex(final String hex) {
        for (String group : getGroups()) {
            for (String color : getGroupColors(group)) {
                String found = getColorHex(group, color);
                if (found != null && found.equalsIgnoreCase(hex)) return color;
            }
        }
        return null;
    }

    public @Nullable String getTextHex(final String color) {
        for (String group : getGroups()) {
            for (String colors : getGroupColors(group)) {
                if (colors.equalsIgnoreCase(color)) return getTextHex(group, color);
            }
        }
        return null;
    }

    public @Nullable String getTextHex(final String group, final String color) {
        if (whitelist && !disabledGroups.contains(group)) return null;
        else if (!whitelist && disabledGroups.contains(group)) return null;

        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return null;
            final String textcolor = setting.split(":")[2];
            return ColorCodes.isHexValid(textcolor) ? textcolor : null;
        }
        if (!ramColors.containsKey(group)) return null;
        for (PlayerColor playerColor : ramColors.get(group)) {
            if (playerColor.name().equalsIgnoreCase(color)) return playerColor.texthex();
        }
        return null;
    }

    /**
     * Gets player name.
     *
     * @param uuid      Player's {@link UUID}
     * @param returnHex return color hex or name
     * @return name hex or name
     */
    public @Nullable String getPlayerColor(@NotNull final UUID uuid, final boolean returnHex) {
        String hex = null;
        if (playerHex.containsKey(uuid)) hex = playerHex.get(uuid).nickhex();
        else {
            final ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
            try {
                if (rs.next()) hex = rs.getString("color");
            } catch (SQLException e) {
                LOG.error("There was an exception with SQL", e);
                return null;
            }
        }
        if (hex == null) return null;
        else if (returnHex) return hex;
        final String finalHex = hex;
        final String color = getColorNameByHex(hex);
        if (color == null) return null;
        new BukkitRunnable() {
            @Override
            public void run() {
                final String texthex = getTextHex(color);
                final int cost = getColorCost(color);
                final int stars = getPlayerStars(uuid);
                if (texthex == null || cost == -1 || stars == -1) return;
                playerHex.put(uuid,
                        new PlayerColor(color, finalHex, texthex, cost, stars)
                );
            }
        }.runTaskAsynchronously(plugin);
        return color;
    }

    public @Nullable PlayerColor getPlayerInfo(final UUID uuid) {
        if (playerHex.containsKey(uuid)) return playerHex.get(uuid);
        else {
            final String hex = getPlayerColor(uuid, true);
            final String color = getColorNameByHex(hex);
            final String textcolor = getTextHex(color);
            final int cost = getHexColorCost(hex);
            final int stars = getPlayerStars(uuid);
            if (hex == null || color == null || textcolor == null || cost == -1 || stars == -1) return null;
            PlayerColor answer = new PlayerColor(color, hex, textcolor, cost, stars);
            playerHex.put(uuid, answer);
            return answer;
        }
    }

    private int getPlayerStars(@NotNull final UUID uuid) {
        if (playerHex.containsKey(uuid)) return playerHex.get(uuid).stars();
        final ResultSet rs = con.querySQL(QueryType.SELECT_STARS, uuid.toString());
        try {
            if (rs.next()) return rs.getInt("stars");
        } catch (SQLException e) {
            LOG.error("There was an exception with SQL", e);
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
    public boolean reload() {
        con.refresh();
        try {
            colorConfig.reload();
        } catch (IOException e) {
            LOG.error("Could not load the color-config.yml file!", e);
            return false;
        }
        final String before = colorConfig.getString("default", "#CFCFCF:#545454");
        final String nick = before.split(":", 2)[0];
        final String text = before.split(":", 2)[1];
        if (ColorCodes.isHexValid(nick)) {
            defaultNickColor = nick;
            LOG.debug("Default nickcolor now is: " + nick);
        } else {
            defaultNickColor = "#CFCFCF";
            LOG.error("Wrong default nickcolor in color-config. Using default #CFCFCF");
        }
        if (ColorCodes.isHexValid(text)) {
            defaultTextColor = text;
            LOG.debug("Default textcolor now is: " + text);
        } else {
            defaultTextColor = "#CFCFCF";
            LOG.error("Wrong default textcolor in color-config. Using default #545454");
        }

        ramColors.clear();
        playerHex.clear();

        if (taskID != -1) Bukkit.getScheduler().cancelTask(taskID);

        final long updateTime = colorConfig.getLong("settings.UpdateTime") * 1200;
        disabledGroups = colorConfig.getStringList("settings.disabledGroups");
        whitelist = colorConfig.getBoolean("settings.WhitelistMode", false);
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, updateTime, updateTime).getTaskId();
        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<String, List<PlayerColor>> temp = new HashMap<>();
                List<String> groups = getGroups();
                for (String group : groups) {
                    List<PlayerColor> list = new ArrayList<>();
                    for (String nickcolor : getGroupColors(group)) {
                        if (nickcolor == null) continue;
                        final String hex = getColorHex(group, nickcolor);
                        final String textcolor = getTextHex(nickcolor);
                        final int cost = getColorCost(group, nickcolor);
                        if (textcolor == null || hex == null || cost == -1) continue;
                        list.add(new PlayerColor(nickcolor, hex, textcolor, cost, 0));
                        LOG.debug("Color \"" + nickcolor + "\" was added to the memory.");
                    }
                    if (list.isEmpty()) continue;
                    temp.put(group, list);
                }
                temp.put("default",
                        Collections.singletonList(new PlayerColor("default", getDefaultNickColor(), getDefaultTextColor(), 0, 0)));
                ramColors.putAll(temp);
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }

    public void setPlayerStars(UUID uuid, int stars){
        if(stars > 0 && playerHex.containsKey(uuid)) {
            PlayerColor info = playerHex.get(uuid);
            playerHex.replace(uuid, info,
                    new PlayerColor(info.name(), info.nickhex(), info.texthex(), info.cost(), stars));
        }
    }

    /**
     * Get default nick hex.
     *
     * @return nickhex name (example: #FFFFFF)
     */
    public String getDefaultNickColor() {
        return defaultNickColor;
    }

    /**
     * Get default text hex.
     *
     * @return nickhex name (example: #FFFFFF)
     */
    public String getDefaultTextColor() {
        return defaultTextColor;
    }

    @Contract(pure = true)
    protected @NotNull Set<Map.Entry<UUID, PlayerColor>> getLatelyUsedPlayers() {
        return playerHex.entrySet();
    }

    protected static NicknameColorManager getInstance() {
        return instance;
    }

    record PlayerColor(String name, String nickhex, String texthex, int cost, int stars) {
        public PlayerColor(final String name, final String nickhex, final String texthex, final int cost, final int stars) {
            this.name = name;
            this.nickhex = ColorCodes.isHexValid(nickhex) ? nickhex : null;
            this.texthex = texthex;
            this.cost = cost < 0 ? cost * (-1) : cost;
            this.stars = Math.max(stars, 0);
        }
    }
}
