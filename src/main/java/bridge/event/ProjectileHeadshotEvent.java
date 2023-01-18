package bridge.event;

import com.google.common.base.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ProjectileHeadshotEvent extends EntityDamageByEntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private static boolean isCancelled;

    @SuppressWarnings("deprecation")
    public ProjectileHeadshotEvent (
            @NotNull final Entity damager,
            @NotNull final Entity damagee,
            @NotNull final DamageCause cause,
            @NotNull final Map<DamageModifier, Double> modifiers,
            @NotNull final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions,
            boolean critical
    ) {
        super(damager, damagee, cause, modifiers, modifierFunctions, critical);
        isCancelled = false;
    }

    /**
     * Gets {@link Projectile}.
     *
     * @return projectile.
     * @throws IllegalStateException if event was fired but no projectile found.
     */
    public Projectile getProjectile() throws IllegalStateException {
        if(getDamager() instanceof Projectile projectile) return projectile;
        else throw new IllegalStateException("ProjectileHeadshotEvent was fired, but theres no projectile found.");
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {isCancelled = cancel;}

    @Override
    public @NotNull HandlerList getHandlers() {return handlers;}
    public static @NotNull HandlerList getHandlerList() {return handlers;}
}
