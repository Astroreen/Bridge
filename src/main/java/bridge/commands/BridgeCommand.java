package bridge.commands;

import bridge.Bridge;
import bridge.MessageType;
import bridge.compatibility.tab.NicknameColorManager;
import bridge.compatibility.tab.TABManager;
import bridge.config.Config;
import bridge.modules.logger.DebugHandlerConfig;
import bridge.utils.PlayerConverter;
import lombok.CustomLog;
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
                case "nickname", "nick" -> handleNickName(sender, args);
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
            case "nickname" -> completeNickName(args);
            default -> Optional.empty();
        };
    }

    private void handleNickName(final CommandSender sender, final String @NotNull ... args) {
        if (args[1].equalsIgnoreCase("color")) {
            //bridge nickname color
            if (args.length == 2) {
                if (sender instanceof Player player) {
                    NicknameColorManager.PlayerColor info = NicknameColorManager.getPlayerInfo(player.getUniqueId());
                    sendMessage(
                            player,
                            MessageType.YOUR_CURRENT_NICKNAME_COLOR,
                            info.color(),
                            info.hex());
                    return;
                }
            }
            //bridge nickname color (cost/have)/<PLAYERS>
            if (args.length == 3) {

            }

            //bridge nickname color set/(cost/have) <COLOR>/<PLAYERS>
            if (args.length == 4) {

            }
            //bridge nickname color replace <fromCOLOR> <toCOLOR>
            if (args.length == 5) {

            }
        }

        if (args[1].equalsIgnoreCase("stars")) {
            //bridge nickname stars
            if (args.length == 2) {

            }
            //bridge nickname stars <PLAYERS>
            if (args.length == 3) {

            }
            //bridge nickname stars (set/add)/have <amount>/<PLAYERS>

            //bridge nickname stars set <amount> <PLAYERS>
            if (args[2].equalsIgnoreCase("set")) {
                if (sender.hasPermission("bridge.nickname.stars.set")) {
                    UUID uuid = null;
                    if (args.length == 4) {
                        if (sender instanceof Player player) uuid = player.getUniqueId();
                        else sendMessage(sender, MessageType.NEED_TO_BE_PLAYER);
                    }
                    if (args.length == 5) {
                        Player player = PlayerConverter.getPlayer(args[4]);
                        if (player == null) {
                            sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args[4]);
                            return;
                        }
                        uuid = player.getUniqueId();
                    }
                    if (uuid != null) new TABManager().getStars().setCurrency(uuid, Integer.parseInt(args[3]));
                } else sendMessage(sender, MessageType.NO_PERMISSION);
            }

            if (args[2].equalsIgnoreCase("add")) {
                //TODO get player stars, add stars, save async
            }
        }
        sendMessage(sender, MessageType.UNKNOWN_ARGUMENT, args);
    }

    private @NotNull Optional<List<String>> completeNickName(final String @NotNull ... args) {
        // bridge nickname color/stars
        if (args.length == 2) return Optional.of(Arrays.asList("color", "stars"));
        // bridge nickname color set/replace
        if (args[1].equalsIgnoreCase("color")) {
            if (args.length == 3) return Optional.of(Arrays.asList("set", "replace"));
            if (args.length == 4) return Optional.of(NicknameColorManager.getAllColorsName());
        }
        // bridge nickname stars set/add/have
        if (args[1].equalsIgnoreCase("stars") && args.length == 3)
            return Optional.of(Arrays.asList("set", "add", "have"));

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
}
