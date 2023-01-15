package velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import velocity.commands.GuildCommand;

import java.nio.file.Path;

@Plugin(authors = "Astroreen", id = "velocity", name = "Bridge",
        url = "www.anicloud.ru", description = "Makes your live much more interesting",
        //version to change
        version = "5.7.01")
public class BridgeVelocity {

    @Getter
    private static BridgeVelocity instance;
    @Getter
    private final ProxyServer proxy;
    @Getter
    private final Logger log;
    private final Path dir;

    /**
     * Method where everything starts from.
     *
     * @param proxy this server
     * @param log   logger
     * @param dir   path to directory
     */
    @Inject
    public BridgeVelocity(final @NotNull ProxyServer proxy, final @NotNull Logger log, final @NotNull @DataDirectory Path dir) {
        this.proxy = proxy;
        this.log = log;
        this.dir = dir;
    }

    /**
     * A method when you want to get
     * or somebody to be able to hook to API
     * (your or somebody else's).
     *
     * @param event when velocity is on initialization phase
     */
    @Subscribe
    public void onProxyInitialization(final @NotNull ProxyInitializeEvent event) {
        instance = this;

        //registering commands
        new GuildCommand();
    }
}
