package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.EntityHook;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@ModifyVariable(method = "addFreshEntity", at = @At("HEAD"), argsOnly = true)
	private Entity viscera$swapEntityOnJoin(Entity entity) {
		if (entity instanceof ItemEntity itemEntity && !entity.isRemoved()) {
			ItemStack stack = itemEntity.getItem();

			if (!stack.isEmpty() && stack.getItem() instanceof EntityHook hook) {
				if (hook.viscera$customItemEntity(stack)) {
					Entity newEntity = hook.viscera$createEntity(itemEntity.level(), itemEntity, stack);

					if (newEntity != null) {
						newEntity.copyPosition(itemEntity);
						newEntity.setDeltaMovement(itemEntity.getDeltaMovement());

						return newEntity;
					}
				}
			}
		}

		return entity;
	}
}
