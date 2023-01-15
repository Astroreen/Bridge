package velocity.commands;

import com.velocitypowered.api.command.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import velocity.BridgeVelocity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * An abstract class to easily make commands.
 *
 * @author Astroreen
 */
public abstract class VelocityCommand implements SimpleCommand {

    /**
     * Get command main name.
     */
    @Getter
    @NotNull
    private final String name;

    /**
     * Get permission to check if Player can use this command.
     * <br/><br/>
     * <b>Note:</b> If permission in constructor is null, this means that everybody can use this command.<br/>
     * Also, in order to make a more complex rule, you'll need to override {@link #hasPermission} method.
     */
    @Getter
    @Nullable
    private final String permission;

    /**
     * Get all command aliases. May be null.
     */
    @Getter
    @Nullable
    private final String[] aliases;

    public VelocityCommand(final @NotNull String name) {
        this.name = name;
        this.permission = null;
        this.aliases = null;
        //register command
        create(this, name, (String[]) null);
    }

    public VelocityCommand(final @NotNull String name, final @Nullable String permission) {
        this.name = name;
        this.permission = permission;
        this.aliases = null;
        //register command
        create(this, name, (String[]) null);
    }

    public VelocityCommand(final @NotNull String name, final String @Nullable [] aliases) {
        this.name = name;
        this.permission = null;
        this.aliases = aliases == null || aliases.length == 0 ? null : aliases;
        //register command
        create(this, name, aliases);
    }

    public VelocityCommand(final @NotNull String name, final @Nullable String permission, final String @Nullable [] aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases == null || aliases.length == 0 ? null : aliases;
        //register command
        create(this, name, aliases);
    }

    /**
     * Does Player have permission to use this command.
     * <br/><br/>
     * <b>Note:</b> If permission in constructor is null, this means that everybody can use this command.
     *
     * @param invocation the invocation context
     * @return true, if does.
     */
    @Override
    public boolean hasPermission(final Invocation invocation) {
        if (permission == null) return true;
        else return invocation.source().hasPermission(getPermission());
    }

    @Override
    public void execute(final @NotNull Invocation invocation) {
        if (!onCommand(invocation.source(), invocation.alias(), invocation.arguments()))
            helpInfo(invocation.source(), invocation.alias(), invocation.arguments());
    }

    @Override
    public List<String> suggest(final @NotNull Invocation invocation) {
        final Optional<List<String>> completions = this.simpleTabComplete(invocation.source(), invocation.alias(), invocation.arguments());
        if (completions.isEmpty()) {
            return null;
        }
        final List<String> out = new ArrayList<>();
        final String lastArg = invocation.arguments()[invocation.arguments().length - 1];
        for (final String completion : completions.get()) {
            if (lastArg == null || lastArg.matches(" *") || completion.toLowerCase(Locale.ROOT).startsWith(lastArg.toLowerCase(Locale.ROOT))) {
                out.add(completion);
            }
        }
        return out;
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final @NotNull Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> suggest(invocation));
    }

    /**
     * Like regular tab-completer, but it works asynchronously.
     * <br/><br/>
     * <b>Note:</b> In order to make sync and async methods,
     * override {@link #suggest} and {@link #suggestAsync} methods.
     *
     * @param source command source
     * @param alias  command name or one of the aliases
     * @param args   arguments
     * @return an optional string list
     */
    public abstract Optional<List<String>> simpleTabComplete(final @NotNull CommandSource source, final @NotNull String alias, final @NotNull String... args);

    /**
     * When player writes command, this method executed
     *
     * @param source command source
     * @param alias  command name or one of the aliases
     * @param args   arguments
     * @return false, to show player help information.
     */
    public abstract boolean onCommand(final @NotNull CommandSource source, final @NotNull String alias, final String @NotNull ... args);

    /**
     * Executed when an {@link #onCommand} method returns false.
     *
     * @param source command source
     * @param alias  command name or one of the aliases
     * @param args   arguments
     */
    public abstract void helpInfo(final @NotNull CommandSource source, final @NotNull String alias, final String @NotNull ... args);

    /**
     * Registers a command
     *
     * @param command command instance
     * @param name    command name
     * @param aliases command aliases, can be null
     */
    private void create(final @NotNull Command command, final @NotNull String name, final String... aliases) {
        final CommandManager manager = BridgeVelocity.getInstance().getProxy().getCommandManager();
        //creating a command builder
        final CommandMeta.Builder meta = manager.metaBuilder(name).plugin(BridgeVelocity.getInstance());
        //setting aliases
        if (aliases != null && aliases.length > 0) meta.aliases(aliases);
        manager.register(meta.build(), command);
    }
}
