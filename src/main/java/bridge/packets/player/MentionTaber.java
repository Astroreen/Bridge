package bridge.packets.player;

import bridge.Bridge;
import bridge.compatibility.protocollib.ProtocolLibManager;
import bridge.listeners.ListenerManager;
import bridge.modules.Module;
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
import com.mojang.authlib.properties.Property;
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CustomLog(topic = "MentionTaber")
public class MentionTaber implements Module, Listener {
    private static Bridge plugin;
    private static String prefix;
    private static ProtocolManager manager;
    private static boolean isActive;
    private final static HashMap<UUID, UUID> uuids = new HashMap<>();
    private final static String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3MTQ3OTc5OTM5NCwKICAicHJvZmlsZUlkIiA6ICI3NDZmZTllY2EyNWM0ZWNlYjAxYTFjNDQxY2ZiZTAzZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJVU0VSX1ZBUEVWNCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yYjc4Zjg2ZDU3YWY0NWUwNjg0MjdjZTc1MGRkYmE3ZjNmZmJkYjJmZTE4MTNmNDE2OTRhODdjZjlhZWZlNzI2IgogICAgfQogIH0KfQ==";
    private final static String signature = "YLuRmmLiuWpXUcRTvhRDez+D6L1XwCSedtq8l46b7pwfNGjcGVAOrCvtjAl2vE1lyR+p8bB04zueULG8ucTt7gatCER/dMyrKmEz0QGRrplhBZtBYB2i+UsdM3iTZWaz6iLK7GpUneh17au2chOZtmQnq6aeG4y/vKV4QjsutWk7X53vBvvtWZotLzWESaIHBil46+pB1u2pJrqMCAcE3wXyzyeeAom4t+txz4U8PJ1CM2d6TBz03JvPn6++WOiYqqLHTwOet8wWTNVJwHrpeR/0ij5as2YYQVeO/ZpmhAjvSXvbtPinvaC2jkhxR1CkzFeFKob9SezVkCGC5129gfa9bDicAHYUMdJgdJHvrOFr4uL3npcgkovTrPn6xVQ6FkvNwBmvpgotKyKKKsuNjvPhiECv6e0vHJeVXaOYTDqWAnB2I0rfVt8RWI+5TOvLPb7qXfd5hx7enI79klDiRRcFda9yl5doutq5UKJUY7cyOdoj7XEyRBjan43/+zHGdjWJc6h/k60PK4PkPFoc1p9XGFOWScxhSy1J0ELOruPLIRP2EZQIHUy2CF2JfyNY5ise9NkYRr3toQ/HxVLqcu6VNGadVlw0hLdrmHD5/RQAmX1RhdXpipEm/ANCIq1JK7YAjSuEFd+jWGXyx205bg/rsvinCV5Bjd7YMxWnGHA=";


    @Override
    public boolean start(final @NotNull Bridge plugin) {
        MentionTaber.plugin = plugin;
        manager = ProtocolLibrary.getProtocolManager();
        reload();
        ListenerManager.register("MentionTaber", this);
        isActive = true;
        return true;
    }

    @Override
    public void reload() {
        final String symbol = plugin.getPluginConfig().getString("setting.modules.mention-taber.prefix", "@");
        if(symbol.length() > 1) {
            LOG.error("Prefix cannot be more that one symbol. Using default '@' symbol.");
            prefix = "@";
        } else prefix = symbol;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            //remove every player mention
            for(final @NotNull Player player : Bukkit.getOnlinePlayers()){
                final PacketContainer packet = getPlayerPacket(player, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                if(packet == null) return;
                manager.broadcastServerPacket(packet);
            }
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
                } catch (InvocationTargetException e) {
                    LOG.error("There was error after trying to send REMOVE_PLAYER packets to " + player.getName(), e);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
            } catch (InvocationTargetException e) {
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
               } catch (InvocationTargetException e) {
                   LOG.error("There was error after trying to send ADD_PLAYER packets to " + player.getName(), e);
               }
           }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(final @NotNull PlayerQuitEvent event){
        final PacketContainer packet = getPlayerPacket(event.getPlayer(), EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        if(packet == null) return;
        manager.broadcastServerPacket(packet);
        uuids.remove(event.getPlayer().getUniqueId());
    }

    private static @Nullable PacketContainer getPlayerPacket(final @NotNull Player player, final @NotNull EnumWrappers.PlayerInfoAction action){
        if(player.getName().length() == 16) return null;
        final String name = prefix + player.getName();

        if(!uuids.containsKey(player.getUniqueId()))
            uuids.put(player.getUniqueId(), UUID.randomUUID());
        final UUID uuid = uuids.get(player.getUniqueId());

        //creating packet
        final PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        final WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(container);
        final GameProfile profile = new GameProfile(uuid, name);
        profile.getProperties().put("textures", new Property("textures", texture, signature));
        final WrappedGameProfile prof = WrappedGameProfile.fromHandle(profile);
        final PlayerInfoData data = new PlayerInfoData(prof, 10, EnumWrappers.NativeGameMode.SPECTATOR, WrappedChatComponent.fromText(name));

        //setting data;
        info.setAction(action);
        info.setData(List.of(data));
        return container;
    }

    @Override
    public void disable() {
        isActive = false;
    }

    @Override
    public boolean isConditionsMet() {
        return ProtocolLibManager.isActive();
    }

    @Override
    public boolean active() {
        return isActive;
    }
}
