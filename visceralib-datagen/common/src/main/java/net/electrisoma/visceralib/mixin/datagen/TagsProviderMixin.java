package net.electrisoma.visceralib.mixin.datagen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.electrisoma.visceralib.api.datagen.providers.VisceralTagProvider;
import net.minecraft.data.tags.TagsProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TagsProvider.class)
public abstract class TagsProviderMixin {

    @ModifyReturnValue(
            method = "getName",
            at = @At("RETURN")
    )
    private String visceralib$customName(String original) {
        return (Object) this instanceof VisceralTagProvider<?> provider
                ? provider.visceral$getName()
                : original;
    }
}