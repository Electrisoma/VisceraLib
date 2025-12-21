package net.electrisoma.visceralib.platform.datagen;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.datagen.services.IDatagenHelper;
import net.minecraft.tags.TagKey;

@AutoService(IDatagenHelper.class)
public class DatagenHelperImpl implements IDatagenHelper {

    @Override
    public String getTagTranslationKey(TagKey<?> tagKey) {
        return tagKey.getTranslationKey();
    }
}
