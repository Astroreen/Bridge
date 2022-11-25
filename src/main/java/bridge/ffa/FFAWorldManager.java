package bridge.ffa;

import bridge.config.ConfigurationFile;
import bridge.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FFAWorldManager {

    private static ConfigurationFile config;

    public static void setup(final @NotNull ConfigurationFile config) {
        FFAWorldManager.config = config;
    }

    public static @Nullable Location randomTeleportLocation(final @NotNull String arena){
        if(isArenaDisabled(arena)) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final List<Location> locations = new ArrayList<>();
        getTeleportPoints(arena).forEach((name, loc) -> locations.add(loc));

        final int r = new Random(locations.size()).nextInt();
        return locations.get(r);
    }

    public static @NotNull HashMap<String, Location> getTeleportPoints(final @NotNull String arena) {
        if(isArenaDisabled(arena)) return new HashMap<>();
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return new HashMap<>();
        if (!getActiveFFAWorlds().contains(world)) return new HashMap<>();

        //if arena teleport locations are disabled, use "default-pos"
        if (!config.getBoolean(String.format("arenas.%s.enabled", arena), false)) {
            Location loc = getTeleportPoint(arena, "default-pos");
            HashMap<String, Location> location = new HashMap<>(1);
            location.put("default-pos", loc);
            if(loc != null) return location;
            return new HashMap<>();
        }

        final ConfigurationSection section =
                config.getConfigurationSection(String.format("arenas.%s.teleport", arena));
        if (section == null) return new HashMap<>();
        final Set<String> pos = section.getKeys(false);
        pos.remove("rtp-enabled");
        pos.remove("enabled");
        final HashMap<String, Location> locations = new HashMap<>();
        for (String name : pos) {
            Location loc = getTeleportPoint(arena, name);
            if(loc != null) locations.put(arena, loc);
        }
        return locations;
    }

    public static @Nullable Location getTeleportPoint(final @NotNull String arena, final @NotNull String location) {
        if(isArenaDisabled(arena)) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final String loc = config.getString(String.format("arenas.%s.teleport.%s", arena, location));
        if(loc == null) return null;
        final String[] cords = loc.split(",", 3);
        final double x = Double.parseDouble(cords[0]);
        final double y = Double.parseDouble(cords[1]);
        final double z = Double.parseDouble(cords[2]);
        return new Location(world, x, y, z);


    }

    public static @NotNull List<World> getActiveFFAWorlds() {
        List<World> worlds = new ArrayList<>();
        getExistingFFAWorlds().forEach(name -> {
            World world = WorldUtils.getWorld(name);
            if (world != null) worlds.add(world);
        });
        return worlds;
    }

    public static @NotNull List<String> getExistingFFAWorlds() {
        List<String> worlds = new ArrayList<>(getAllFFAWorlds());
        worlds.forEach(name -> {
            if (!WorldUtils.isWorldFolderExist(name)) worlds.remove(name);
        });
        return worlds;
    }

    public static @NotNull List<String> getAllFFAWorlds() {
        final ConfigurationSection section = config.getConfigurationSection("arenas");
        if (section == null) return List.of();
        return new ArrayList<>(section.getKeys(false));
    }

    public static boolean isArenaDisabled(final @NotNull String arena){
        return !config.getBoolean(String.format("arenas.%s.use", arena), false);
    }

    public static boolean haveSchematic(final @NotNull String arena){
        final String schem = config.getString(String.format("arenas.%s.schematic", arena));
        return schem != null && !schem.equals("none") && !schem.split("\\.", 2)[1].equals("schem");
    }

    public static @Nullable Block getSchematicBlock(final @NotNull String arena){
        if(!haveSchematic(arena)) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final String loc = config.getString(String.format("arenas.%s.schematic-cords", arena));
        if(loc == null) return null;
        final String[] cords = loc.split(",", 3);
        final double x = Double.parseDouble(cords[0]);
        final double y = Double.parseDouble(cords[1]);
        final double z = Double.parseDouble(cords[2]);
        return new Location(world, x, y, z).getBlock();
    }
}
