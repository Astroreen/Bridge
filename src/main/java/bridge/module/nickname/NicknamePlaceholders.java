package bridge.module.nickname;

import bridge.Bridge;
import bridge.compatibility.tab.TABManager;
import common.Currency;
import bridge.utils.ColorCodes;
import bridge.utils.PlayerConverter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NicknamePlaceholders extends PlaceholderExpansion {

    private final Bridge plugin = Bridge.getInstance();
    private NicknameManager manager;

    public void setup(NicknameManager manager) {this.manager = manager;}

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
            case "have_nickcolor_name", "have_textcolor_name" -> {
                final NicknameManager.PlayerColor color = manager.getPlayerInfo(p.getUniqueId());
                return color == null ? "" : color.name();
            }
            case "have_nickcolor" -> {
                final String color = manager.getPlayerColor(p.getUniqueId());
                if (color == null) return "";
                String[] hex = new String[2];
                if(manager.isGradient(color)) hex = color.split(">");
                else if (ColorCodes.isHexValid(color)) {
                    hex[0] = color;
                    hex[1] = color;
                }
                final String name = p.getName();
                if (name == null) return "";
                return ColorCodes.generateColoredMessage(manager.getConfig(), hex[0], hex[1], name);
            }
            case "have_nickcolor_hex" -> {
                final NicknameManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                if (info == null) return "";
                final String[] hex = info.gradient().split(">");
                if (hex[0].equals(hex[1])) return hex[0];
                return String.format("%s, %s", hex[0], hex[1]);
            }
            case "have_textcolor_hex" -> {
                final NicknameManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                return info == null ? "" : info.texthex();
            }
            case "have_custom_color" -> {
                final NicknameManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                if (info == null) return "";
                return manager.getColorName(info.name() == null ? "yes" : "no");
            }
            case "color_cost" -> {
                int cost;
                final NicknameManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                if (info == null) return "";
                cost = info.cost();
                if (cost == -1) return "";
                return String.valueOf(cost);
            }
            case "have_stars" -> {
                final Currency stars = TABManager.getStars();
                if (stars == null) return "";
                final int amount = stars.getCurrencyAmount(p.getUniqueId());
                if (amount == -1) return "";
                return String.valueOf(amount);
            }
            case "can_use_hex", "can_use_custom" -> {
                Player player = PlayerConverter.getPlayer(p.getUniqueId());
                if (player == null) return "";
                return player.hasPermission("bridge.nickname.name.set.nickhex") ? "yes" : "no";
            }
            default -> {
                String[] args = params.split("_");
                if (args[0].equals("have")) {
                    if (args[1].equals("stars") && args.length == 3) {
                        int stars;
                        final NicknameManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                        if (info == null) return "";
                        stars = info.stars();
                        if (stars == -1) return "";
                        return stars >= Integer.parseInt(args[2], 10) ? "yes" : "no";
                    }
                } else if (args[0].equals("can") && args[1].equals("use") && args.length == 3) {
                    if (!manager.getAllColorsName().contains(args[2])) return "";
                    final Integer colorCost = manager.getColorCost(args[2]);
                    final NicknameManager.PlayerColor info = manager.getPlayerInfo(p.getUniqueId());
                    if (info == null || colorCost == null || info.stars() == -1) return "";
                    return info.stars() >= colorCost ? "yes" : "no";
                } else if (args[0].equals("color") && args[1].equals("preview")) {
                    final String color = manager.getGradient(args[2]);
                    if (color == null) return "";
                    final String[] hex = color.split(">");
                    final String name = p.getName();
                    if (name != null)
                        return ColorCodes.generateColoredMessage(manager.getConfig(), hex[0], hex[1], name);
                }
            }
        }
        return "";
    }
}