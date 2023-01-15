package velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import common.Permission;
import org.jetbrains.annotations.NotNull;
import velocity.BridgeVelocity;

import java.util.List;
import java.util.Optional;

public class GuildCommand extends VelocityCommand {

    private static final BridgeVelocity plugin = BridgeVelocity.getInstance();

    public GuildCommand() {
        super("guild", Permission.COMMAND_GUILD, new String[]{"g"});
    }

    @Override
    public boolean onCommand(final @NotNull CommandSource source, final @NotNull String alias, final String @NotNull ... args) {
        return false;
    }

    @Override
    public void helpInfo(final @NotNull CommandSource source, final @NotNull String alias, final String @NotNull ... args) {
        //Empty
    }

    @Override
    public Optional<List<String>> simpleTabComplete(final @NotNull CommandSource source, final @NotNull String alias, final String @NotNull ... args) {
        return Optional.empty();
    }
}
