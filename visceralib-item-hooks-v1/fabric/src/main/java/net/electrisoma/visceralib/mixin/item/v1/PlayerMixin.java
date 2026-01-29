package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.DropHook;
import net.electrisoma.visceralib.api.item.v1.EntityHook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void viscera$bridgeLeftClickHook(Entity target, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof EntityHook hook) {
            if (hook.viscera$onEntityAttack(stack, player, target))
                ci.cancel();
        }
    }

    @Inject(
            method = "drop(Lnet/minecraft/world/item/ItemStack;" +
                    "ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void viscera$bridgeDropHook(
            ItemStack stack,
            boolean throwRandomly,
            boolean retainOwnership,
            CallbackInfoReturnable<ItemEntity> cir
    ) {
        DropHook hook = stack.getItem();
        Player player = (Player) (Object) this;
        if (!hook.viscera$canPlayerDrop(stack, player))
            cir.setReturnValue(null);
    }
}