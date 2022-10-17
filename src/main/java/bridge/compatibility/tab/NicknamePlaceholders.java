package bridge.compatibility.tab;

import bridge.Bridge;
import bridge.utils.PlayerConverter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class NicknamePlaceholders extends PlaceholderExpansion {

    private final Bridge plugin = Bridge.getInstance();
    private final HashMap<UUID, NicknameColorManager.PlayerColor> data = new HashMap<>();
    private NicknameColorManager manager;
    private int taskID = -1;
    private final Runnable runnable = data::clear;

    protected void setup(NicknameColorManager manager, long updateTime) {
        this.manager = manager;
        data.clear();
        if (taskID != -1) Bukkit.getScheduler().cancelTask(taskID);
        taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, updateTime, updateTime).getTaskId();
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
        return "nickname";
    }

    /**
     * Name of person who created the expansion
     *
     * @return The color of the author as a String.
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
            case "have_color" -> {
                if (data.containsKey(p.getUniqueId())) return data.get(p.getUniqueId()).color();
                final String color = manager.getPlayerColor(p.getUniqueId(), false);
                return color == null ? "" : color;
            }
            case "have_color_hex" -> {
                if (data.containsKey(p.getUniqueId())) return data.get(p.getUniqueId()).hex();
                final String hex = manager.getPlayerColor(p.getUniqueId(), true);
                return hex == null ? "" : hex;
            }
            case "have_nickcolor" -> {
                if (data.containsKey(p.getUniqueId())) return data.get(p.getUniqueId()).nickcolor();
                final String color = manager.getPlayerColor(p.getUniqueId(), false);
                if (color == null) return "";
                final String nickcolor = manager.getNicknameColor(color);
                return nickcolor == null ? "" : nickcolor;
            }
            case "have_custom_color" -> {
                return manager.getColorNameByHex(
                        manager.getPlayerColor(p.getUniqueId(),
                                true)) == null ? "yes" : "no";
            }
            case "color_cost" -> {
                int cost = -1;
                if (!data.containsKey(p.getUniqueId())) {
                    final NicknameColorManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                    if (info == null) return "";
                    cost = info.cost();
                    if (cost == -1) return "";
                    data.put(p.getUniqueId(), info);
                }
                return String.valueOf(cost);
            }
            case "have_stars" -> {
                int stars;
                if (!data.containsKey(p.getUniqueId())) {
                    final NicknameColorManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                    if (info == null) return "";
                    stars = info.stars();
                    if (stars == -1) return "";
                    data.put(p.getUniqueId(), info);
                } else stars = data.get(p.getUniqueId()).stars();

                return String.valueOf(stars);
            }
            case "can_use_hex" -> {
                Player player = PlayerConverter.getPlayer(p.getUniqueId());
                if (player == null) return "";
                return player.hasPermission("bridge.nickname.color.set.hex") ? "yes" : "no";
            }
            default -> {
                String[] args = params.split("_");
                if (args[0].equals("have")) {
                    if (args[1].equals("stars") && args.length == 3) {
                        int stars;
                        if (!data.containsKey(p.getUniqueId())) {
                            final NicknameColorManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                            if (info == null) return "";
                            stars = info.stars();
                            if (stars == -1) return "";
                            data.put(p.getUniqueId(), info);
                        } else stars = data.get(p.getUniqueId()).stars();

                        return stars >= Integer.parseInt(args[2], 10)  ? "yes" : "no";
                    }
                } else if (args[0].equals("can") && args[1].equals("use") && args.length == 3) {
                    if (!manager.getAllColorsName().contains(args[2])) return "";
                    final int colorCost = manager.getColorCost(args[2]);
                    int stars;
                    if (!data.containsKey(p.getUniqueId())) {
                        final NicknameColorManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                        if (info == null) return "";
                        stars = info.stars();
                        if (stars == -1) return "";
                        data.put(p.getUniqueId(), info);
                    } else stars = data.get(p.getUniqueId()).stars();


                    if (stars == -1) return "";
                    return stars >= colorCost ? "yes" : "no";
                } else return "";
            }
        }
        return "";
    }
}