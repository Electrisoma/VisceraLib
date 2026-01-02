package net.electrisoma.visceralib.mixin.datagen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.electrisoma.visceralib.api.datagen.providers.VisceralTagProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.List;

@Mixin(TagsProvider.TagAppender.class)
public abstract class TagAppenderMixin<T> {

    @Unique @SuppressWarnings("unchecked")
    private VisceralTagProvider.VisceralTagBuilder<T> visceralib$asVisceral() {
        return (Object) this instanceof VisceralTagProvider.VisceralTagBuilder<?> builder
                ? (VisceralTagProvider.VisceralTagBuilder<T>) builder
                : null;
    }

    @ModifyReturnValue(
            method = "add(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/data/tags/TagsProvider$TagAppender;",
            at = @At("RETURN")
    )
    private TagsProvider.TagAppender<T> visceralib$interceptAdd(
            TagsProvider.TagAppender<T> original,
            ResourceKey<T> key
    ) {
        var builder = visceralib$asVisceral();
        return builder != null
                ? builder.visceral$add(key)
                : original;
    }

    @ModifyReturnValue(
            method = "add([Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/data/tags/TagsProvider$TagAppender;",
            at = @At("RETURN")
    )
    private TagsProvider.TagAppender<T> visceralib$interceptAddVarargs(
            TagsProvider.TagAppender<T> original,
            ResourceKey<T>[] keys
    ) {
        var builder = visceralib$asVisceral();
        if (builder == null)
            return original;

        Arrays.stream(keys).forEach(builder::visceral$add);
        return builder;
    }

    @ModifyReturnValue(
            method = "addAll(Ljava/util/List;)Lnet/minecraft/data/tags/TagsProvider$TagAppender;",
            at = @At("RETURN")
    )
    private TagsProvider.TagAppender<T> visceralib$interceptAddAll(
            TagsProvider.TagAppender<T> original,
            List<ResourceKey<T>> keys
    ) {
        var builder = visceralib$asVisceral();
        if (builder == null)
            return original;

        keys.forEach(builder::visceral$add);
        return builder;
    }
}
