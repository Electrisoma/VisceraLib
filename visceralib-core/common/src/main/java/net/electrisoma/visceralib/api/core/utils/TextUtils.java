package net.electrisoma.visceralib.api.core.utils;

import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class TextUtils {

    // "camelCase"
    public static String toCamelCase(String string) {
        String[] words = splitWords(string);
        if (words.length == 0) return "";
        StringBuilder sb = new StringBuilder(words[0].toLowerCase(Locale.ROOT));
        for (int i = 1; i < words.length; i++)
            sb.append(capitalize(words[i]));
        return sb.toString();
    }

    // "PascalCase"
    public static String toPascalCase(String string) {
        return Arrays.stream(splitWords(string))
                .map(TextUtils::capitalize)
                .collect(Collectors.joining());
    }

    // "snake_case"
    public static String toSnakeCase(String string) {
        return String.join("_", splitWords(string)).toLowerCase(Locale.ROOT);
    }

    // "kebab-case"
    public static String toKebabCase(String string) {
        return String.join("-", splitWords(string)).toLowerCase(Locale.ROOT);
    }

    // "SCREAMING-SNAKE-CASE"
    public static String toScreamingSnakeCase(String string) {
        return String.join("_", splitWords(string)).toUpperCase(Locale.ROOT);
    }

    // "Train-Case"
    public static String toTrainCase(String string) {
        return Arrays.stream(splitWords(string))
                .map(TextUtils::capitalize)
                .collect(Collectors.joining("-"));
    }

    // "Title Case"
    public static String toTitleCase(String string) {
        return Arrays.stream(splitWords(string))
                .map(TextUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    // "UPPER CASE"
    public static String toUpperCase(String string) {
        return nonNull(string).toUpperCase(Locale.ROOT);
    }

    // "ndsᴉp pown"
    public static String toUpsideDown(String text) {
        String input = nonNull(text);
        String normal = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,'?!_;";
        String upside = "ɐqɔpǝɟƃɥᴉɾʞןɯuodbɹsʇnʌʍxʎz∀ᙠƆᗡƎℲ⅁HΙſʞWɯNOԀӨᴚS⊥∩ΛMX⅄Z0ƖᄅƐㄣϛ9ㄥ86˙‘‚¿¡‾؛";

        StringBuilder sb = new StringBuilder();
        for (int i = input.length() - 1; i >= 0; i--) {
            char c = input.charAt(i);
            int index = normal.indexOf(c);
            sb.append(index != -1 ? upside.charAt(index) : c);
        }
        return sb.toString();
    }

    // abbreviates text
    public static String abbreviate(String text, int maxLength) {
        String input = nonNull(text);
        if (input.length() <= maxLength) return input;
        return input.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    // simplifies text to be single-spaced
    public static String simplify(String text) {
        return nonNull(text).trim().replaceAll("\\s+", " ");
    }

    public static String stripFormatting(String text) {
        return nonNull(text)
                .replaceAll("§[0-9a-fk-orA-FK-ORxX]", "")
                .replaceAll("<[^>]*>", "");
    }

    private static String[] splitWords(String string) {
        return nonNull(string)
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replace("_", " ")
                .replace("-", " ")
                .trim()
                .split("\\s+");
    }

    private static String capitalize(String word) {
        if (word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase(Locale.ROOT) +
                word.substring(1).toLowerCase(Locale.ROOT);
    }

    private static String nonNull(String string) {
        return string == null ? "" : string;
    }
}
