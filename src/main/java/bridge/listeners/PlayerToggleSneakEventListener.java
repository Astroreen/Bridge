package bridge.listeners;

import bridge.Bridge;
import bridge.compatibility.Compatibility;
import bridge.compatibility.CompatiblePlugin;
import bridge.compatibility.itemsadder.IAManager;
import bridge.modules.Module;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import lombok.CustomLog;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

@CustomLog
public class PlayerToggleSneakEventListener implements Module, Listener {

    private static Bridge plugin;
    private static boolean isActive;
    private static String helmet = null;
    private static boolean isHelmetIA = false;
    private static String chestplate = null;
    private static boolean isChestplateIA = false;
    private static String leggings = null;
    private static boolean isLeggingsIA = false;
    private static String boots = null;
    private static boolean isBootsIA;

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        PlayerToggleSneakEventListener.plugin = plugin;
        ListenerManager.register("Fly-Shift", this);
        reload();
        isActive = true;
        return true;
    }
    @EventHandler
    public void onItemsAdderLoadDataEvent(final @NotNull ItemsAdderLoadDataEvent event){
        if (event.getCause().equals(ItemsAdderLoadDataEvent.Cause.FIRST_LOAD)) reload();
    }

    @Override
    public void reload() {
        final String settings = plugin.getPluginConfig().getString("settings.modules.shift-fly.armor");
        if(settings == null) {
            LOG.error("Wasn't able to get armor from config.yml for module 'shift-fly'. Ignoring.");
            return;
        }
        final String[] armor = settings.replaceAll("\\s+", "").split(",", 4);
        if(armor.length != 4) {
            LOG.error("No armor there specified for module 'shift-fly'. Ignoring.");
            return;
        }
        if(Compatibility.getHooked().contains(CompatiblePlugin.ITEMSADDER)){
            if(IAManager.isItemExist(armor[0])) {
                helmet = armor[0];
                isHelmetIA = true;
            }
            if(IAManager.isItemExist(armor[1])) {
                chestplate = armor[1];
                isChestplateIA = true;
            }
            if(IAManager.isItemExist(armor[2])) {
                leggings = armor[2];
                isLeggingsIA = true;
            }
            if(IAManager.isItemExist(armor[3])) {
                boots = armor[3];
                isBootsIA = true;
            }
        }

        try { //helmet
            if(!isHelmetIA) helmet = Material.valueOf(armor[0]).toString();
        } catch (final IllegalArgumentException e){
            LOG.error("Can't find Material or item by id '" + armor[0] + "' in config.yml");
            helmet = null;
            isHelmetIA = false;
        }
        try { //chestplate
            if(!isChestplateIA) chestplate = Material.valueOf(armor[1]).toString();
        } catch (final IllegalArgumentException e){
            LOG.error("Can't find Material or item by id '" + armor[1] + "' in config.yml");
            chestplate = null;
            isChestplateIA = false;
        }
        try { //leggings
            if(!isLeggingsIA) leggings = Material.valueOf(armor[2]).toString();
        } catch (final IllegalArgumentException e){
            LOG.error("Can't find Material or item by id '" + armor[2] + "' in config.yml");
            leggings = null;
            isLeggingsIA = false;
        }
        try { //boots
            if(!isBootsIA) boots = Material.valueOf(armor[3]).toString();
        } catch (final IllegalArgumentException e){
            LOG.error("Can't find Material or item by id '" + armor[3] + "' in config.yml");
            boots = null;
            isBootsIA = false;
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent (final @NotNull PlayerToggleSneakEvent event){
        final PlayerInventory inv = event.getPlayer().getInventory();
        event.getPlayer().setAllowFlight(
                event.isSneaking()
                && haveHelmet(inv.getHelmet())
                && haveChestplate(inv.getChestplate())
                && haveLeggings(inv.getLeggings())
                && haveBoots(inv.getBoots())
        );
    }

    private static boolean haveHelmet(final ItemStack item) {
        if(helmet == null || item == null) return false;
        if(isHelmetIA) {
            final CustomStack custom = IAManager.getItem(helmet);
            if(custom == null) return false;
            final CustomStack remade = CustomStack.byItemStack(item);
            if(remade == null) return false;
            return remade.matchNamespacedID(custom);
        }
        return item.isSimilar(new ItemStack(Material.valueOf(helmet)));
    }

    private static boolean haveChestplate(final ItemStack item) {
        if(chestplate == null || item == null) return false;
        if(isChestplateIA) {
            final CustomStack custom = IAManager.getItem(chestplate);
            if(custom == null) return false;
            final CustomStack remade = CustomStack.byItemStack(item);
            if(remade == null) return false;
            return remade.matchNamespacedID(custom);
        }
        return item.isSimilar(new ItemStack(Material.valueOf(chestplate)));
    }

    private static boolean haveLeggings(final ItemStack item) {
        if(leggings == null || item == null) return false;
        if(isLeggingsIA) {
            final CustomStack custom = IAManager.getItem(leggings);
            if(custom == null) return false;
            final CustomStack remade = CustomStack.byItemStack(item);
            if(remade == null) return false;
            return remade.matchNamespacedID(custom);
        }
        return item.isSimilar(new ItemStack(Material.valueOf(leggings)));
    }

    private static boolean haveBoots(final ItemStack item) {
        if(boots == null || item == null) return false;
        if(isBootsIA) {
            final CustomStack custom = IAManager.getItem(boots);
            if(custom == null) return false;
            final CustomStack remade = CustomStack.byItemStack(item);
            if(remade == null) return false;
            return remade.matchNamespacedID(custom);
        }
        return item.isSimilar(new ItemStack(Material.valueOf(boots)));
    }

    @Override
    public void disable() {
        isActive = false;
    }

    @Override
    public boolean isConditionsMet() {
        return true; //no conditions
    }

    @Override
    public boolean active() {
        return isActive;
    }
}
