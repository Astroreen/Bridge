package bridge.packets.player;

import bridge.Bridge;
import bridge.compatibility.protocollib.ProtocolLibManager;
import bridge.listener.ListenerManager;
import common.IModule;
import bridge.utils.ProtocolLibUtils;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.mojang.authlib.GameProfile;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@CustomLog(topic = "MentionTaber")
public class MentionTaber implements IModule, Listener {
    private static Bridge plugin;
    private static String prefix;
    private static ProtocolManager manager;
    private final static HashMap<UUID, UUID> uuids = new HashMap<>();

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        MentionTaber.plugin = plugin;
        manager = ProtocolLibrary.getProtocolManager();
        reload();
        ListenerManager.register(getName(), this);
        return true;
    }

    @Override @SuppressWarnings("deprecation")
    public void reload() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            //remove every player mention
            for(final @NotNull Player player : Bukkit.getOnlinePlayers()){
                final PacketContainer packet = getPlayerPacket(player, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                if(packet == null) return;
                manager.broadcastServerPacket(packet);
            }

            final String symbol = plugin.getPluginConfig().getString("modules.mention-taber-symbol", "@");
            if(symbol.length() > 1) {
                LOG.error("Prefix cannot be more that one symbol. Using default '@' symbol.");
                prefix = "@";
            } else prefix = symbol;

            //add every player mention
            for(final @NotNull Player player : Bukkit.getOnlinePlayers()){
                final PacketContainer packet1 = getPlayerPacket(player, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
                if(packet1 == null) return;
                manager.broadcastServerPacket(packet1);
                //hide from its own
                final PacketContainer packet2 = getPlayerPacket(player, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                if(packet2 == null) return;
                try {
                    manager.sendServerPacket(player, packet2);
                } catch (Exception e) {
                    LOG.error("There was error after trying to send REMOVE_PLAYER packets to " + player.getName(), e);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST) @SuppressWarnings("deprecation")
    public void onPlayerJoinEvent(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        //broadcast to everyone player his mention
        final PacketContainer packet1 = getPlayerPacket(player, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        if(packet1 == null) return;
        manager.broadcastServerPacket(packet1);

        //delete from player his own mention
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            final PacketContainer packet2 = getPlayerPacket(player, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
            if(packet2 == null) return;
            try {
                manager.sendServerPacket(player, packet2);
            } catch (Exception e) {
                LOG.error("There was error after trying to send REMOVE_PLAYER packets to " + player.getName(), e);
            }
        }, 20L * 3); //delete packet after 3 seconds

        //broadcast every mention to player
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           for(final @NotNull Player p : Bukkit.getOnlinePlayers()){
               final PacketContainer packet = getPlayerPacket(p, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
               if(packet == null) return;
               try {
                   manager.sendServerPacket(player, packet);
               } catch (Exception e) {
                   LOG.error("There was error after trying to send ADD_PLAYER packets to " + player.getName(), e);
               }
           }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST) @SuppressWarnings("deprecation")
    public void onPlayerQuitEvent(final @NotNull PlayerQuitEvent event){
        final PacketContainer packet = getPlayerPacket(event.getPlayer(), EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        if(packet == null) return;
        manager.broadcastServerPacket(packet);
        uuids.remove(event.getPlayer().getUniqueId());
    }

    @SuppressWarnings("deprecation")
    private static @Nullable PacketContainer getPlayerPacket(final @NotNull Player player, final @NotNull EnumWrappers.PlayerInfoAction action){
        if(player.getName().length() == 16) return null;
        final String name = prefix + player.getName();

        if(!uuids.containsKey(player.getUniqueId()))
            uuids.put(player.getUniqueId(), UUID.randomUUID());
        final UUID uuid = uuids.get(player.getUniqueId());

        //making new GameProfile
        final GameProfile profile = new GameProfile(uuid, name);
        profile.getProperties().put("textures", ProtocolLibUtils.getUnknownProperty());
        final WrappedGameProfile prof = WrappedGameProfile.fromHandle(profile);
        final PlayerInfoData data = new PlayerInfoData(prof, 10, EnumWrappers.NativeGameMode.SPECTATOR, WrappedChatComponent.fromText(name));

        //handle REMOVE_PLAYER situation
        if(action.equals(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER))
            return ProtocolLibUtils.removePlayerInfoData(Collections.singletonList(data), 10).get(0);

        //creating packet
        final PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        final WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(packet);

        //setting data;
        info.setData(new ArrayList<>(Collections.singleton(data)));
        info.setAction(action);
        return packet;
    }

    @Override
    public void disable() {
        //Empty
    }

    @Override
    public boolean isConditionsMet() {
        return ProtocolLibManager.isActive();
    }
}
