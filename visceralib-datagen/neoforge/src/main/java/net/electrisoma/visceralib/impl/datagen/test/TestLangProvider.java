package net.electrisoma.visceralib.impl.datagen.test;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class TestLangProvider extends LanguageProvider {

    public TestLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add(ModItems.BEST_ITEM.get(), "thing_item");
    }
}
