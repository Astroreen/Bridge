package bridge.world.generator.voidgen;

import com.google.gson.annotations.SerializedName;
import org.bukkit.block.Biome;

public class ChunkGenSettings {

    @SerializedName("biome")
    private final Biome biome = null;

    @SerializedName("caves")
    private final boolean caves = false;

    @SerializedName("decoration")
    private final boolean decoration = false;

    @SerializedName("mobs")
    private final boolean mobs = false;

    @SerializedName("structures")
    private final boolean structures = false;

    @SerializedName("noise")
    private final boolean noise = false;

    @SerializedName("surface")
    private final boolean surface = false;

    @SerializedName("bedrock")
    private final boolean bedrock = false;

    public Biome getBiome() {
        return this.biome;
    }

    public boolean isCaves() {
        return this.caves;
    }

    public boolean isDecoration() {
        return this.decoration;
    }

    public boolean isMobs() {
        return this.mobs;
    }

    public boolean isStructures() {
        return this.structures;
    }

    public boolean isNoise() {
        return this.noise;
    }

    public boolean isSurface() {
        return this.surface;
    }

    public boolean isBedrock() {
        return this.bedrock;
    }
}
