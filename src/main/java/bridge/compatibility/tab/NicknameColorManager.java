package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.config.ConfigurationFile;
import bridge.database.Connector;
import bridge.database.QueryType;
import bridge.database.Saver;
import bridge.database.UpdateType;
import bridge.modules.Currency;
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
    private static Currency currency;
    private static ConfigurationFile colorConfig;
    private static HashMap<String, List<Colors>> ramColors;
    private static final Bridge instance = Bridge.getInstance();
    private static final Saver saver = instance.getSaver();

    /**
     * Creating a color-config to use for color groups.
     *
     * @param currency {@link Currency} to use
     * @param con {@link Connector} instance
     * @return true if successful
     */
    protected static boolean setup(final Connector con, final Currency currency) {
        NicknameColorManager.con = con;
        NicknameColorManager.currency = currency;
        NicknameColorManager.ramColors = new HashMap<>();
        try {
            colorConfig = ConfigurationFile.create(new File(instance.getDataFolder(), "color-config.yml"), instance, "color-config.yml");
        } catch (final InvalidConfigurationException | FileNotFoundException e) {
            LOG.warn(e.getMessage(), e);
            return false;
        }

        if(instance.isConfigSet()){
            for(String group : getGroups()){
                //getGroupColors(group)
            }
        }
        return true;
    }

    /**
     * Get all group that was defined in {@code color-config.yml}.
     *
     * @return list go groups
     */
    protected static @NotNull List<String> getGroups() {
        return colorConfig.getStringList("groups");
    }

    /**
     * Get all colors names from group that were defined in {@code color-config.yml}.
     *
     * @param group group that you can get from {@code getGroup} method
     * @return list of group colors
     */
    protected static @NotNull List<String> getGroupColors(@NotNull final String group) {
        return colorConfig.getStringList(String.format("groups.%s", group));
    }

    /**
     * Get hex color from group and color name.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color color for nickname. May be null. You can get this value from {@code getGroupColors} method
     * @return hex color or null
     */
    protected static @Nullable String getGroupColor(final String group, final String color) {
        String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
        if (setting == null) return null;

        return setting.split(" ", 2)[0];
    }

    /**
     * Get all colors names from all groups
     * @return list of colors names
     */
    protected static @NotNull List<String> getAllColorsName() {
        List<String> list = new ArrayList<>();
        for (String group : getGroups()){
            list.addAll(getGroupColors(group));
        }
        return list;
    }

    /**
     * Get cost to be able to apply color to nickname.
     *
     * @param group group that you can get from {@code getGroup} method
     * @param color color for nickname that you can get from {@code getGroupColors} method
     * @return cost amount
     */
    protected static int getGroupColorCost(final String group, final String color) {
        String setting = colorConfig.getString(String.format("groups.%s.%s", group, color));
        if (setting == null) return -1;

        return Integer.parseInt(setting.split(" ", 2)[1], 10);
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
        if (nameTagManager == null) return;

        //Applying to nickname
        nameTagManager.setName(player, ChatColor.of(color) + p.getName() + ChatColor.RESET);
        //Applying to TAB
        TabAPI.getInstance().getTablistFormatManager().setName(player, ChatColor.of(color) + p.getName() + ChatColor.RESET);
        //Applying to chat
        p.displayName(Component.text(p.getName()).color(TextColor.fromHexString(color)));

        //save to database async
        if(save) saver.add(new Saver.Record(UpdateType.UPDATE_COLOR, color, p.getUniqueId().toString()));
    }


    /**
     * Get default hex color.
     * @return hex color (example: #FFFFFF)
     */
    protected static String getDefaultColor() {
        return colorConfig.getString("default", "#CFCFCF");
    }

    protected static void reload () {
        //TODO save groups, colors, hex values into memory and then reloading, delete them
        //HashMap<ColorName, HEX>
    }

    /**
     * Find color name by hex.
     *
     * @param hex hex color to find in {@code color-config.yml}.
     * @return color name or null
     */
    public static @Nullable String getColorNameByHex (String hex){
        //TODO check if hashmaps are empty, and only then do this method
        for (String group : getGroups()){
            for(String color : getGroupColors(group)) {
                String found = getGroupColor(group, color);
                if(found != null && found.equalsIgnoreCase(hex)) return found;
            }
        }
        return null;
    }

    public static @Nullable String getPlayerColor(@NotNull final UUID uuid){ // , final boolean hex
        /*if(hex) {
            //TODO save recent managed player's colors and after period delete them
            //getColorNameByHex(get)
        }*/
        ResultSet rs = con.querySQL(QueryType.SELECT_COLOR, uuid.toString());
        try {
            return rs.getString("color");
        } catch (SQLException e) {
            LOG.error("There was an exception with SQL", e);
            return null;
        }
    }

    private record Colors(String name, String hex, int cost){
        private Colors(String name, String hex, int cost){
            this.name = name;
            this.hex = ColorCodes.isHexValid(hex) ? hex : null;
            this.cost = cost < 0 ? cost*(-1) : cost;
        }
    }
}
