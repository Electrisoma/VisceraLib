package net.electrisoma.visceralib.platform.datagen.v1.client;

import net.electrisoma.visceralib.platform.datagen.v1.client.services.IDatagenClientHelper;

import net.minecraft.tags.TagKey;

import com.google.auto.service.AutoService;

@AutoService(IDatagenClientHelper.class)
public final class DatagenClientHelperImpl implements IDatagenClientHelper {

	@Override
	public String getTagTranslationKey(TagKey<?> tagKey) {
		return tagKey.getTranslationKey();
	}
}
