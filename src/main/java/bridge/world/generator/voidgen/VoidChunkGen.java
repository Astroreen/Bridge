package bridge.world.generator.voidgen;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.CustomLog;
import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

@CustomLog(topic = "VoidChunkGen")
public class VoidChunkGen extends ChunkGen {

    public VoidChunkGen(JavaPlugin javaPlugin, String paramIdentifier) {
        super(javaPlugin);
        Gson gson = new Gson();

        if (StringUtils.isBlank(paramIdentifier)) {
            this.chunkGenSettings = new ChunkGenSettings();
            LOG.info("Generator settings have not been set. Using default values:");
        } else {
            try {
                this.chunkGenSettings = gson.fromJson(paramIdentifier, ChunkGenSettings.class);
            } catch (JsonSyntaxException jse) {
                this.chunkGenSettings = new ChunkGenSettings();
                LOG.info("Generator settings \"" + paramIdentifier + "\" syntax is not valid. Using default values:");
            }
        }
        // Posting the currently used chunkGenSettings to console.
        LOG.info(gson.toJson(chunkGenSettings));
    }
}