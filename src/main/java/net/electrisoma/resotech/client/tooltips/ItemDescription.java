package net.electrisoma.resotech.client.tooltips;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ItemDescription {
    private final List<String> summaryLines = new ArrayList<>();

    public static ItemDescription fromTranslationKey(String keyBase) {
        ItemDescription desc = new ItemDescription();

        if (I18n.exists(keyBase + ".summary")) {
            desc.summaryLines.add(I18n.get(keyBase + ".summary"));
        }

        return desc;
    }

    public List<Component> getTooltipLines() {
        List<Component> result = new ArrayList<>();
        for (String line : summaryLines) {
            result.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }
        return result;
    }
}