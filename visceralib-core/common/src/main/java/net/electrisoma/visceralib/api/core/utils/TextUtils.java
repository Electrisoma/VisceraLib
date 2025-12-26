package net.electrisoma.visceralib.api.core.utils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TextUtils {

    // "camelCase"
    public static String toCamelCase(String text) {
        String[] words = splitWords(text);
        return words.length == 0 ? "" : IntStream
                .range(1, words.length)
                .mapToObj(i -> capitalize(words[i]))
                .collect(Collectors.joining("", words[0].toLowerCase(Locale.ROOT), ""));
    }

    // "PascalCase"
    public static String toPascalCase(String text) {
        return Arrays.stream(splitWords(text))
                .map(TextUtils::capitalize)
                .collect(Collectors.joining());
    }

    // "snake_case"
    public static String toSnakeCase(String text) {
        return String.join("_", splitWords(text)).toLowerCase(Locale.ROOT);
    }

    // "kebab-case"
    public static String toKebabCase(String text) {
        return String.join("-", splitWords(text)).toLowerCase(Locale.ROOT);
    }

    // "SCREAMING-SNAKE-CASE"
    public static String toScreamingSnakeCase(String text) {
        return String.join("_", splitWords(text)).toUpperCase(Locale.ROOT);
    }

    // "Train-Case"
    public static String toTrainCase(String text) {
        return Arrays.stream(splitWords(text))
                .map(TextUtils::capitalize)
                .collect(Collectors.joining("-"));
    }

    // "Title Case"
    public static String toTitleCase(String text) {
        return Arrays.stream(splitWords(text))
                .map(TextUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    // "UPPER CASE"
    public static String toUpperCase(String text) {
        return nonNull(text).toUpperCase(Locale.ROOT);
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
        return input.length() <= maxLength ? input : input.substring(0, Math.max(0, maxLength - 3)) + "...";
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

    private static String[] splitWords(String text) {
        return nonNull(text)
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replace("_", " ")
                .replace("-", " ")
                .trim()
                .split("\\s+");
    }

    private static String capitalize(String text) {
        return text.isEmpty() ? "" : text.substring(0, 1).toUpperCase(Locale.ROOT) +
                text.substring(1).toLowerCase(Locale.ROOT);
    }

    private static String nonNull(String text) {
        return text == null ? "" : text;
    }
}
