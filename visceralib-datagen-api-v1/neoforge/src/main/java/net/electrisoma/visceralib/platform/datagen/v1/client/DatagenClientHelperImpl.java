package net.electrisoma.visceralib.platform.datagen.v1.client;

import net.electrisoma.visceralib.platform.datagen.v1.client.services.IDatagenClientHelper;

import net.neoforged.neoforge.common.Tags;

import net.minecraft.tags.TagKey;

import com.google.auto.service.AutoService;

@AutoService(IDatagenClientHelper.class)
public class DatagenClientHelperImpl implements IDatagenClientHelper {

	@Override
	public String getTagTranslationKey(TagKey<?> tagKey) {
		return Tags.getTagTranslationKey(tagKey);
	}
}
