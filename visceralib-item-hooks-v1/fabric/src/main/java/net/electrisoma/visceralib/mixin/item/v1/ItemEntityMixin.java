package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.EntityHook;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

	@Shadow public abstract ItemStack getItem();

	public ItemEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyConstant(method = "tick", constant = @Constant(intValue = 6000))
	private int viscera$modifyLifespan(int original) {
		EntityHook hook = this.getItem().getItem();
		return hook.viscera$getEntityDespawn(this.getItem(), this.level());
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void viscera$handleCustomEntitySwap(CallbackInfo ci) {
		if (this.level().isClientSide || this.isRemoved()) return;
		if (this.tickCount == 0) {
			EntityHook hook = this.getItem().getItem();
			if (hook.viscera$customItemEntity(this.getItem())) {
				Entity newEntity = hook.viscera$createEntity(this.level(), this, this.getItem());

				if (newEntity != null) {
					newEntity.copyPosition(this);
					newEntity.setDeltaMovement(this.getDeltaMovement());
					this.level().addFreshEntity(newEntity);
					this.discard();
					ci.cancel();
				}
			}
		}
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void viscera$bridgeOnEntityItemUpdate(CallbackInfo ci) {
		if (this.isRemoved()) return;
		EntityHook hook = this.getItem().getItem();
		if (hook.viscera$onEntityItemUpdate(this.getItem(), (ItemEntity) (Object) this)) {
			ci.cancel();
		}
	}
}
