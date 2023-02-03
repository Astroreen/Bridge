package bridge.module;

import bridge.Bridge;
import bridge.listener.ListenerManager;
import bridge.pluginmodule.config.ConfigurationFile;
import bridge.pluginmodule.permissions.PermissionManager;
import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import common.IModule;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.bukkit.event.entity.EntityPotionEffectEvent.Action;

@CustomLog(topic = "BetterInvisibility")
public class BetterInvisibilityModule implements IModule, Listener {

    /// REMOVE INVISIBILITY
    private static boolean playerRemoveDamage;
    private static boolean playerRemoveInteraction;
    private static boolean playerRemoveHit;
    private static boolean mobRemoveDamage;
    private static boolean mobRemoveHit;
    /// HIDE PARTICLES
    private static boolean playerHideParticles;
    private static String playerHidePermission;
    private static boolean mobHideParticles;
    // REAPPLY EFFECT
    private static SimpleEntry<Boolean, Long> playerReapplyDamage;
    private static SimpleEntry<Boolean, Long> playerReapplyInteraction;
    private static SimpleEntry<Boolean, Long> playerReapplyHit;
    private static SimpleEntry<Boolean, Long> mobReapplyDamage;
    private static SimpleEntry<Boolean, Long> mobReapplyHit;
    // BLIND TIER
    private static int defaultBlindTier;
    private static final Map<String, Integer> blindTier = new HashMap<>();
    //FORGET IN INVIS
    private static boolean forgetInvis;

    //VARIABLES
    private static ConfigurationFile config;
    private static Bridge plugin;

    @Override
    public boolean start(final @NotNull Bridge plugin) {
        BetterInvisibilityModule.plugin = plugin;
        try {
            config = ConfigurationFile.create(
                    new File(plugin.getDataFolder(), "better-invis.yml"),
                    plugin,
                    "server/better-invis.yml"
            );
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Wasn't able to create configuration file for '" + getName() + "' module.", e);
            return false;
        }

        ListenerManager.register(getName(), this);
        reload();
        return true;
    }

    @Override
    public void reload() {
        forgetInvis = config.getBoolean("clear-threat-in-invis", false);

        final ConfigurationSection playerRemove = config.getConfigurationSection("player-remove-invisibility-after");
        if (playerRemove != null) {
            playerRemoveDamage = playerRemove.getBoolean("damage", false);
            playerRemoveInteraction = playerRemove.getBoolean("interaction", false);
            playerRemoveHit = playerRemove.getBoolean("hit", false);
        }

        final ConfigurationSection mobRemove = config.getConfigurationSection("mob-remove-invisibility-after");
        if (mobRemove != null) {
            mobRemoveDamage = mobRemove.getBoolean("damage", false);
            mobRemoveHit = mobRemove.getBoolean("hit", false);
        }

        final ConfigurationSection hideParticles = config.getConfigurationSection("hide-particles");
        if (hideParticles != null) {
            playerHideParticles = hideParticles.getBoolean("player", false);
            playerHidePermission = hideParticles.getString("player-permission", "bridge.invisibility.show.particles");
            mobHideParticles = hideParticles.getBoolean("mob", false);
        }

        final ConfigurationSection playerReapply = config.getConfigurationSection("reapply-player-invisibility-after");
        if (playerReapply != null) {
            playerReapplyDamage = new SimpleEntry<>(
                    playerReapply.getBoolean("damage", false),
                    playerReapply.getLong("damage-delay", 20)
            );
            playerReapplyInteraction = new SimpleEntry<>(
                    playerReapply.getBoolean("interaction", false),
                    playerReapply.getLong("interaction-delay", 20)
            );
            playerReapplyHit = new SimpleEntry<>(
                    playerReapply.getBoolean("hit", false),
                    playerReapply.getLong("hit-delay", 20)
            );
        }

        final ConfigurationSection mobReapply = config.getConfigurationSection("reapply-mob-invisibility-after");
        if (mobReapply != null) {
            mobReapplyDamage = new SimpleEntry<>(
                    mobReapply.getBoolean("damage", false),
                    mobReapply.getLong("damage-delay", 20)
            );
            mobReapplyHit = new SimpleEntry<>(
                    mobReapply.getBoolean("hit", false),
                    mobReapply.getLong("hit-delay", 20)
            );
        }

        defaultBlindTier = config.getInt("default-blind-tier");

        final ConfigurationSection blindTierList = config.getConfigurationSection("blind-tier");
        if (blindTierList != null) {
            blindTier.clear(); //clearing map if configuration section exist
            final Set<String> mobs = blindTierList.getKeys(false);
            //adding to map mobs types
            for (final @NotNull String mob : mobs) {
                int tier = blindTierList.getInt(mob);
                blindTier.put(mob, tier);
            }
        }
    }

    /**
     * Reapplies or removes invisibility potion when <b>player</b> have <b>interacted</b> with something.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(final @NotNull PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) return; // no invis no problem

        if (playerReapplyInteraction.getKey()) {
            final PotionEffect effect = player.getPotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                assert effect != null; // cuz we've checked this above using #hasPotionEffect
                player.addPotionEffect(effect);
                clearThreat(player);
            }, playerReapplyInteraction.getValue());
        } else if (playerRemoveInteraction) player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    /**
     * Reapplies or removes invisibility potion when {@linkplain LivingEntity entity} <b>getting damage.</b>
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(final @NotNull EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return; // nah, I only work with mobs and players

        if (!entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) return; // no invis no problem
        final PotionEffect effect = entity.getPotionEffect(PotionEffectType.INVISIBILITY);
        assert effect != null; // cuz we've checked this above using #hasPotionEffect

        if (entity instanceof Player player) {
            if (playerReapplyDamage.getKey()) {

                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                Bukkit.getScheduler().runTaskLater(
                        plugin, () -> {
                            player.addPotionEffect(effect);
                            clearThreat(player);
                        },
                        playerReapplyDamage.getValue()
                );
            } else if (playerRemoveDamage) player.removePotionEffect(PotionEffectType.INVISIBILITY);
            return;
        }

        if (mobReapplyDamage.getKey()) {
            entity.removePotionEffect(PotionEffectType.INVISIBILITY);
            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> {
                        entity.addPotionEffect(effect);
                        clearThreat(entity);
                    },
                    mobReapplyDamage.getValue()
            );
        } else if (mobRemoveDamage) entity.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    /**
     * Reapplies or removes invisibility potion when {@linkplain LivingEntity entity} <i>(damager)</i> <b>hit</b> another entity.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(final @NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity damager)) return; // nah, I only work with mobs and players

        if (event.getEntity() instanceof LivingEntity entity //damaged while was invisible?
                && entity.hasPotionEffect(PotionEffectType.INVISIBILITY)){
            //that's unfair so no damage
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> entity.setNoDamageTicks(0));
            clearThreat(entity); //and clearing him from target list
        }


        if (!damager.hasPotionEffect(PotionEffectType.INVISIBILITY)) return; // no invis no problem


        final PotionEffect effect = damager.getPotionEffect(PotionEffectType.INVISIBILITY);
        assert effect != null; // cuz we've checked this above using #hasPotionEffect

        if (damager instanceof Player player) {
            if (playerReapplyHit.getKey()) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                Bukkit.getScheduler().runTaskLater(
                        plugin, () -> {
                            player.addPotionEffect(effect);
                            clearThreat(player);
                        },
                        playerReapplyHit.getValue()
                );
            } else if (playerRemoveHit) player.removePotionEffect(PotionEffectType.INVISIBILITY);
            return;
        }

        if (mobReapplyHit.getKey()) {
            damager.removePotionEffect(PotionEffectType.INVISIBILITY);
            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> {
                        damager.addPotionEffect(effect);
                        clearThreat(damager);
                    },
                    mobReapplyHit.getValue()
            );
        } else if (mobRemoveHit) damager.removePotionEffect(PotionEffectType.INVISIBILITY);
    }

    /**
     * Hides particles.
     */
    @EventHandler(priority = EventPriority.HIGHEST) //we will change something in this event, so no monitor
    public void onEntityChangeEffectEvent(final @NotNull EntityPotionEffectEvent event) {
        final PotionEffect effect = event.getNewEffect();
        // We're not dealing with it, if effect was removed or effect isn't invisibility.
        if (effect == null
                || !effect.getType().equals(PotionEffectType.INVISIBILITY))
            return;
        if (!effect.hasParticles()) return; // why do I have to change particles to the potion that already without them
        if (!(event.getEntity() instanceof LivingEntity entity)) return; // nah, I only work with mobs and players
        final Action action = event.getAction();
        //trigger when effect is added or changed to it
        if (!(action.equals(Action.ADDED) || action.equals(Action.CHANGED))) return;
        if (action.equals(Action.CHANGED)) event.setOverride(true);

        event.setCancelled(true); // to apply my effect after all rules have been checked

        //check for perms if player
        if (entity instanceof Player player && playerHideParticles) {
            PermissionManager perms = new PermissionManager();
            if (!perms.havePermission(player, playerHidePermission)) return;
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.INVISIBILITY,
                    effect.getDuration(),
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    false,
                    effect.hasIcon()
            ));
            clearThreat(player);
        } else if (mobHideParticles) {
            entity.removePotionEffect(PotionEffectType.INVISIBILITY);
            entity.addPotionEffect(new PotionEffect(
                    PotionEffectType.INVISIBILITY,
                    effect.getDuration(),
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    false,
                    effect.hasIcon()
            ));
            clearThreat(entity);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTargetLivingEntityEvent(final @NotNull EntityTargetLivingEntityEvent event) {
        final LivingEntity target = event.getTarget();
        if (target == null) return; //target just have been changed

        if (check(target, event.getEntity())) {
            event.setTarget(null); //no target if invisible
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityPathfindEvent(final @NotNull EntityPathfindEvent event){
        if(!(event.getTargetEntity() instanceof LivingEntity entity)) return;

        if(check(entity, event.getEntity())) {// do we have reason to change something
            clearThreat(entity);
            event.setCancelled(true);
        }
    }

    private void clearThreat(final @NotNull LivingEntity entity) {
        if (!forgetInvis) return;
        for (final @NotNull Mob mob : entity.getWorld().getEntitiesByClass(Mob.class)) {
            if (mob.getTarget() != null && // target not null
                    (mob.getTarget().equals(entity) || entity instanceof Player)
            ) Bukkit.getPluginManager().callEvent(new EntityTargetLivingEntityEvent(
                    mob,
                    mob.getTarget(),
                    null)
            );
        }
    }

    private boolean check(final @NotNull LivingEntity target, final @NotNull Entity enemy) {
        if (!(target.hasPotionEffect(PotionEffectType.INVISIBILITY)
                || target.isInvisible())) return false; // not my problem

        final PotionEffect effect = target.getPotionEffect(PotionEffectType.INVISIBILITY);
        assert effect != null;

        final int amplifier = effect.getAmplifier();
        final int tier;
        if (blindTier.containsKey(enemy.getType().name()))
            tier = blindTier.get(enemy.getType().name());
        else tier = defaultBlindTier;

        return amplifier >= tier;
    }

    @Override
    public void disable() {
        //Empty
    }

    @Override
    public boolean isConditionsMet() {
        return true;
    }

    @Override
    public String getName() {
        return "BetterInvisibility";
    }
}
