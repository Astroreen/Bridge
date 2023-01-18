package bridge.listener;

import bridge.event.ProjectileHeadshotEvent;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

@CustomLog
public class onEntityDamageByEntityEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("deprecation")
    public void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof final Projectile pr))
            return;
        if (!(event.getEntity() instanceof final LivingEntity target))
            return;

        final Location loc = target.getLocation();
        final Location prLoc = pr.getLocation();

        final double eyeHeight = target.getEyeHeight(), height = target.getHeight();
        final double mobYmin = loc.getY() + (height - (height - eyeHeight) * 2);

        if (prLoc.getY() >= mobYmin)
            Bukkit.getPluginManager().callEvent(
                    new ProjectileHeadshotEvent(
                            event.getDamager(),
                            event.getEntity(),
                            event.getCause(),
                            new EnumMap<>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, event.getDamage())),
                            new EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, Functions.constant(-0.0))),
                            event.isCritical()
                    )
            );
    }
}
