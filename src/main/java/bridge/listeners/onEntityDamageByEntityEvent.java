package bridge.listeners;

import bridge.event.ProjectileHeadshotEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class onEntityDamageByEntityEvent implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof final Projectile pr))
            return;
        if (!(event.getEntity() instanceof final LivingEntity target))
            return;

        final Location loc = target.getLocation();
        final Location prLoc = pr.getLocation();

        final double eyeHeight = target.getEyeHeight(), height = target.getHeight();
        final double mobYmin = loc.getY() + (height - (height - eyeHeight) * 2);

        if (prLoc.getY() >= mobYmin) Bukkit.getPluginManager().callEvent(new ProjectileHeadshotEvent(event));
    }
}
