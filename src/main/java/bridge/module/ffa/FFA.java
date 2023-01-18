package bridge.module.ffa;

import bridge.Bridge;
import bridge.compatibility.Compatibility;
import bridge.compatibility.CompatiblePlugin;
import bridge.compatibility.worldedit.WEManager;
import bridge.listener.ListenerManager;
import bridge.utils.FileUtils;
import common.IModule;
import common.config.ConfigurationFile;
import common.database.Connector;
import common.database.QueryType;
import common.database.Saver;
import common.database.UpdateType;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Main class for FFA control.
 *
 * @author Astroreen
 */
@CustomLog(topic = "FFA")
public class FFA implements IModule, Listener {

    private static Bridge plugin;
    private static ConfigurationFile config;
    private static ConfigurationFile kits;
    private static final List<UUID> exist = new ArrayList<>();

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        FFA.plugin = plugin;
        //register this class as listener
        ListenerManager.register(getName(), this);

        final String dir = "/ffa/";
        if (!FileUtils.createDirectory(dir)) {
            LOG.error("Wasn't able to create a directory for module.");
            return false;
        }
        final String schem = "/ffa/schematics/";
        if (!FileUtils.createDirectory(schem)) {
            LOG.error("Wasn't able to create a schematic directory for module.");
            return false;
        }
        final File root = new File(plugin.getDataFolder() + dir);
        try {
            FFA.config = ConfigurationFile.create(new File(root, "ffa-config.yml"), plugin, "server/ffa/ffa-config.yml");
            FFA.kits = ConfigurationFile.create(new File(root, "kits.yml"), plugin, "server/ffa/kits.yml");
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Wasn't able to create 'ffa-config.yml' file!", e);
            return false;
        }

        try (final ResultSet rs = new Connector().querySQL(QueryType.LOAD_ALL_FFA_UUIDS)) {
            while (rs.next())
                exist.add(UUID.fromString(rs.getString("playerID")));
        } catch (SQLException e) {
            LOG.error("There was exception with SQL", e);
        }

        FFAKitManager.setup(kits);
        FFAArenaManager.setup(plugin, config);
        new FFAPlaceholders().register();
        //fully working
        return true;
    }

    @Override
    public void reload() {
        try {
            kits.reload();
            config.reload();
        } catch (IOException e) {
            LOG.warn("Could not reload " + getName() + " configs! " + e.getMessage(), e);
        }
        FFAKitManager.setup(kits);
        FFAArenaManager.setup(plugin, config);
        FFAPlaceholders.reload();
    }

    @Override
    public void disable() {
        FFAArenaManager.unloadAllArenas();
    }

    @EventHandler
    public void onPlayerJoinEvent(@NotNull PlayerJoinEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        if (!exist.contains(uuid)) {
            new Connector().updateSQL(UpdateType.ADD_FFA, uuid.toString());
            exist.add(uuid);
        }
    }


    /**
     * Remove empty potion bottles from player's inventory.
     */
    @EventHandler
    public void onPlayerItemConsumeEvent(final @NotNull PlayerItemConsumeEvent event) {
        if (!FFAArenaManager.getActiveFFAWorlds(false).contains(event.getPlayer().getWorld())) return;
        if (event.getItem().getType().equals(Material.POTION)) {
            final Player p = event.getPlayer();
            final int heldSlot = p.getInventory().getHeldItemSlot();
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                final ItemStack held = p.getInventory().getItem(heldSlot);
                final ItemStack off = p.getInventory().getItemInOffHand();
                if (held != null && held.getType() == Material.GLASS_BOTTLE) {
                    held.setAmount(0);
                }
                if (off.getType() == Material.GLASS_BOTTLE) {
                    off.setAmount(0);
                }
            }, 1L);
        }
    }

    /**
     * Heal player killer and remove from killed player his kit.
     * Also increment values in database
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeathEvent(final @NotNull PlayerDeathEvent event) {
        final Saver saver = plugin.getSaver();
        final Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        if (!FFAArenaManager.getActiveFFAWorlds(false).contains(killer.getWorld())) return;
        final String kit = FFAKitManager.getPlayerKitName(killer);
        final AttributeInstance maxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            killer.setHealth(20);
            if (kit != null) FFAKitManager.applyKit(killer, kit);
            return;
        }
        killer.setHealth(maxHealth.getValue());
        if (kit != null) FFAKitManager.applyKit(killer, kit);
        FFAKitManager.removeKit(event.getPlayer());
        saver.add(new Saver.Record(UpdateType.INCREMENT_FFA_DEATHS, event.getPlayer().getUniqueId().toString()));
        saver.add(new Saver.Record(UpdateType.INCREMENT_FFA_KILLS, killer.getUniqueId().toString()));
        FFAPlaceholders.addKill(killer.getUniqueId());
        FFAPlaceholders.addDeath(event.getPlayer().getUniqueId());
    }

    /**
     * Remove from player his kit if he is not in FFA world system.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorldEvent(final @NotNull PlayerChangedWorldEvent event) {
        final World world = event.getPlayer().getWorld();
        if (!FFAArenaManager.getActiveFFAWorlds(false).contains(world)) {
            FFAKitManager.removeKit(event.getPlayer());
        }
    }

    /**
     * Allows break blocks only on arenas with schematic.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreakEvent(final @NotNull BlockBreakEvent event) {
        final World world = event.getPlayer().getWorld();
        if (FFAArenaManager.getActiveFFAWorlds(false).contains(world)
                && !FFAArenaManager.haveSchematic(world.getName()))
            event.setCancelled(true);
    }

    /**
     * Not allows to explode blocks on FFA's arenas that do not have schematic.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockExplodeEvent(final @NotNull BlockExplodeEvent event) {
        final World world = event.getBlock().getWorld();
        if (FFAArenaManager.getActiveFFAWorlds(false).contains(world)
                && !FFAArenaManager.haveSchematic(world.getName()))
            event.setCancelled(true);
    }

    /**
     * Pastes schematic on FFA's world load.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldLoadEvent(final @NotNull WorldLoadEvent event) {
        final World world = event.getWorld();
        if (Compatibility.getHooked().contains(CompatiblePlugin.FAWE)
                && FFAArenaManager.getActiveFFAWorlds(true).contains(world)
                && FFAArenaManager.haveSchematic(world.getName())) {
            //pastes schematic
            final String schem = FFAArenaManager.getSchematicFileName(world.getName());
            final Location location = FFAArenaManager.getSchematicLocation(world.getName());
            if (location == null) return;
            WEManager.pasteSchematicAsync(location, FFAArenaManager.getFFASchematicFolder(), schem);
            FFAArenaManager.addRestarter(world);
        }

    }

    /**
     * Removes schematic restarter thread. If there are one.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldUnloadEvent(final @NotNull WorldUnloadEvent event){
        FFAArenaManager.removeRestarter(event.getWorld().getName());
    }

    @Override
    public boolean isConditionsMet() {
        return true;
    }
}
