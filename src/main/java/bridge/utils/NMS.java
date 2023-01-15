package bridge.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class NMS {

    public static Version getVersion() {
        final String version = getRawVersion();
        return switch (version) {
            case "1.18.1" -> Version.v1_18_1;
            case "1.18.2" -> Version.v1_18_2;
            case "1.19" -> Version.v1_19;
            case "1.19.1" -> Version.v1_19_1;
            case "1.19.2" -> Version.v1_19_2;
            case "1.19.3" -> Version.v1_19_3;
            default -> Version.NOT_FOUND;
        };
    }

    public static String getRawVersion() {
        return Bukkit.getBukkitVersion().split("-")[0]; //returns something like 1.19.1-R0.1-SNAPSHOT
    }

    public static boolean isYoungerThan(final @NotNull Version than){
        return getVersion().weight > than.weight;
    }

    public enum Version {
        NOT_FOUND(1),
        v1_18_1(1181),
        v1_18_2(1182),
        v1_19(1190),
        v1_19_1(1191),
        v1_19_2(1192),
        v1_19_3(1193),
        ;

        final int weight;

        Version(int weight) {
            this.weight = weight;
        }
    }
}
