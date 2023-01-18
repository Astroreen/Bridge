package bridge.module;

import bridge.Bridge;
import bridge.event.ProjectileHeadshotEvent;
import bridge.listener.ListenerManager;
import common.IModule;
import common.Permission;
import common.config.ConfigurationFile;
import common.exceptions.HookException;
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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@CustomLog
public class HeadshotModule implements IModule, Listener {

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

    @Override
    public boolean start(final @NotNull Bridge plugin) throws HookException {
        HeadshotModule.plugin = plugin;
        ListenerManager.register("Headshot",this);
        //setup configuration file
        try {
            config = ConfigurationFile.create(new File(plugin.getDataFolder(), "headshot-config.yml"), plugin, "server/headshot-config.yml");
        } catch (InvalidConfigurationException | FileNotFoundException e) {
            LOG.error("Wasn't able to create 'headshot-config.yml' file!", e);
            return false;
        }
        reload();
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHeadshot(final @NotNull ProjectileHeadshotEvent event) {
        if (!(event.getEntity() instanceof final LivingEntity target) || !allowedTypes.contains(event.getEntity().getType().name()))
            return;

        final Projectile pr = event.getProjectile();

        //is shooter instance of player
        boolean isPlayer = pr.getShooter() instanceof Player;

        //check permission for damage multiplying
        if (isPlayer) {
            final Player p = (Player) pr.getShooter();

            if (!p.hasPermission(Permission.HEADSHOT_MAKE_HEADSHOT))
                return;
            //play sound if player
            if (sound != null)
                p.playSound(p.getLocation(), sound, soundVolume, soundPitch);
        }
        //start doing things
        //spawn particles
        particle(particle, target.getEyeLocation(),
                0.3 + xOffset,
                0.3 + yOffset,
                0.3 + zOffset,
                particleAmount, speed,
                target instanceof Player player ? player : null);

        LOG.debug("Headshot! Shooter: "
                + (isPlayer ? ((Player) pr.getShooter()).getName() : pr.getType().name())
                + ", Entity: " + (event.getEntity() instanceof Player player ? player.getName() : event.getEntity().getType().name()));

        //multiply damage
        double dmg = event.getDamage() * hitMultiplier;

        if (event.getDamage() == dmg) LOG.debug("Damage was " + event.getDamage());
        else LOG.debug("Original damage was " + event.getDamage() + ", with multiplier: " + dmg);

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
            if (p.equals(pl))
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
}
