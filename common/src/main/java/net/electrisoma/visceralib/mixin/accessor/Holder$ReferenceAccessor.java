package net.electrisoma.visceralib.mixin.accessor;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface Holder$ReferenceAccessor<T> {

    @Accessor("owner")
    HolderOwner<T> visceral$getOwner();

    @Invoker("bindValue")
    void visceral$bindValue(T value);
}
