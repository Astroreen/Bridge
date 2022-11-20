package bridge.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldUtils {

    public static boolean loadWorld() {
        //TODO load world
        return false;
    }

    public static boolean unloadWorld() {
        //TODO unload world
        return false;
    }

    @Contract(pure = true)
    public static @Nullable World getWorld(final @NotNull String name){
        return Bukkit.getWorld(name);
    }

    public static boolean isWorldLoaded(final @NotNull String name) {
        //TODO check if world is loaded
        return false;
    }
}
