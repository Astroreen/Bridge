package bridge.utils;

import bridge.Bridge;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileUtils {

    private final static File root = Bridge.getInstance().getDataFolder();

    /**
     * Creates directory from plugin data folder.
     *
     * @param directory directory path (ex: "/dir/")
     * @return true if successful
     */
    @Contract(pure = true)
    public static boolean createDirectory(final @NotNull String directory) {
        if(!isValidPath(directory)) return false;

        File dir = new File(root + directory);
        if (!dir.exists()) dir.mkdirs();
        return true;
    }

    /**
     * Check if path is valid. (Valid ex: "/dir/", "/dir/anotherD1r/")
     * @param path string path
     * @return true if valid
     */
    public static boolean isValidPath(final @NotNull String path){
        return path.matches("^/$|^((/([a-zA-Z0-9_-]+))+)/$");
    }
}
