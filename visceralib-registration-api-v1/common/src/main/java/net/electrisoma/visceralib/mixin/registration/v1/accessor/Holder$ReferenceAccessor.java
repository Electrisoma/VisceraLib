package net.electrisoma.visceralib.mixin.registration.v1.accessor;

import net.minecraft.core.Holder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface Holder$ReferenceAccessor<T> {

	@Invoker("bindValue")
	void viscera$bindValue(T value);
}
