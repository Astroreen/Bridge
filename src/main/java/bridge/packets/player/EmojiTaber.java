package bridge.packets.player;

import bridge.Bridge;
import bridge.compatibility.protocollib.ProtocolLibManager;
import bridge.config.ConfigurationFile;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@CustomLog(topic = "EmojiTaber")
public class EmojiTaber implements Module, Listener {
    private static ConfigurationFile config;
    private static boolean isActive;
    private static ProtocolManager manager;
    private final static HashMap<String, UUID> fakePlayers = new HashMap<>();
    private final static Set<UUID> uuids = new HashSet<>();
    //skin by name/word: https://ru.namemc.com/
    //search for uuid: https://minecraftuuid.com/
    //https://sessionserver.mojang.com/session/minecraft/profile/{Trimmed uuid}?unsigned=false
    private final static String texture = "ewogICJ0aW1lc3RhbXAiIDogMTY3MTQ3OTc5OTM5NCwKICAicHJvZmlsZUlkIiA6ICI3NDZmZTllY2EyNWM0ZWNlYjAxYTFjNDQxY2ZiZTAzZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJVU0VSX1ZBUEVWNCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yYjc4Zjg2ZDU3YWY0NWUwNjg0MjdjZTc1MGRkYmE3ZjNmZmJkYjJmZTE4MTNmNDE2OTRhODdjZjlhZWZlNzI2IgogICAgfQogIH0KfQ==";
    private final static String signature = "YLuRmmLiuWpXUcRTvhRDez+D6L1XwCSedtq8l46b7pwfNGjcGVAOrCvtjAl2vE1lyR+p8bB04zueULG8ucTt7gatCER/dMyrKmEz0QGRrplhBZtBYB2i+UsdM3iTZWaz6iLK7GpUneh17au2chOZtmQnq6aeG4y/vKV4QjsutWk7X53vBvvtWZotLzWESaIHBil46+pB1u2pJrqMCAcE3wXyzyeeAom4t+txz4U8PJ1CM2d6TBz03JvPn6++WOiYqqLHTwOet8wWTNVJwHrpeR/0ij5as2YYQVeO/ZpmhAjvSXvbtPinvaC2jkhxR1CkzFeFKob9SezVkCGC5129gfa9bDicAHYUMdJgdJHvrOFr4uL3npcgkovTrPn6xVQ6FkvNwBmvpgotKyKKKsuNjvPhiECv6e0vHJeVXaOYTDqWAnB2I0rfVt8RWI+5TOvLPb7qXfd5hx7enI79klDiRRcFda9yl5doutq5UKJUY7cyOdoj7XEyRBjan43/+zHGdjWJc6h/k60PK4PkPFoc1p9XGFOWScxhSy1J0ELOruPLIRP2EZQIHUy2CF2JfyNY5ise9NkYRr3toQ/HxVLqcu6VNGadVlw0hLdrmHD5/RQAmX1RhdXpipEm/ANCIq1JK7YAjSuEFd+jWGXyx205bg/rsvinCV5Bjd7YMxWnGHA=";

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        EmojiTaber.config = plugin.getPluginConfig();
        manager = ProtocolLibrary.getProtocolManager();
        ListenerManager.register("EmojiTaber", this);
        reload();
        for(int i = 0; i <= 80; i++) uuids.add(UUID.randomUUID());
        isActive = true;
        return true;
    }

    @Override
    public void reload() {
        final List<String> names = config.getStringList("settings.modules.emoji-taber.list");
        if (names.isEmpty()) return;
        // deleting for all players npc using outdated HashMap
        manager.broadcastServerPacket(getNPCsPacket(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER));
        // putting all new names into HashMap, but not overriding them
        names.stream().filter(name -> !fakePlayers.containsKey(name)).collect(Collectors.toSet())
                .forEach(name -> fakePlayers.put(name, UUID.randomUUID()));
        //removing non-existent names
        fakePlayers.keySet().stream().filter(name ->
                !names.contains(name) // filter names that are not in list from config
        ).collect(Collectors.toSet()).forEach(fakePlayers::remove); // then remove them.
        // resending new packets of npc existence
        manager.broadcastServerPacket(getNPCsPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        try {
            //full tab to not see fake players
            manager.sendServerPacket(player, fullTabPacket());
            //send fake players (emojis)
            manager.sendServerPacket(player, getNPCsPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
        } catch (InvocationTargetException e) {
            LOG.error("There was error after trying to send ADD_PLAYER packets to player " + player.getName(), e);
        }
    }

    private static @Nullable PacketContainer getNPCsPacket(final @NotNull EnumWrappers.PlayerInfoAction action) {
        if (fakePlayers.isEmpty()) return null;
        //creating packet
        final PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        final WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(container);

        info.setAction(action);
        // creating every npc data for every name in HashMap
        final List<PlayerInfoData> data = new ArrayList<>();
        for (final @NotNull String name : fakePlayers.keySet()) {
            final UUID uuid = fakePlayers.get(name);
            final GameProfile profile = new GameProfile(uuid, name);
            profile.getProperties().put("textures", new Property("textures", texture, signature));
            final WrappedGameProfile prof = WrappedGameProfile.fromHandle(profile);
            data.add(new PlayerInfoData(prof, 10, EnumWrappers.NativeGameMode.SPECTATOR, WrappedChatComponent.fromText(name)));
        }
        //setting data
        info.setData(data);
        return container;
    }

    private static @NotNull PacketContainer fullTabPacket() {
        //creating packet
        final PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        final WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(container);
        info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        final List<PlayerInfoData> data = new ArrayList<>();
        for (final @NotNull UUID uuid : uuids) {
            final GameProfile profile = new GameProfile(uuid, " ");
            profile.getProperties().put("textures", new Property("textures", texture, signature));
            final WrappedGameProfile prof = WrappedGameProfile.fromHandle(profile);
            data.add(new PlayerInfoData(prof, 10, EnumWrappers.NativeGameMode.SPECTATOR, WrappedChatComponent.fromText(" ")));
        }
        info.setData(data);
        return container;
    }



    @Override
    public void disable() {
        isActive = false;
    }

    @Override
    public boolean isConditionsMet() {
        return Bridge.getInstance().getPluginConfig().getBoolean("settings.modules.emoji-taber", true)
                && ProtocolLibManager.isActive();
    }

    @Override
    public boolean active() {
        return isActive;
    }
}
