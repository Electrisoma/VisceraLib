package net.electrisoma.visceralib.platform.datagen.client;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.datagen.client.services.IDatagenClientHelper;
import net.minecraft.tags.TagKey;

@AutoService(IDatagenClientHelper.class)
public class DatagenClientHelperImpl implements IDatagenClientHelper {

    @Override
    public String getTagTranslationKey(TagKey<?> tagKey) {
        return tagKey.getTranslationKey();
    }
}
