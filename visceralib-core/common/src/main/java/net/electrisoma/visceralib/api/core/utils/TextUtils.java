package net.electrisoma.visceralib.api.core.utils;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class for common string manipulations and case conversions.
 */
public class TextUtils {

	/** "camelCase" */
	public static String toCamelCase(String text) {
		String[] words = splitWords(text);
		return words.length != 0 ? IntStream
				.range(1, words.length)
				.mapToObj(i -> capitalize(words[i]))
				.collect(Collectors.joining("", words[0].toLowerCase(Locale.ROOT), "")) : "";
	}

	/** "camelCase" with smart word boundary detection */
	public static String toSmartCamelCase(String text) {
		return toCamelCase(smartSplit(text));
	}

	/** "PascalCase" */
	public static String toPascalCase(String text) {
		return Arrays.stream(splitWords(text))
				.map(TextUtils::capitalize)
				.collect(Collectors.joining());
	}

	/** "PascalCase" with smart word boundary detection */
	public static String toSmartPascalCase(String text) {
		return toPascalCase(smartSplit(text));
	}

	/** "snake_case" */
	public static String toSnakeCase(String text) {
		return String.join("_", splitWords(text)).toLowerCase(Locale.ROOT);
	}

	/** "snake_case" with smart word boundary detection */
	public static String toSmartSnakeCase(String text) {
		return toSnakeCase(smartSplit(text));
	}

	/** "kebab-case" */
	public static String toKebabCase(String text) {
		return String.join("-", splitWords(text)).toLowerCase(Locale.ROOT);
	}

	/** "kebab-case" with smart word boundary detection */
	public static String toSmartKebabCase(String text) {
		return toKebabCase(smartSplit(text));
	}

	/** "SCREAMING-SNAKE-CASE" */
	public static String toScreamingSnakeCase(String text) {
		return String.join("_", splitWords(text)).toUpperCase(Locale.ROOT);
	}

	/** "SCREAMING-SNAKE-CASE" with smart word boundary detection */
	public static String toSmartScreamingSnakeCase(String text) {
		return toScreamingSnakeCase(smartSplit(text));
	}

	/** "Train-Case" */
	public static String toTrainCase(String text) {
		return Arrays.stream(splitWords(text))
				.map(TextUtils::capitalize)
				.collect(Collectors.joining("-"));
	}

	/** "Train-Case" with smart word boundary detection */
	public static String toSmartTrainCase(String text) {
		return toTrainCase(smartSplit(text));
	}

	/** "Title Case" */
	public static String toTitleCase(String text) {
		return Arrays.stream(splitWords(text))
				.map(TextUtils::capitalize)
				.collect(Collectors.joining(" "));
	}

	/** "Title Case" with smart word boundary detection */
	public static String toSmartTitleCase(String text) {
		return toTitleCase(smartSplit(text));
	}

	/** "UPPER CASE" */
	public static String toUpperCase(String text) {
		return nonNull(text).toUpperCase(Locale.ROOT);
	}

	/** "ndsᴉp pown" */
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

	/** abbreviates text */
	public static String abbreviate(String text, int maxLength) {
		String input = nonNull(text);
		return input.length() <= maxLength ? input :
				input.substring(0, Math.max(0, maxLength - 3)) + "...";
	}

	/** simplifies text to be single-spaced */
	public static String simplify(String text) {
		return nonNull(text).trim().replaceAll("\\s+", " ");
	}

	/** strips Minecraft §-style and {@code <tag>}-style formatting */
	public static String stripFormatting(String text) {
		return nonNull(text)
				.replaceAll("§[0-9a-fk-orA-FK-ORxX]", "")
				.replaceAll("<[^>]*>", "");
	}

	/** splits text into individual words based on case changes, underscores, or hyphens */
	private static String[] splitWords(String text) {
		return nonNull(text)
				.replaceAll("([a-z])([A-Z])", "$1 $2")
				.replace("_", " ")
				.replace("-", " ")
				.trim()
				.split("\\s+");
	}

	/**
	 * Injects spaces into a single lowercase string by identifying the most
	 * likely compound word boundary (e.g., "neoforge" -> "neo forge").
	 */
	private static String smartSplit(String text) {
		String input = nonNull(text).toLowerCase(Locale.ROOT);
		if (input.length() < 5) return input;

		String spaced = input.replaceAll("(?i)(.{3,})(lib|api|core|mod|forge|craft|work|tool|util|ext|ui)$", "$1 $2");
		if (!spaced.equals(input)) return spaced;

		int pivot = -1;
		var matcher = Pattern.compile("[aeiouy][^aeiouy][aeiouy]").matcher(input);
		while (matcher.find())
			pivot = matcher.start() + 1;

		return pivot > 2 && pivot < input.length() - 2 ?
				input.substring(0, pivot) + " " + input.substring(pivot) : input;
	}

	/** capitalizes the first letter and lowercases the rest */
	private static String capitalize(String text) {
		return text.isEmpty() ? "" :
				text.substring(0, 1).toUpperCase(Locale.ROOT) +
				text.substring(1).toLowerCase(Locale.ROOT);
	}

	/** returns an empty string if the input is null */
	private static String nonNull(String text) {
		return text == null ? "" : text;
	}
}
