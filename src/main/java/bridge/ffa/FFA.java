package bridge.ffa;

import bridge.Bridge;
import bridge.config.ConfigurationFile;
import bridge.listeners.ListenerManager;
import bridge.modules.Module;
import bridge.utils.FileUtils;
import lombok.CustomLog;
import org.bukkit.Bukkit;
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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
@CustomLog(topic = "FFA")
public class FFA implements Module, Listener {

    private static Bridge plugin;
    private static ConfigurationFile config;
    private static ConfigurationFile kits;
    private static boolean isActive = false;

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        FFA.plugin = plugin;
        //register this class as listener
        ListenerManager.register("FFA", this);

        final String dir = "/ffa/";
        if(!FileUtils.createDirectory(dir)) {
            LOG.error("Wasn't able to create a directory for module.");
            return false;
        }
        final String schem = "/ffa/schematics/";
        if(!FileUtils.createDirectory(schem)) {
            LOG.error("Wasn't able to create a schematic directory for module.");
            return false;
        }
        final File root = new File(plugin.getDataFolder() + dir);
        try {
            FFA.config = ConfigurationFile.create(new File(root, "ffa-config.yml"), plugin, "ffa/ffa-config.yml");
            FFA.kits = ConfigurationFile.create(new File(root, "kits.yml"), plugin, "ffa/kits.yml");
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Wasn't able to create 'ffa-config.yml' file!", e);
            return false;
        }

        FFAKitManager.setup(kits);
        FFAArenaManager.setup(plugin, config);
        new FFAPlaceholders().register();
        //fully working
        isActive = true;
        return true;
    }

    @Override
    public void reload() {
        FFAKitManager.setup(kits);
        FFAArenaManager.setup(plugin, config);
    }

    @Override
    public void disable() {
        FFAArenaManager.unloadAllArenas();
        isActive = false;
    }


    /**
     * Remove empty potion bottles from player's inventory.
     */
    @EventHandler
    public void onPlayerItemConsumeEvent(final @NotNull PlayerItemConsumeEvent event){
        if(!FFAArenaManager.getActiveFFAWorlds(false).contains(event.getPlayer().getWorld())) return;
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
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeathEvent(final @NotNull PlayerDeathEvent event){
        final Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        if(!FFAArenaManager.getActiveFFAWorlds(false).contains(killer.getWorld())) return;
        final String kit = FFAKitManager.getPlayerKitName(killer);
        final AttributeInstance maxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            killer.setHealth(20);
            if(kit != null) FFAKitManager.applyKit(killer, kit);
            return;
        }
        killer.setHealth(maxHealth.getValue());
        if(kit != null) FFAKitManager.applyKit(killer, kit);
        FFAKitManager.removeKit(event.getPlayer());
    }

    /**
     * Remove from player his kit if he is not in FFA world system.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorldEvent(final @NotNull PlayerChangedWorldEvent event){
        final World world = event.getPlayer().getWorld();
        if(!FFAArenaManager.getActiveFFAWorlds(false).contains(world)) {
            FFAKitManager.removeKit(event.getPlayer());
        }
    }

    /**
     * Allows break blocks only on arenas with schematic.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreakEvent(final @NotNull BlockBreakEvent event){
        final World world = event.getPlayer().getWorld();
        if(FFAArenaManager.getActiveFFAWorlds(false).contains(world)
                && !FFAArenaManager.haveSchematic(world.getName()))
            event.setCancelled(true);
    }

    /**
     * Not allows to explode blocks on FFA's arenas that do not have schematic.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockExplodeEvent(final @NotNull BlockExplodeEvent event){
        final World world = event.getBlock().getWorld();
        if(FFAArenaManager.getActiveFFAWorlds(false).contains(world)
                && !FFAArenaManager.haveSchematic(world.getName()))
            event.setCancelled(true);
    }

    @Override
    public boolean isConditionsMet() {
        return true;
    }

    @Override
    public boolean active() {
        return isActive;
    }
}
