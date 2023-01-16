package bridge.event;

import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ProjectileHeadshotEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private final EntityDamageByEntityEvent event;

    public ProjectileHeadshotEvent (final @NotNull EntityDamageByEntityEvent event) {
        this.event = event;
        isCancelled = false;
    }

    public @NotNull EntityDamageByEntityEvent getOriginalEvent() {return event;}

    /**
     * Gets {@link Projectile} from original method using {@link #getOriginalEvent()}.
     *
     * @return projectile.
     * @throws IllegalStateException if event was fired but no projectile found.
     */
    public Projectile getProjectile() throws IllegalStateException {
        if(getOriginalEvent().getDamager() instanceof Projectile projectile) return projectile;
        else throw new IllegalStateException("ProjectileHeadshotEvent was fired, but theres no projectile found.");
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {return handlers;}
}
