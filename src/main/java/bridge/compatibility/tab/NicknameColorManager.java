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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@CustomLog
public class NicknameColorManager {

    private static Connector con;
    private static ConfigurationFile colorConfig;
    private static long UpdateTime;
    private static final Bridge instance = Bridge.getInstance();
    private static final Saver saver = instance.getSaver();
    private static final HashMap<String, List<Colors>> ramColors = new HashMap<>();
    private static final HashMap<UUID, String> playerHex = new HashMap<>();
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
     * @param con      {@link Connector} instance
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


        UpdateTime = colorConfig.getLong("settings.UpdateTime")*1200;
        task = runnable.runTaskTimerAsynchronously(instance, UpdateTime, UpdateTime);
        return true;
    }

    /**
     * Get all group that was defined in {@code color-config.yml}.
     *
     * @return list go groups
     */
    protected static @NotNull List<String> getGroups() {
        if (ramColors.isEmpty()) return colorConfig.getStringList("groups");

        return ramColors.keySet().stream().toList();
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
        for (Colors color : ramColors.get(group)) {
            list.add(color.name);
        }
        return list;
    }

    /**
     * Get hex color from given group and color name.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color color for nickname. May be null. You can get this value from {@code getGroupColors} method
     * @return hex color or null
     */
    protected static @Nullable String getGroupColor(final String group, final String color) {
        if (ramColors.isEmpty()) {
            String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
            if (setting == null) return null;
            return setting.split(" ", 2)[0];
        }
        if(ramColors.containsKey(group)) return null;
        for (Colors colors : ramColors.get(group)) {
            if (colors.name().equalsIgnoreCase(color)) return colors.hex();
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
        if(ramColors.containsKey(group)) return -1;
        for (Colors colors : ramColors.get(group)) {
            if (colors.name().equalsIgnoreCase(color)) return colors.cost();
        }
        return -1;
    }

    /**
     * Get all colors names from all groups
     *
     * @return list of colors names
     */
    protected static @NotNull List<String> getAllColorsName() {
        List<String> list = new ArrayList<>();
        for (String group : getGroups()) {
            list.addAll(getGroupColors(group));
        }
        return list;
    }

    /**
     * Applies color to the {@link Player} nickname.
     * <p>
     * Requires TAB and UnlimitedNameTagMode to be enabled.
     *
     * @param p     the {@link Player }
     * @param color the hex color to apply
     * @param save  save color to database or not
     */
    protected static void applyNicknameColor(@NotNull final Player p, final String color, final boolean save) {
        TabPlayer player = TabAPI.getInstance().getPlayer(p.getUniqueId());
        UnlimitedNametagManager nameTagManager =
                TabAPI.getInstance().getTeamManager() instanceof UnlimitedNametagManager
                        ? (UnlimitedNametagManager) TabAPI.getInstance().getTeamManager() : null;
        if (nameTagManager == null || !ColorCodes.isHexValid(color)) return;
        //Applying to nickname
        nameTagManager.setName(player, ChatColor.of(color) + p.getName() + ChatColor.RESET);
        //Applying to TAB
        TabAPI.getInstance().getTablistFormatManager().setName(player, ChatColor.of(color) + p.getName() + ChatColor.RESET);
        //Applying to chat
        p.displayName(Component.text(p.getName()).color(TextColor.fromHexString(color)));
        playerHex.put(p.getUniqueId(), color);

        //save to database async
        if (save) saver.add(new Saver.Record(UpdateType.UPDATE_COLOR, color, p.getUniqueId().toString()));
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
        UpdateTime = colorConfig.getLong("settings.UpdateTime")*1200;
        task = runnable.runTaskTimerAsynchronously(instance, UpdateTime, UpdateTime);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.isConfigSet()) {
                    for (String group : getGroups()) {
                        List<String> groupColors = getGroupColors(group);
                        List<Colors> list = new ArrayList<>();
                        for (int i = 0; i <= groupColors.size(); i++) {
                            list.add(new Colors(
                                    groupColors.get(i),
                                    getGroupColor(group, groupColors.get(i)),
                                    getColorCost(group, groupColors.get(i))
                            ));
                        }
                        ramColors.put(group, list);
                    }
                }
            }
        }.runTaskAsynchronously(instance);

    }

    /**
     * Find color name by hex.
     *
     * @param hex hex color to find in {@code color-config.yml}.
     * @return color name if exist or null if not
     */
    public static @Nullable String getColorNameByHex(String hex) {
        for (String group : getGroups()) {
            for (String color : getGroupColors(group)) {
                String found = getGroupColor(group, color);
                if (found != null && found.equalsIgnoreCase(hex)) return found;
            }
        }
        return null;
    }

    public static @Nullable String getPlayerColor(@NotNull final UUID uuid, final boolean returnHex) {
        final String hex;
        if (playerHex.containsKey(uuid)) hex = playerHex.get(uuid);
        else {
            ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
            try {
                return rs.getString("color");
            } catch (SQLException e) {
                LOG.error("There was an exception with SQL", e);
                return null;
            }
        }

        if(returnHex) return hex;
        else return getColorNameByHex(hex);
    }

    @Contract(pure = true)
    public static @NotNull Set<Map.Entry<UUID, String>> getLatelyUsedPlayers () {
        return playerHex.entrySet();
    }

    private record Colors(String name, String hex, int cost) {
        private Colors(String name, String hex, int cost) {
            this.name = name;
            this.hex = ColorCodes.isHexValid(hex) ? hex : null;
            this.cost = cost < 0 ? cost * (-1) : cost;
        }
    }
}
