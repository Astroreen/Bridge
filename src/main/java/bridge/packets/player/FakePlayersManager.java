package bridge.packets.player;

import bridge.Bridge;
import bridge.compatibility.protocollib.ProtocolLibManager;
import bridge.config.ConfigurationFile;
import bridge.listeners.ListenerManager;
import bridge.modules.Module;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class FakePlayersManager implements Module, Listener {
    private static Bridge plugin;
    private static ConfigurationFile config;
    private static boolean isActive;
    private static ProtocolManager manager;

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        FakePlayersManager.plugin = plugin;
        FakePlayersManager.config = plugin.getPluginConfig();
        manager = ProtocolLibrary.getProtocolManager();
        ListenerManager.register("FakePlayersManager", this);
        isActive = true;
        return true;
    }

    @Override
    public void reload() {
        //resending packets of npc existence
        Bukkit.getOnlinePlayers().forEach(FakePlayersManager::removePlayer);
        Bukkit.getOnlinePlayers().forEach(FakePlayersManager::addPlayer);
    }

    private static void addPlayer(final @NotNull Player player) {
        final PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);

    }

    private static void removePlayer(final @NotNull Player player){

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent (final @NotNull PlayerJoinEvent event){
        final Player player = event.getPlayer();
        addPlayer(player);
    }

    @Override
    public void disable() {
        isActive = false;
    }

    @Override
    public boolean isConditionsMet() {
        return config.getBoolean("settings.modules.fake-players", true)
                && ProtocolLibManager.isActive();
    }

    @Override
    public boolean active() {
        return isActive;
    }
}
