package bridge.packets.player;

import bridge.Bridge;
import bridge.compatibility.protocollib.ProtocolLibManager;
import common.config.ConfigurationFile;
import bridge.listener.ListenerManager;
import common.IModule;
import bridge.pluginmodule.permissions.PermissionManager;
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
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@CustomLog(topic = "EmojiTaber")
public class EmojiTaber implements IModule, Listener {
    private static Bridge plugin;
    private static ConfigurationFile config;
    private static ProtocolManager manager;
    private static String permission;
    private final static HashMap<String, List<Emoji>> fakePlayers = new HashMap<>();
    private final static Set<UUID> joined = new HashSet<>(1);


    @Override
    public boolean start(final @NotNull Bridge plugin) {
        EmojiTaber.plugin = plugin;
        try {
            EmojiTaber.config = ConfigurationFile.create(new File(plugin.getDataFolder(), "emoji-list.yml"), plugin, "server/emoji-list.yml");
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Wasn't able to create 'emoji-list.yml' file!", e);
            return false;
        }
        EmojiTaber.manager = ProtocolLibrary.getProtocolManager();
        ListenerManager.register(getName(), this);
        this.reload();
        return true;
    }

    @Override @SuppressWarnings("deprecation")
    public void reload() {
        try {
            config.reload();
        } catch (IOException e) {
            LOG.error("Couldn't reload 'emoji-list.yml' config.", e);
            return;
        }
        EmojiTaber.permission = config.getString("permission-base", "bridge.emoji");
        final ConfigurationSection section = config.getConfigurationSection("");
        if (section == null) return;
        final HashMap<String, List<String>> emojis = new HashMap<>();
        final Set<String> groups = section.getKeys(false);
        groups.remove("permission-base");
        groups.forEach(group -> emojis.put(group, config.getStringList(group)));
        if (emojis.isEmpty()) return;

        // deleting for all players npc using outdated HashMap
        Bukkit.getOnlinePlayers().forEach(player -> sendNPCPackets(player, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER));
        for (final @NotNull String group : groups) {
            final List<String> list = emojis.get(group);
            if (!fakePlayers.containsKey(group)) {
                final List<Emoji> created = new ArrayList<>();
                list.forEach(name -> created.add(new Emoji(name, UUID.randomUUID())));
                fakePlayers.put(group, created);
                continue;
            }

            //removing non-existent names
            fakePlayers.get(group).stream().filter(emoji ->
                    !list.contains(emoji.name()) // filter names that are not in list from config
            ).collect(Collectors.toSet()).forEach(rec -> fakePlayers.get(group).remove(rec)); // then remove them.

            // getting names that are same in different list and HashMap
            final Set<String> names = new HashSet<>();
            for (final @NotNull Emoji emoji : fakePlayers.get(group)) {
                for (final @NotNull String name : list) {
                    if (emoji.name().equals(name)) names.add(name);
                }
            }
            // putting all new names into HashMap, but not overriding them
            list.stream().filter(name -> !names.contains(name)).toList() // filter ones that new list doesn't contain
                    .forEach(name -> fakePlayers.get(group).add(new Emoji(name, UUID.randomUUID()))); // adding them
        }
        // resending new packets of npc existence
        Bukkit.getOnlinePlayers().forEach(player -> sendNPCPackets(player, EnumWrappers.PlayerInfoAction.ADD_PLAYER));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (joined.contains(player.getUniqueId())) return;
        joined.add(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                //sending full tab of fake players with no name
                ProtocolLibUtils.fullTabPacket(10).forEach(packet -> {
                    try {
                        manager.sendServerPacket(player, packet);
                    } catch (Exception e) {
                        LOG.error("There was error after trying to send ADD_PLAYER packets to player " + player.getName(), e);
                    }
                });
            }
        }.runTaskAsynchronously(plugin);
        sendNPCPackets(player, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        joined.remove(player.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    private static void sendNPCPackets(final Player player, final @NotNull EnumWrappers.PlayerInfoAction action) {
        if (fakePlayers.isEmpty()) return;
        final PermissionManager perms = new PermissionManager();

        final Set<Emoji> emojis = new HashSet<>();
        // add emojis that can be shown
        for (final @NotNull String group : fakePlayers.keySet()) {
            if (perms.havePermission(player, permission + "." + group)) {
                emojis.addAll(fakePlayers.get(group));
            }

        }

        //creating PlayerInfoData for all emojis
        final Set<String> l = new HashSet<>();
        final Set<PlayerInfoData> data = new HashSet<>();
        for (final @NotNull Emoji emoji : emojis) {
            if (emoji.name().length() > 16) l.add(emoji.name());
            final String name = emoji.name().length() < 16 ? emoji.name() : emoji.name().substring(0, 16);
            final UUID uuid = emoji.uuid();
            final GameProfile profile = new GameProfile(uuid, name);
            profile.getProperties().put("textures", ProtocolLibUtils.getUnknownProperty());
            final WrappedGameProfile prof = WrappedGameProfile.fromHandle(profile);
            data.add(new PlayerInfoData(prof, 10, EnumWrappers.NativeGameMode.SPECTATOR, WrappedChatComponent.fromText(name)));
        }
        if(l.size() != 0) LOG.warn("Found " + l.size() + " emoji which are more that 16 characters: " + l.toString().substring(1, l.toString().length() - 1));

        final List<PacketContainer> packets = new ArrayList<>();
        if(action.equals(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER))
            packets.addAll(ProtocolLibUtils.removePlayerInfoData(data.stream().toList(), 10));
        else {
            //split list into chunks
            final List<List<PlayerInfoData>> temp = Lists.partition(data.stream().toList(), 10);

            //preparing packet containers
            for (final @NotNull List<PlayerInfoData> list : temp) {
                //creating packet
                final PacketContainer container = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
                final WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo(container);

                //setting data;
                info.setData(list);
                info.setAction(action);
                packets.add(container);
            }
        }

        //sending packets asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                for (final @NotNull PacketContainer packet : packets) {
                    try {
                        manager.sendServerPacket(player, packet);
                    } catch (Exception e) {
                        LOG.error("There was error after trying to send " + action + " packets to player " + player.getName(), e);
                    }
                }
            }
        }.runTaskAsynchronously(plugin);


    }


    @Override
    public void disable() {
        //Empty
    }

    @Override
    public boolean isConditionsMet() {
        return ProtocolLibManager.isActive();
    }

    record Emoji(String name, UUID uuid) {
    }
}
