package bridge.commands;

import bridge.Bridge;
import bridge.MessageType;
import bridge.compatibility.Compatibility;
import bridge.compatibility.tab.NicknameManager;
import bridge.compatibility.tab.TABManager;
import bridge.config.Config;
import bridge.modules.Module;
import bridge.modules.ModuleManager;
import bridge.modules.Currency;
import bridge.modules.logger.DebugHandlerConfig;
import bridge.modules.permissions.Permission;
import bridge.modules.permissions.PermissionManager;
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
    private final PermissionManager permManager;

    public BridgeCommand() {
        final PluginCommand command = instance.getCommand("bridge");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
        permManager = instance.getPermManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if ("bridge".equalsIgnoreCase(cmd.getName())) {
            LOG.debug("Executing /bridge command for user " + sender.getName()
                    + " with arguments: " + Arrays.toString(args));
            // if the command is empty, display help message
            if (args.length == 0) {
                return true;
            }

            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "language", "lang" -> handleLanguage(sender, args);
                case "version", "ver", "v" ->
                        sendMessage(sender, MessageType.VERSION, instance.getDescription().getVersion());
                case "debug" -> handleDebug(sender, args);
                case "reload", "rl" -> {
                    if (noPermission(sender, Permission.COMMAND_RELOAD)) return true;
                    //just reloading
                    instance.reload();
                    sendMessage(sender, MessageType.RELOADED);
                }
                case "nickname", "nick" -> {
                    if (!Compatibility.getHooked().contains("TAB"))
                        sendMessage(sender, MessageType.PLUGIN_DISABLED, "TAB");
                    final Module module = ModuleManager.getModule("TAB");
                    if (module == null || !module.active())
                        sendMessage(sender, MessageType.MODULE_STATE, "ColorNickname", Config.getMessage(MessageType.DISABLED));
                    handleNickName(sender, args);
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
        if(noPermission(sender, Permission.COMMAND_NICKNAME)) return;
        NicknameManager manager = TABManager.getManager();
        if (manager == null) return;
        if (args.length < 2) {
            sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args);
            return;
        }
        if (args[1].equalsIgnoreCase("color")) {
            //bridge nickname color (cost/have)/set <color>
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase("cost")) {
                    if (noPermission(sender, Permission.NICKNAME_COLOR_COST_OWN)) return;
                    if (sender instanceof Player player) {
                        NicknameManager.PlayerColor info = manager.getPlayerInfo(player.getUniqueId());
                        if (info == null) return;
                        if (!manager.getAllColorsName().contains(info.name())) {
                            sendMessage(player, MessageType.YOUR_NICKNAME_IS_UNIQUE);
                            return;
                        }
                        sendMessage(
                                player,
                                MessageType.YOUR_NICKNAME_COLOR_COST,
                                String.valueOf(manager.getColorCost(info.name()))
                        );
                    } else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                } else if (args[2].equalsIgnoreCase("have")) {
                    if (noPermission(sender, Permission.NICKNAME_COLOR_HAVE_OWN)) return;
                    if (sender instanceof Player player) {
                        NicknameManager.PlayerColor info = manager.getPlayerInfo(player.getUniqueId());
                        if(info == null) {
                            sendMessage(player, MessageType.YOUR_NICKNAME_IS_UNIQUE);
                            return;
                        }
                        if(info.name() == null) {
                            if(permManager.havePermission(player, Permission.NICKNAME_COLOR_SET_HEX)){
                                sendMessage(sender, MessageType.YOUR_NICKNAME_COLOR,
                                        info.gradient());
                                return;
                            }
                            sendMessage(player, MessageType.YOUR_NICKNAME_IS_UNIQUE);
                            return;
                        }
                        sendMessage(
                                player,
                                MessageType.YOUR_NICKNAME_COLOR,
                                info.name());
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
                    if (noPermission(player, Permission.NICKNAME_COLOR_SET_OWN)) return;

                    final String gradient;
                    List<String> colorList = manager.getAllColorsName();

                    if (ColorCodes.isHexValid(args[3])) {
                        if (noPermission(sender, Permission.NICKNAME_COLOR_SET_HEX)) return;
                        gradient = args[3] + ">" + args[3];
                    } else if (manager.isGradient(args[3])) gradient = args[3];
                    else if (colorList.contains(args[3])) gradient = manager.getGradient(args[3]);
                    else {
                        sendMessage(player, MessageType.UNKNOWN_ARGUMENT, args[3]);
                        return;
                    }
                    Currency currency = TABManager.getStars();
                    if (currency != null) {
                        Integer cost = manager.getColorCost(args[3]);
                        if (cost == null) {
                            sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                            return;
                        }
                        int have = currency.getCurrencyAmount(player.getUniqueId());
                        if (have < cost) {
                            sendMessage(
                                    player,
                                    MessageType.NOT_ENOUGH_STARS,
                                    String.valueOf(cost - have));
                            return;
                        }
                    }
                    manager.applyColor(player, gradient, true);
                    sendMessage(player, MessageType.YOUR_NICKNAME_COLOR_CHANGED, args[3]);
                    //done!
                } else if (args[2].equalsIgnoreCase("cost")) {
                    if (noPermission(sender, Permission.NICKNAME_COLOR_COST_OTHER)) return;
                    Player player = PlayerConverter.getPlayer(args[3]);
                    if (player == null) {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                        return;
                    }

                    NicknameManager.PlayerColor info = manager.getPlayerInfo(player.getUniqueId());
                    if (info == null) return;
                    if (!manager.getAllColorsName().contains(info.name())) {
                        sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_IS_UNIQUE, player.getName());
                        return;
                    }

                    sendMessage(
                            sender,
                            MessageType.OTHER_PLAYER_NICKNAME_COLOR_COST,
                            player.getName(),
                            String.valueOf(info.cost()));
                    //done!
                } else if (args[2].equalsIgnoreCase("have")) {
                    if (noPermission(sender, Permission.NICKNAME_COLOR_HAVE_OTHER)) return;
                    Player player = PlayerConverter.getPlayer(args[3]);
                    if (player == null) sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                    else {
                        NicknameManager.PlayerColor info = manager.getPlayerInfo(player.getUniqueId());
                        if (info == null) {
                            sendMessage(player, MessageType.YOUR_NICKNAME_IS_UNIQUE);
                            return;
                        }
                        if (info.name() == null) {
                            if (permManager.havePermission(player, Permission.NICKNAME_COLOR_SET_HEX)) {
                                sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_COLOR,
                                        player.getName(), info.gradient());
                                return;
                            }
                            sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_IS_UNIQUE);
                            return;
                        }
                        sendMessage(
                                sender,
                                MessageType.OTHER_PLAYER_NICKNAME_COLOR,
                                player.getName(),
                                info.name());
                    }
                }
            }
            //bridge nickname color set/replace color/<fromCOLOR> <PLAYERS>/<toCOLOR>
            else if (args.length == 5) {
                if (args[2].equalsIgnoreCase("replace")) {
                    if (noPermission(sender, Permission.NICKNAME_COLOR_REPLACE)) return;
                    if (manager.globallyReplaceColors(args[3], args[4]))
                        sendMessage(sender, MessageType.REPLACE_COLORS_SUCCESSFULLY, args[3], args[4]);
                    else sendMessage(sender, MessageType.REPLACE_COLORS_ERROR, args[3], args[4]);
                } else if (args[2].equalsIgnoreCase("set")) {
                    if (noPermission(sender, Permission.NICKNAME_COLOR_SET_OTHER)) return;
                    final String gradient;
                    String ColorName = null;
                    if (ColorCodes.isHexValid(args[3])) {
                        if (noPermission(sender, Permission.NICKNAME_COLOR_SET_HEX)) return;
                        gradient = args[3] + ">" + args[3];
                    } else if (manager.isGradient(args[3])) gradient = args[3];
                    else if (manager.getAllColorsName().contains(args[3])) {
                        ColorName = args[3];
                        gradient = manager.getGradient(args[3]);
                    } else {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[3]);
                        return;
                    }

                    Player p = PlayerConverter.getPlayer(args[4]);
                    if (p == null) {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[4]);
                        return;
                    }
                    manager.applyColor(p, gradient, true);
                    sendMessage(p, MessageType.YOUR_NICKNAME_COLOR_CHANGED, ColorName == null ? gradient : ColorName);
                    sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_COLOR_CHANGED, p.getName(), ColorName == null ? gradient : ColorName);
                }
            }
        } else if (args[1].equalsIgnoreCase("stars")) {

            //bridge nickname stars set <amount> <PLAYERS>
            if (args[2].equalsIgnoreCase("set")) {
                UUID uuid = null;
                if (args.length == 4) {
                    if (noPermission(sender, Permission.NICKNAME_STARS_SET_OWN)) return;
                    if (sender instanceof Player player) uuid = player.getUniqueId();
                    else {
                        sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                        return;
                    }
                } else if (args.length == 5) {
                    if (noPermission(sender, Permission.NICKNAME_STARS_SET_OTHER)) return;
                    Player player = PlayerConverter.getPlayer(args[4]);
                    if (player == null) {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[4]);
                        return;
                    }
                    uuid = player.getUniqueId();
                }
                if (uuid != null) {
                    Currency currency = TABManager.getStars();
                    if (currency == null) {
                        sendMessage(sender, MessageType.MODULE_STATE, "UseMoney", Config.getMessage(MessageType.DISABLED));
                        return;
                    }
                    currency.setCurrency(uuid, Integer.parseInt(args[3]));
                    Player player = PlayerConverter.getPlayer(uuid);
                    if (player == null) {
                        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[4]);
                        return;
                    }
                    sendMessage(sender, MessageType.OTHER_PLAYER_NICKNAME_STARS_CHANGED,
                            args[3],
                            args.length == 4 ? Config.getMessage(MessageType.SELF)
                                    : player.getName());

                } else sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args);
                //done!
            }

            //bridge nickname stars add <amount> <PLAYERS>
            else if (args[2].equalsIgnoreCase("add")) {
                Currency currency = TABManager.getStars();
                if (currency == null) {
                    sendMessage(
                            sender,
                            MessageType.MODULE_STATE,
                            "UseStars",
                            Config.getMessage(MessageType.DISABLED));
                    return;
                }
                if (args.length == 4) {
                    if (noPermission(sender, Permission.NICKNAME_STARS_ADD_OWN)) return;
                    if (sender instanceof Player player) {
                        int toAdd = Integer.parseInt(args[3], 10);
                        int have = currency.getCurrencyAmount(player.getUniqueId());
                        currency.setCurrency(player.getUniqueId(), have + toAdd);
                        sendMessage(player, MessageType.YOUR_NICKNAME_STARS_CHANGED, String.valueOf(have + toAdd));
                    } else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                } else if (args.length == 5) {
                    if (noPermission(sender, Permission.NICKNAME_STARS_ADD_OTHER)) return;
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
                Currency currency = TABManager.getStars();
                if (currency == null) {
                    sendMessage(
                            sender,
                            MessageType.MODULE_STATE,
                            "UseStars",
                            Config.getMessage(MessageType.DISABLED));
                    return;
                }
                if (args.length == 3) {
                    if (noPermission(sender, Permission.NICKNAME_STARS_HAVE_OWN)) return;
                    if (sender instanceof Player player) {
                        sendMessage(
                                player,
                                MessageType.YOUR_NICKNAME_STARS,
                                String.valueOf(currency.getCurrencyAmount(player.getUniqueId()))
                        );
                    } else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                } else if (args.length == 4) {
                    if (noPermission(sender, Permission.NICKNAME_STARS_HAVE_OTHER)) return;
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
                } else if (args[2].equalsIgnoreCase("set")) {
                    NicknameManager manager = TABManager.getManager();
                    if (manager == null) return Optional.empty();
                    return Optional.of(manager.getAllColorsName());
                } else if (args[2].equalsIgnoreCase("replace")) return Optional.of(List.of("#HEX"));
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
        if(noPermission(sender, Permission.COMMAND_LANGUAGE)) return;
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
        if(noPermission(sender, Permission.COMMAND_DEBUG)) return;
        if (args.length == 1) {
            sendMessage(sender, MessageType.DEBUGGING,
                    DebugHandlerConfig.isDebugging() ?
                            Config.getMessage(MessageType.ENABLED) :
                            Config.getMessage(MessageType.DISABLED));
            return;
        }

        final Boolean input = "true".equalsIgnoreCase(args[1]) ? Boolean.TRUE
                : "false".equalsIgnoreCase(args[1]) ? Boolean.FALSE : null;
        if (input != null && args.length == 2) {

            if (DebugHandlerConfig.isDebugging() && input || !DebugHandlerConfig.isDebugging() && !input) {
                sendMessage(sender, MessageType.ALREADY_DEBUGGING,
                        DebugHandlerConfig.isDebugging() ?
                                Config.getMessage(MessageType.ENABLED) :
                                Config.getMessage(MessageType.DISABLED));
                return;
            }

            try {
                DebugHandlerConfig.setDebugging(input);
            } catch (final IOException e) {
                sendMessage(sender, MessageType.SET_DEBUG_ERROR);
                LOG.warn("Could not save new debugging state to configuration file! " + e.getMessage(), e);
            }
            sendMessage(sender, MessageType.SET_DEBUG_SUCCESSFULLY,
                    DebugHandlerConfig.isDebugging() ?
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

    private boolean noPermission(@NotNull CommandSender sender, @NotNull Permission perm) {
        if(sender instanceof Player player){
            if(!permManager.havePermission(player, perm)){
                sendMessage(sender, MessageType.NO_PERMISSION);
                return true;
            }
        } else if (!sender.hasPermission(perm.perm)) {
            sendMessage(sender, MessageType.NO_PERMISSION);
            return true;
        }
        return false;
    }
}
