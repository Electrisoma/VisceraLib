package net.electrisoma.visceralib.platform.datagen.v1.client;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.datagen.v1.client.services.IDatagenClientHelper;
import net.minecraft.tags.TagKey;

@AutoService(IDatagenClientHelper.class)
public class DatagenClientHelperImpl implements IDatagenClientHelper {

    @Override
    public String getTagTranslationKey(TagKey<?> tagKey) {
        return tagKey.getTranslationKey();
    }
}
