package net.electrisoma.visceralib.platform.datagen.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.minecraft.tags.TagKey;

public interface IDatagenHelper {

    IDatagenHelper INSTANCE = ServiceHelper.load(IDatagenHelper.class);

    String getTagTranslationKey(TagKey<?> tagKey);
}
