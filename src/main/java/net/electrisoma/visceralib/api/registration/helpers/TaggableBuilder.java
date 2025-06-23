package net.electrisoma.visceralib.api.registration.helpers;

import net.minecraft.tags.TagKey;
import java.util.List;

public interface TaggableBuilder<T> {
    TaggableBuilder<T> tag(TagKey<T> tag);

    TaggableBuilder<T> tags(TagKey<T>... tags);

    List<TagKey<T>> getTags();
}
