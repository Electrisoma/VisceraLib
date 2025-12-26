package net.electrisoma.visceralib.platform.datagen.client.services;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.minecraft.tags.TagKey;

public interface IDatagenClientHelper {

    IDatagenClientHelper INSTANCE = ServiceHelper.load(IDatagenClientHelper.class);

    String getTagTranslationKey(TagKey<?> tagKey);
}
