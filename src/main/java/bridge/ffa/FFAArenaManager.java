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

public class FFAArenaManager {

    private static ConfigurationFile config;

    public static void setup(final @NotNull ConfigurationFile config) {
        FFAArenaManager.config = config;
    }

    public static void reload() {

    }

    public static void pasteSchematic(final @NotNull Block block, String schematic){
        //TODO paste schematic
    }

    /**
     * Unloads all working arenas.
     */
    public static void unloadAllArenas() {
        getActiveFFAWorlds().forEach(w -> WorldUtils.unloadWorld(w, false));
    }

    /**
     * Get random location specified in arena's configuration.
     *
     * @param arena the name
     * @return null, if arena don't exist
     * or arena is disabled
     * or random teleportation is disabled, else {@link Location}
     */
    public static @Nullable Location randomTeleportLocation(final @NotNull String arena) {
        if (isArenaDisabled(arena)) return null;
        if (!config.getBoolean(String.format("arenas.%s.teleport.rtp-enabled", arena))) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final List<Location> locations = new ArrayList<>();
        getTeleportPoints(arena).forEach((name, loc) -> locations.add(loc));

        return locations.get(new Random(locations.size()).nextInt());
    }

    /**
     * Get arena's teleport locations as {@link HashMap}
     * where key is location name and value is location.
     *
     * @param arena the name
     * @return map with locations
     */
    public static @NotNull HashMap<String, Location> getTeleportPoints(final @NotNull String arena) {
        if (isArenaDisabled(arena)) return new HashMap<>();
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return new HashMap<>();
        if (!getActiveFFAWorlds().contains(world)) return new HashMap<>();

        //if arena teleport locations are disabled, use "default-pos"
        if (!config.getBoolean(String.format("arenas.%s.enabled", arena), false)) {
            Location loc = getTeleportPoint(arena, "default-pos");
            HashMap<String, Location> location = new HashMap<>(1);
            location.put("default-pos", loc);
            if (loc != null) return location;
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
            if (loc != null) locations.put(arena, loc);
        }
        return locations;
    }

    /**
     * Get one teleport point using it name.
     *
     * @param arena    the arena name
     * @param location the location name
     * @return null, if arena is disabled
     * or if wasn't able to parse location
     * from config. Location otherwise.
     */
    public static @Nullable Location getTeleportPoint(final @NotNull String arena, final @NotNull String location) {
        if (isArenaDisabled(arena)) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final String loc = config.getString(String.format("arenas.%s.teleport.%s", arena, location));
        if (loc == null) return null;
        final String[] cords = loc.split(",", 3);
        final double x = Double.parseDouble(cords[0]);
        final double y = Double.parseDouble(cords[1]);
        final double z = Double.parseDouble(cords[2]);
        return new Location(world, x, y, z);


    }

    /**
     * Get FFA worlds that is working right now.
     *
     * @return list of active FFA worlds
     */
    public static @NotNull List<World> getActiveFFAWorlds() {
        List<World> worlds = new ArrayList<>();
        getExistingFFAWorlds().forEach(name -> {
            World world = WorldUtils.getWorld(name);
            if (world != null) worlds.add(world);
        });
        return worlds;
    }

    /**
     * Get name of the world that is FFA worlds.
     * <p>
     * <b>ATTENTION:</b> worlds can be unloaded!
     *
     * @return list of FFA worlds names.
     */
    public static @NotNull List<String> getExistingFFAWorlds() {
        List<String> worlds = new ArrayList<>(getAllFFAWorlds());
        worlds.forEach(name -> {
            if (!WorldUtils.isWorldFolderExist(name)) worlds.remove(name);
        });
        return worlds;
    }

    /**
     * Get all FFA worlds names from config.
     *
     * @return list of names.
     */
    public static @NotNull List<String> getAllFFAWorlds() {
        final ConfigurationSection section = config.getConfigurationSection("arenas");
        if (section == null) return List.of();
        return new ArrayList<>(section.getKeys(false));
    }

    /**
     * Check if arena is disabled.
     *
     * @param arena the name
     * @return true if disabled
     */
    public static boolean isArenaDisabled(final @NotNull String arena) {
        return !config.getBoolean(String.format("arenas.%s.use", arena), false);
    }

    /**
     * Check if arena use schematic.
     *
     * @param arena the name
     * @return true, if use schematic
     */
    public static boolean haveSchematic(final @NotNull String arena) {
        final String schem = config.getString(String.format("arenas.%s.schematic", arena));
        return schem != null && !schem.equals("none") && !schem.split("\\.", 2)[1].equals("schem");
    }

    /**
     * Get block from config there to place schematic.
     *
     * @param arena the name
     * @return null, if arena don't exist or can't
     * parse block location from config. Otherwise, returns {@link Block}.
     */
    public static @Nullable Block getSchematicBlock(final @NotNull String arena) {
        if (!haveSchematic(arena)) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final String loc = config.getString(String.format("arenas.%s.schematic-cords", arena));
        if (loc == null) return null;
        final String[] cords = loc.split(",", 3);
        final double x = Double.parseDouble(cords[0]);
        final double y = Double.parseDouble(cords[1]);
        final double z = Double.parseDouble(cords[2]);
        return new Location(world, x, y, z).getBlock();
    }
}
