package bridge.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ColorCodes {
    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    private ColorCodes () {}

    public static boolean isHexValid (@NotNull String color) {
        return color.matches("^#([A-Fa-f\\d]{6})$");
    }

    /**
     * Translate all colors and return as Kyori Adventure {@link TextComponent}
     *
     * @param text original text
     * @return {@link TextComponent} if all color codes are translatable
     * @throws IllegalStateException if in color code aren't recognizable
     */
    public static @NotNull TextComponent translateToTextComponent(@NotNull String text)
            throws IllegalStateException {

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        TextComponent.Builder builder = Component.text();
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                TextComponent component = Component.empty();
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    component = component.content(texts[i].substring(7))
                            .color(TextColor.fromHexString(texts[i].substring(0, 7)));
                    builder.append(component);
                } else {
                    if (texts[i].length() > 1) {
                        component = component.content(texts[i].substring(1));
                    } else {
                        component = component.content(" ");
                    }

                    Style style = switch (texts[i].charAt(0)) {
                        case '0' -> Style.style(TextColor.fromHexString("#000000"));
                        case '1' -> Style.style(TextColor.fromHexString("#0000AA"));
                        case '2' -> Style.style(TextColor.fromHexString("#00AA00"));
                        case '3' -> Style.style(TextColor.fromHexString("#00AAAA"));
                        case '4' -> Style.style(TextColor.fromHexString("#AA0000"));
                        case '5' -> Style.style(TextColor.fromHexString("#AA00AA"));
                        case '6' -> Style.style(TextColor.fromHexString("#FFAA00"));
                        case '7' -> Style.style(TextColor.fromHexString("#AAAAAA"));
                        case '8' -> Style.style(TextColor.fromHexString("#555555"));
                        case '9' -> Style.style(TextColor.fromHexString("#5555FF"));
                        case 'a' -> Style.style(TextColor.fromHexString("#55FF55"));
                        case 'b' -> Style.style(TextColor.fromHexString("#55FFFF"));
                        case 'c' -> Style.style(TextColor.fromHexString("#FF5555"));
                        case 'd' -> Style.style(TextColor.fromHexString("#FF55FF"));
                        case 'e' -> Style.style(TextColor.fromHexString("#FFFF55"));
                        case 'f' -> Style.style(TextColor.fromHexString("#FFFFFF"));
                        case 'k' -> Style.style(TextDecoration.OBFUSCATED);
                        case 'l' -> Style.style(TextDecoration.BOLD);
                        case 'm' -> Style.style(TextDecoration.STRIKETHROUGH);
                        case 'n' -> Style.style(TextDecoration.UNDERLINED);
                        case 'o' -> Style.style(TextDecoration.ITALIC);
                        case 'r' -> Style.style(TextColor.fromHexString("#FFFFFF"));
                        default -> throw new IllegalStateException("Unexpected value: " + texts[i].charAt(0));
                    };
                    component = component.style(style);
                    builder.append(component);
                }
            } else builder.append(Component.text(texts[i]));
        }
        return builder.build();
    }

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    @Deprecated
    public static @NotNull String translate(@NotNull String text) {

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }
}
