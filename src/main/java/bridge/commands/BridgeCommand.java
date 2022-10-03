package bridge.commands;

import bridge.Bridge;
import bridge.MessageType;
import bridge.compatibility.Compatibility;
import bridge.compatibility.tab.NicknameColorManager;
import bridge.compatibility.tab.TABManager;
import bridge.config.Config;
import bridge.modules.Currency;
import bridge.modules.logger.DebugHandlerConfig;
import bridge.utils.ColorCodes;
import bridge.utils.PlayerConverter;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

@CustomLog
public class BridgeCommand implements CommandExecutor, SimpleTabCompleter {

    private final Bridge instance = Bridge.getInstance();
    DebugHandlerConfig debugHandler;

    public BridgeCommand() {
        debugHandler = new DebugHandlerConfig(instance.getPluginConfig());
        final PluginCommand command = instance.getCommand("bridge");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if ("bridge".equalsIgnoreCase(cmd.getName())) {
            LOG.debug("Executing /bridge command for user " + sender.getName()
                    + " with arguments: " + Arrays.toString(args));
            // if the command is empty, display help message
            if (args.length == 0) {
                //TODO displayHelp(sender, alias);
                return true;
            }

            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "language", "lang" -> handleLanguage(sender, args);
                case "version", "ver", "v" ->
                        sendMessage(sender, MessageType.VERSION, instance.getDescription().getVersion());
                case "debug" -> handleDebug(sender, args);
                case "reload", "rl" -> {
                    //just reloading
                    instance.reload();
                    sendMessage(sender, MessageType.RELOADED);
                }
                case "nickname", "nick" -> {
                    if (!TABManager.isModuleEnabled()) sendMessage(sender, MessageType.MODULE_STATE, "ColorNickname", Config.getMessage(MessageType.DISABLED));
                    else if (!Compatibility.getHooked().contains("TAB")) sendMessage(sender, MessageType.PLUGIN_DISABLED, "TAB");
                    else handleNickName(sender, args);
                }
                default -> sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[0]);
            }
            LOG.debug("Command executing done");
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<String>> simpleTabComplete(CommandSender sender, Command command, String alias, String @NotNull ... args) {
        if (args.length == 1) {
            return Optional.of(Arrays.asList("language", "version", "reload", "debug", "nickname"));
        }
        return switch (args[0].toLowerCase(Locale.ROOT)) {
            case "language" -> completeLanguage(args);
            case "debug" -> completeDebug(args);
            case "nickname" -> completeNickname(args);
            default -> Optional.empty();
        };
    }

    private void handleNickName(final CommandSender sender, final String @NotNull ... args) {
        if (args.length < 2) return;
        if (args[1].equalsIgnoreCase("color")) {
            //bridge nickname color (cost/have)/set <color>
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase("cost")) {
                    if (noPermission(sender, "bridge.nickname.color.cost")) return;
                    if (sender instanceof Player player) {
                        String color = NicknameColorManager.getPlayerColor(player.getUniqueId(), false);
                        if (color == null) {
                            sendMessage(player, MessageType.YOUR_NICKNAME_IS_UNIQUE);
                            return;
                        }
                        sendMessage(
                                player,
                                MessageType.YOUR_NICKNAME_COLOR_COST,
                                String.valueOf(
                                        NicknameColorManager.getColorCost(color)
                                )
                        );
                    } else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                } else if (args[2].equalsIgnoreCase("have")) {
                    if (noPermission(sender, "bridge.nickname.color.have")) return;
                    if (sender instanceof Player player) {
                        String color = NicknameColorManager.getPlayerColor(player.getUniqueId(), false);
                        if (color == null) {
                            sendMessage(player, MessageType.YOUR_NICKNAME_COLOR,
                                    NicknameColorManager.getPlayerColor(player.getUniqueId(), true));
                            return;
                        }
                        sendMessage(
                                player,
                                MessageType.YOUR_NICKNAME_COLOR,
                                color);
                    } else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                }
            }
            //bridge nickname color set/(cost/have) <COLOR>/<PLAYERS>
            else if (args.length == 4) {
                if (args[2].equalsIgnoreCase("set")) {
                    if (!(sender instanceof Player player)) {
                        sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                        return;
                    }
                    if (noPermission(player, "bridge.nickname.color.set")) return;

                    String hex;
                    List<String> colorList = NicknameColorManager.getAllColorsName();

                    if (ColorCodes.isHexValid(args[3])) {
                        if (noPermission(sender, "bridge.nickname.color.set.hex")) return;
                        hex = args[3];
                    } else if (colorList.contains(args[3])) {
                        String group = NicknameColorManager.getColorGroup(args[3]);
                        if (
                                !player.hasPermission(String.format("bridge.nickname.%s.%s", group, args[3]))
                                        || !player.hasPermission(String.format("bridge.nickname.%s.*", group))
                                        || !player.hasPermission("bridge.nickname.color.set")) {
                            sendMessage(player, MessageType.NO_PERMISSION);
                            return;
                        }
                        hex = NicknameColorManager.getColorHex(args[3]);
                    } else {
                        sendMessage(player, MessageType.UNKNOWN_ARGUMENT, args[3]);
                        return;
                    }
                    Currency currency = new TABManager().getStars();
                    if (currency != null) {
                        int cost = NicknameColorManager.getHexColorCost(args[3]);
                        int have = currency.getCurrencyAmount(player.getUniqueId());
                        if (have < cost) {
                            sendMessage(
                                    player,
                                    MessageType.NOT_ENOUGH_STARS,
                                    String.valueOf(cost - have));
                            return;
                        }
                    }
                    NicknameColorManager.applyNicknameColor(player, hex, true);
                    sendMessage(player, MessageType.YOUR_NICKNAME_COLOR_CHANGED, args[3]);
                    //done!
                } else if (args[2].equalsIgnoreCase("cost")) {
                    if (noPermission(sender, "bridge.nickname.color.cost.other")) return;
                    Player player = PlayerConverter.getPlayer(args[3]);
                    if (player == null) {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                        return;
                    }

                    String color = NicknameColorManager.getPlayerColor(player.getUniqueId(), false);
                    if (color == null) {
                        sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_IS_UNIQUE, player.getName());
                        return;
                    }

                    sendMessage(
                            sender,
                            MessageType.OTHER_PLAYER_NICKNAME_COLOR_COST,
                            player.getName(),
                            String.valueOf(NicknameColorManager.getColorCost(color)));
                    //done!
                } else if (args[2].equalsIgnoreCase("have")) {
                    if (noPermission(sender, "bridge.nickname.color.have.other")) return;
                    Player player = PlayerConverter.getPlayer(args[3]);
                    if (player == null) sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                    else {
                        String hex = NicknameColorManager.getPlayerColor(player.getUniqueId(), true);
                        String color = null;
                        if (hex != null) {
                            color = NicknameColorManager.getColorNameByHex(hex);
                            if (color == null) color = hex;
                        }
                        sendMessage(
                                sender,
                                MessageType.OTHER_PLAYER_NICKNAME_COLOR,
                                player.getName(),
                                color
                        );
                    }
                }
            }
            //bridge nickname color set/replace color/<fromCOLOR> <PLAYERS>/<toCOLOR>
            else if (args.length == 5) {
                if (args[2].equalsIgnoreCase("replace")) {
                    if (noPermission(sender, "bridge.nickname.color.replace")) return;
                    if (NicknameColorManager.globallyReplaceColors(args[3], args[4]))
                        sendMessage(sender, MessageType.REPLACE_COLORS_SUCCESSFULLY, args[3], args[4]);
                    else sendMessage(sender, MessageType.REPLACE_COLORS_ERROR, args[3], args[4]);
                } else if (args[2].equalsIgnoreCase("set")) {
                    if (noPermission(sender, "bridge.nickname.color.set.other")) return;
                    String color;
                    if (ColorCodes.isHexValid(args[3])) {
                        if (noPermission(sender, "bridge.nickname.color.set.hex")) return;
                        color = args[3];
                    } else if (NicknameColorManager.getAllColorsName().contains(args[3]))
                        color = NicknameColorManager.getColorHex(args[3]);
                    else {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                        return;
                    }

                    Player p = PlayerConverter.getPlayer(args[4]);
                    if (p == null) {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[4]);
                        return;
                    }
                    NicknameColorManager.applyNicknameColor(p, color, true);
                    sendMessage(p, MessageType.YOUR_NICKNAME_COLOR_CHANGED, color);
                    sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_COLOR_CHANGED, p.getName(), color);
                }
            }
        } else if (args[1].equalsIgnoreCase("stars")) {

            //bridge nickname stars set <amount> <PLAYERS>
            if (args[2].equalsIgnoreCase("set")) {
                UUID uuid = null;
                if (args.length == 4) {
                    if (noPermission(sender, "bridge.nickname.stars.set")) return;
                    if (sender instanceof Player player) uuid = player.getUniqueId();
                    else {
                        sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                        return;
                    }
                } else if (args.length == 5) {
                    if (noPermission(sender, "bridge.nickname.stars.set.other")) return;
                    Player player = PlayerConverter.getPlayer(args[4]);
                    if (player == null) {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[4]);
                        return;
                    }
                    uuid = player.getUniqueId();
                }
                if (uuid != null) {
                    Currency currency = new TABManager().getStars();
                    if (currency != null) {
                        currency.setCurrency(uuid, Integer.parseInt(args[3]));
                        sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_STARS_CHANGED,
                                args[3],
                                args.length == 4 ? Config.getMessage(MessageType.SELF)
                                        : Objects.requireNonNull(PlayerConverter.getPlayer(uuid)).getName());
                    }
                } else sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args);
                //done!
            }

            //bridge nickname stars add <amount> <PLAYERS>
            else if (args[2].equalsIgnoreCase("add")) {
                Currency currency = new TABManager().getStars();
                if (currency == null) {
                    sendMessage(
                            sender,
                            MessageType.MODULE_STATE,
                            "UseStars",
                            Config.getMessage(MessageType.DISABLED));
                    return;
                }
                if (args.length == 4) {
                    if (noPermission(sender, "bridge.nickname.stars.add")) return;
                    if (sender instanceof Player player) {
                        int toAdd = Integer.parseInt(args[3], 10);
                        int have = currency.getCurrencyAmount(player.getUniqueId());
                        currency.setCurrency(player.getUniqueId(), have + toAdd);
                        sendMessage(player, MessageType.YOUR_NICKNAME_STARS_CHANGED, String.valueOf(have + toAdd));
                    } else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                } else if (args.length == 5) {
                    if (noPermission(sender, "bridge.nickname.stars.add.other")) return;
                    Player p = PlayerConverter.getPlayer(args[4]);
                    if (p == null) sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[4]);
                    else {
                        int toAdd = Integer.parseInt(args[3], 10);
                        int have = currency.getCurrencyAmount(p.getUniqueId());
                        currency.setCurrency(p.getUniqueId(), have + toAdd);
                        sendMessage(p, MessageType.YOUR_NICKNAME_STARS_CHANGED, String.valueOf(have + toAdd));
                        sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_STARS_CHANGED, String.valueOf(have + toAdd), p.getName());
                    }
                }
                //done!
            }

            //bridge nickname stars have <PLAYERS>
            else if (args[2].equalsIgnoreCase("have")) {
                Currency currency = new TABManager().getStars();
                if (currency == null) {
                    sendMessage(
                            sender,
                            MessageType.MODULE_STATE,
                            "UseStars",
                            Config.getMessage(MessageType.DISABLED));
                    return;
                }
                if (args.length == 3) {
                    if (noPermission(sender, "bridge.nickname.stars.have")) return;
                    if (sender instanceof Player player) {
                        sendMessage(
                                player,
                                MessageType.YOUR_NICKNAME_STARS,
                                String.valueOf(currency.getCurrencyAmount(player.getUniqueId()))
                        );
                    } else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                } else if (args.length == 4) {
                    if (noPermission(sender, "bridge.nickname.stars.have.other")) return;
                    Player p = PlayerConverter.getPlayer(args[3]);
                    if (p == null) sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                    else sendMessage(
                            sender,
                            MessageType.OTHER_PLAYER_NICKNAME_STARS,
                            p.getName(),
                            String.valueOf(currency.getCurrencyAmount(p.getUniqueId()))
                    );
                }
                //done!
            }
        } else sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args);
    }

    private @NotNull Optional<List<String>> completeNickname(final String @NotNull ... args) {
        // bridge nickname color/stars
        if (args.length == 2) return Optional.of(Arrays.asList("color", "stars"));
        // bridge nickname color (have/cost)/set/replace <PLAYERS>/<COLORS>/fromColor <PLAYERS>/toColor
        if (args[1].equalsIgnoreCase("color")) {
            if (args.length == 3) return Optional.of(Arrays.asList("have", "cost", "set", "replace"));
            else if (args.length == 4) {
                if (args[2].equalsIgnoreCase("have")
                        || args[2].equalsIgnoreCase("cost")) {
                    List<String> names = new ArrayList<>();
                    Bukkit.getOnlinePlayers().forEach((p) -> names.add(p.getName()));
                    return Optional.of(names);
                } else if (args[2].equalsIgnoreCase("set")) return Optional.of(NicknameColorManager.getAllColorsName());
                else if (args[2].equalsIgnoreCase("replace")) return Optional.of(List.of("#HEX"));
            } else if (args.length == 5) {
                if (args[2].equalsIgnoreCase("set")) {
                    List<String> names = new ArrayList<>();
                    Bukkit.getOnlinePlayers().forEach((p) -> names.add(p.getName()));
                    return Optional.of(names);
                } else if (args[2].equalsIgnoreCase("replace")) return Optional.of(List.of("#HEX"));
            }
        }
        // bridge nickname stars (set/add)/have <AMOUNT>/<PLAYERS
        else if (args[1].equalsIgnoreCase("stars")) {
            if (args.length == 3) return Optional.of(Arrays.asList("set", "add", "have"));
            else if (args.length == 4 && args[2].equalsIgnoreCase("have")) {
                List<String> names = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach((p) -> names.add(p.getName()));
                return Optional.of(names);
            }
        }
        return Optional.empty();
    }

    private void handleLanguage(final CommandSender sender, final String @NotNull ... args) {
        if (args.length == 1) {
            sendMessage(sender, MessageType.CURRENT_LANGUAGE, Config.getLanguage());
            return;
        }

        final String language = Config.getLanguage();

        if (Config.getLanguages().contains(args[1]) && args.length == 2) {
            if (language.equalsIgnoreCase(args[1])) {
                sendMessage(sender, MessageType.ALREADY_LANGUAGE, language);
                return;
            }

            try {
                Config.setLanguage(language);
            } catch (final IllegalArgumentException e) {
                sendMessage(sender, MessageType.NO_SUCH_LANGUAGE);
                return;
            }
            instance.getPluginConfig().set("settings.language", language);
            sendMessage(sender, MessageType.SET_LANGUAGE_SUCCESSFULLY, language);
            return;
        }
        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args);
    }

    private @NotNull Optional<List<String>> completeLanguage(final String @NotNull ... args) {
        if (args.length == 2) {
            return Optional.of(Config.getLanguages());
        }
        return Optional.of(new ArrayList<>());
    }

    private void handleDebug(final CommandSender sender, final String @NotNull ... args) {
        if (args.length == 1) {
            sendMessage(sender, MessageType.DEBUGGING,
                    debugHandler.isDebugging() ?
                            Config.getMessage(MessageType.ENABLED) :
                            Config.getMessage(MessageType.DISABLED));
            return;
        }

        final Boolean input = "true".equalsIgnoreCase(args[1]) ? Boolean.TRUE
                : "false".equalsIgnoreCase(args[1]) ? Boolean.FALSE : null;
        if (input != null && args.length == 2) {

            if (debugHandler.isDebugging() && input || !debugHandler.isDebugging() && !input) {
                sendMessage(sender, MessageType.ALREADY_DEBUGGING,
                        debugHandler.isDebugging() ?
                                Config.getMessage(MessageType.ENABLED) :
                                Config.getMessage(MessageType.DISABLED));
                return;
            }

            try {
                debugHandler.setDebugging(input);
            } catch (final IOException e) {
                sendMessage(sender, MessageType.SET_DEBUG_ERROR);
                LOG.warn("Could not save new debugging state to configuration file! " + e.getMessage(), e);
            }
            sendMessage(sender, MessageType.SET_DEBUG_SUCCESSFULLY,
                    debugHandler.isDebugging() ?
                            Config.getMessage(MessageType.ENABLED) :
                            Config.getMessage(MessageType.DISABLED));
            return;
        }
        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args);
    }

    private @NotNull Optional<List<String>> completeDebug(final String @NotNull ... args) {
        if (args.length == 2) {
            return Optional.of(Arrays.asList("true", "false"));
        }
        return Optional.of(new ArrayList<>());
    }

    private void sendMessage(final CommandSender sender, final MessageType msg) {
        sendMessage(sender, msg, (String[]) null);
    }

    private void sendMessage(final CommandSender sender, final MessageType msg, final String... variables) {
        if (sender instanceof Player player) {
            Config.sendMessage(player, msg, variables);
        } else {
            sender.sendMessage(Config.parseMessage(Config.getMessage(msg, variables)));
        }
    }

    private boolean noPermission(@NotNull CommandSender sender, String perm) {
        if (!sender.hasPermission(perm)) {
            sendMessage(sender, MessageType.NO_PERMISSION);
            return true;
        }
        return false;
    }
}
