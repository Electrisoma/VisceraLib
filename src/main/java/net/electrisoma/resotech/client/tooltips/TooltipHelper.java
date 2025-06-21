package net.electrisoma.resotech.client.tooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TooltipHelper {
    public static final int MAX_WIDTH = 200;

    public static List<Component> wrapText(String input, ChatFormatting color) {
        return wrapText(input, Style.EMPTY.withColor(color.getColor()));
    }

    public static List<Component> wrapText(String input, Style style) {
        List<Component> lines = new ArrayList<>();
        BreakIterator boundary = BreakIterator.getLineInstance(Locale.ROOT);
        boundary.setText(input);

        Font font = Minecraft.getInstance().font;
        StringBuilder currentLine = new StringBuilder();
        int width = 0;

        for (int start = boundary.first(), end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            String word = input.substring(start, end);
            int wordWidth = font.width(word);

            if (width + wordWidth > MAX_WIDTH && !currentLine.isEmpty()) {
                lines.add(Component.literal(currentLine.toString()).withStyle(style));
                currentLine = new StringBuilder();
                width = 0;
            }

            currentLine.append(word);
            width += wordWidth;
        }

        if (!currentLine.isEmpty()) {
            lines.add(Component.literal(currentLine.toString()).withStyle(style));
        }

        return lines;
    }
}
