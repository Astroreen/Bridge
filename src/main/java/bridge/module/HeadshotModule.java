package bridge.module;

import bridge.Bridge;
import bridge.config.ConfigurationFile;
import bridge.event.ProjectileHeadshotEvent;
import bridge.exceptions.HookException;
import common.Module;
import common.Permission;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@CustomLog
public class HeadshotModule implements Module, Listener {

    private static Bridge plugin;
    private static ConfigurationFile config;
    private static double hitMultiplier = 1.0;
    private static double xOffset = 1.0, yOffset = 1.0, zOffset = 1.0;
    private static double speed;
    private static String sound;
    private static float soundVolume, soundPitch;
    private static Particle particle;
    private static int particleAmount = 1;

    private final static List<String> allowedTypes = new ArrayList<>();
    private static boolean isActive;

    @Override
    public boolean start(final @NotNull Bridge plugin) throws HookException {
        HeadshotModule.plugin = plugin;
        //setup configuration file
        try {
            config = ConfigurationFile.create(new File("headshot-config.yml"), plugin, "server/headshot-config.yml");
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Wasn't able to create 'headshot-config.yml' file!", e);
            return false;
        }
        reload();
        isActive = true;

        return true;
    }

    @Override
    public void reload() {
        allowedTypes.addAll(config.getStringList("allowed-types"));

        if (config.getBoolean("sound.enable")) {
            sound = config.getString("sound.name");
            soundVolume = (float) config.getDouble("sound.volume", 1.0f);
            soundPitch = (float) config.getDouble("sound.pitch", 1.0f);
        }

        hitMultiplier = config.getInt("hit-multiplier", 2);
        //particle settings
        particle = Particle.valueOf(config.getString("particle-settings.particle", "CRIT").toUpperCase());
        particleAmount = config.getInt("particle-settings.amount", 5);
        xOffset = config.getDouble("particle-settings.xOffset", 0.0);
        yOffset = config.getDouble("particle-settings.yOffset", 0.0);
        zOffset = config.getDouble("particle-settings.zOffset", 0.0);
        speed = config.getDouble("particle-settings.speed", 0.3);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onHeadshot(final @NotNull ProjectileHeadshotEvent e) {
        final EntityDamageByEntityEvent event = e.getOriginalEvent();
        if (!(event.getEntity() instanceof final LivingEntity target) || !allowedTypes.contains(event.getEntity().getType().name()))
            return;

        final Projectile pr = e.getProjectile();

        //is shooter instance of player
        boolean isPlayer = pr.getShooter() instanceof Player;

        if (isPlayer) {
            final Player p = (Player) pr.getShooter();

            if (!p.hasPermission(Permission.HEADSHOT_MAKE_HEADSHOT))
                return;
        }
        //start doing things
        //spawn particles
        particle(particle, target.getEyeLocation(),
                0.3 + xOffset,
                0.3 + yOffset,
                0.3 + zOffset,
                particleAmount, speed,
                target instanceof Player player ? player : null);
        //check permission for damage multiplying
        if (isPlayer) {
            final Player p = (Player) pr.getShooter();

            if (sound != null)
                p.playSound(p.getLocation(), sound, soundVolume, soundPitch);
        }

        //multiply damage
        double dmg = event.getDamage() * hitMultiplier;
        //set damage
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            target.setNoDamageTicks(0);
            target.damage(dmg, pr);
        });

    }

    private void particle(final @NotNull Particle particle, final @NotNull Location loc,
                         final double x, final double y, final double z, final int count,
                         final double speed, final Player pl) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (pl.equals(p))
                continue;
            p.spawnParticle(particle, loc, count, x, y, z, speed);
        }
    }

    @Override
    public void disable() {

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
