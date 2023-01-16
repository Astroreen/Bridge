package velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import common.logger.BRLogger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import velocity.commands.GuildCommand;

import java.nio.file.Path;

@Plugin(authors = "Astroreen", id = "velocity", name = "Bridge",
        url = "www.anicloud.ru", description = "Makes your live much more interesting",
        //version to change
        version = "5.7.03")
public class BridgeVelocity {

    @Getter
    private static BridgeVelocity instance;
    @Getter
    private final ProxyServer proxy;
    @Getter
    private final Logger logger;
    private BRLogger log;
    private final Path dir;

    /**
     * Method where everything starts from.
     *
     * @param proxy this server
     * @param logger   logger
     * @param dir   path to directory
     */
    @Inject
    public BridgeVelocity(final @NotNull ProxyServer proxy, final @NotNull Logger logger, final @NotNull @DataDirectory Path dir) {
        this.proxy = proxy;
        this.logger = logger;
        this.dir = dir;
    }

    /**
     * A method when <b>you</b> want to get
     * <b>or somebody else</b> to be able to hook to API<br/><br/>
     * <i>(yours or somebody else's)</i>.
     *
     * @param event when velocity is on initialization phase
     */
    @Subscribe
    public void onProxyInitializationEvent(final @NotNull ProxyInitializeEvent event) {
        //moment where you can hook to instance
        instance = this;

        //registering logger
        this.log = BRLogger.create(logger);

        //registering commands
        new GuildCommand();
    }

    /**
     * Method for properly disabling parts of plugin.
     */
    @Subscribe
    public void onProxyShutdownEvent(final @NotNull ProxyShutdownEvent event){
    }
}