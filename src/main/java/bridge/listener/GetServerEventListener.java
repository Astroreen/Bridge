package bridge.listener;

import bridge.Bridge;
import bridge.event.GetServerEvent;
import lombok.CustomLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@CustomLog
public class GetServerEventListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGetServerEvent(final @NotNull GetServerEvent event){
        final String name = event.getServerName();
        LOG.debug("Got this server name from BungeeCord! Writing it in server.properties as \"server-name: " + name + "\"");
        Bridge plugin = Bridge.getInstance();

        Path path = Paths.get(plugin.getDataFolder().getParentFile().getAbsolutePath()).getParent().resolve("server.properties");
        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++){
                final String line = lines.get(i);
                if(line.matches("server-name")) {
                    lines.remove(i);
                    break;
                }
            }
            lines.add("server-name: " + name);
            Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOG.error("There was an exception writing a server-name into server.properties file.", e);
        }
    }

}
