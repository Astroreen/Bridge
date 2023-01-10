package bridge.ffa;

import bridge.Bridge;
import bridge.compatibility.worldedit.WEManager;
import bridge.config.ConfigurationFile;
import bridge.utils.WorldUtils;
import lombok.CustomLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

@CustomLog
public class FFAArenaManager {

    private static Bridge plugin;
    private static ConfigurationFile config;
    private static final List<World> activeArenas = new ArrayList<>();
    private static final HashMap<String, Integer> restarters = new HashMap<>();

    /**
     * Same method as reload.
     *
     * @param config the main ffa config
     */
    public static void setup(final @NotNull Bridge plugin, final @NotNull ConfigurationFile config) {
        FFAArenaManager.plugin = plugin;
        FFAArenaManager.config = config;
        try {
            config.reload();
        } catch (IOException e) {
            LOG.warn("Could not reload ffa-config.yml! " + e.getMessage(), e);
        }
        getActiveFFAWorlds(true);
    }

    /**
     * Unloads all working arenas.
     */
    public static void unloadAllArenas() {
        getActiveFFAWorlds(false).forEach(w -> WorldUtils.unloadWorld(w, false));
    }
    public static void addRestarter(final @NotNull World world){
        if(!getActiveFFAWorlds(false).contains(world)) return;
        final Integer task = launchSchematicRestarter(world);
        if(task == null) return;
        restarters.put(world.getName(), task);
    }

    public static void removeRestarter(final @NotNull String arena){
        if(restarters.containsKey(arena)) Bukkit.getScheduler().cancelTask(restarters.get(arena));
    }

    private static @Nullable Integer launchSchematicRestarter(final @NotNull World world) {
        if(!getActiveFFAWorlds(false).contains(world)) return null;
        final String arena = world.getName();
        if(isArenaDisabled(arena)) return null;
        if(!haveSchematic(arena)) return null;

        final int minutes = config.getInt(String.format("arenas.%s.schematic-restart", arena), 30) * 1200;

        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            //pastes schematic
            final String schem = FFAArenaManager.getSchematicFileName(world.getName());
            final Location location = FFAArenaManager.getSchematicLocation(world.getName());
            if (location == null) return;
            WEManager.pasteSchematicAsync(location, FFAArenaManager.getFFASchematicFolder(), schem);
        }, minutes, minutes).getTaskId();
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
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        if (!config.getBoolean(String.format("arenas.%s.teleport.rtp-enabled", arena))) return getDefaultTeleportPoint(arena);
        final List<Location> locations = new ArrayList<>();
        getTeleportPoints(arena).forEach((name, loc) -> locations.add(loc));
        if(locations.isEmpty()) return null;
        return locations.get(new Random().nextInt(locations.size()));
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
        if (!getActiveFFAWorlds(false).contains(world)) return new HashMap<>();

        //if arena teleport locations are disabled, use "default-pos"
        if (!config.getBoolean(String.format("arenas.%s.teleport.enabled", arena), false)) {
            HashMap<String, Location> location = new HashMap<>(1);
            Location loc = getDefaultTeleportPoint(arena);
            location.put("default", loc);
            if (loc != null) return location;
            else return new HashMap<>();
        }

        final HashMap<String, Location> locations = new HashMap<>();
        final Set<String> names = getTeleportPointsName(arena);
        for (final String name : names) {
            final Location loc = getTeleportPoint(arena, name);
            if (loc != null) locations.put(name, loc);
        }
        return locations;
    }

    public static @NotNull Set<String> getTeleportPointsName(final @NotNull String arena){
        if(isArenaDisabled(arena)) return new HashSet<>();
        if(!getExistingFFAWorlds().contains(arena)) return new HashSet<>();

        //if arena teleport locations are disabled, use "default-pos"
        if (!config.getBoolean(String.format("arenas.%s.teleport.enabled", arena), false)) {
            Set<String> location = new HashSet<>(1);
            Location loc = getDefaultTeleportPoint(arena);
            location.add("default");
            if (loc != null) return location;
            else return new HashSet<>();
        }

        final ConfigurationSection section =
                config.getConfigurationSection(String.format("arenas.%s.teleport", arena));
        if (section == null) return new HashSet<>();
        final Set<String> pos = section.getKeys(false);
        pos.remove("enabled");
        pos.remove("rtp-enabled");
        final Set<String> locations = new HashSet<>();
        locations.add("default");
        locations.addAll(pos);
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

    public static @Nullable Location getDefaultTeleportPoint(final @NotNull String arena) {
        if (isArenaDisabled(arena)) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final String def = config.getString(String.format("arenas.%s.default-pos", arena));
        if (def != null) {
            final String[] pos = def.split(",");
            final double x = Double.parseDouble(pos[0]);
            final double y = Double.parseDouble(pos[1]);
            final double z = Double.parseDouble(pos[2]);
            return new Location(world, x, y, z);
        }
        return null;
    }

    /**
     * Get FFA worlds that is working right now.
     *
     * @return list of active FFA worlds
     */
    public static @NotNull List<World> getActiveFFAWorlds(final boolean reload) {
        if (!reload) return activeArenas;
        final List<World> worlds = new ArrayList<>();
        getExistingFFAWorlds().forEach(name -> {
            final World world = WorldUtils.getWorld(name);
            if (world != null) worlds.add(world);
        });
        activeArenas.clear();
        activeArenas.addAll(worlds);
        return activeArenas;
    }

    /**
     * Get name of the world that is FFA worlds
     * and also checks if these worlds even exist.
     * <p>
     * <b>ATTENTION:</b> worlds can be unloaded!
     *
     * @return list of FFA worlds names.
     */
    public static @NotNull List<String> getExistingFFAWorlds() {
        final List<String> list = getAllFFAWorlds();
        final List<String> worlds = new ArrayList<>(list);
        list.forEach(name -> {
            if (!WorldUtils.isWorldFolderExist(name)) worlds.remove(name);
        });
        return worlds;
    }

    /**
     * Get all FFA worlds names from config.
     *
     * @return list of names.
     */
    private static @NotNull List<String> getAllFFAWorlds() {
        final ConfigurationSection section = config.getConfigurationSection("arenas");
        if (section == null) return List.of();
        return new ArrayList<>(section.getKeys(false));
    }

    /**
     * Get FFA folder that contains schematics to use.
     *
     * @return folder containing schematics
     */
    @Contract(" -> new")
    public static @NotNull File getFFASchematicFolder() {
        final File def = WEManager.getDefaultSchematicFolder();
        if (config.getBoolean("use-different-schematic-folder") && def != null) return def;
        else return new File(plugin.getDataFolder(), "/ffa/schematics");
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
     * @return true, if using schematic
     */
    public static boolean haveSchematic(final @NotNull String arena) {
        final String schem = config.getString(String.format("arenas.%s.schematic", arena));
        if (schem == null) return false;
        final String[] split = schem.split("\\.");
        return !schem.equals("none") && split[split.length - 1].equals("schem");
    }

    /**
     * Get location from config there to place schematic.
     *
     * @param arena the name
     * @return null, if arena don't exist/unloaded or can't
     * parse world/location from config. Otherwise, returns {@link Location}.
     */
    public static @Nullable Location getSchematicLocation(final @NotNull String arena) {
        if (!haveSchematic(arena)) return null;
        final World world = WorldUtils.getWorld(arena);
        if (world == null) return null;
        final String loc = config.getString(String.format("arenas.%s.schematic-cords", arena));
        if (loc == null) return null;
        final String[] cords = loc.split(":", 3);
        final double x = Integer.parseInt(cords[0]);
        final double y = Integer.parseInt(cords[1]);
        final double z = Integer.parseInt(cords[2]);
        return new Location(world, x, y, z);
    }

    /**
     * Return arena's schematic name.
     *
     * @param arena the arena name
     * @return "none" if schematic wasn't found.
     * Otherwise, schematics file name.
     */
    public static @NotNull String getSchematicFileName(final @NotNull String arena) {
        return config.getString(String.format("arenas.%s.schematic", arena), "none");
    }
}
