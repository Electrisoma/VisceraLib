package net.electrisoma.visceralib.mixin.item.v1;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.electrisoma.visceralib.api.item.v1.*;
import net.electrisoma.visceralib.api.item.v1.client.ArmorTextureHook;
import net.electrisoma.visceralib.api.item.v1.client.BlockResetHook;
import net.electrisoma.visceralib.api.item.v1.client.HighlightTipHook;
import net.electrisoma.visceralib.api.item.v1.client.ReequipAnimationHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IItemExtension.class)
public interface IItemExtensionMixin {

    /*
    * TODO
    *  canPerformAction
    *  canContinueUsing
    *  getCreatorModId
    *  getBurnTime
    *  onAnimalArmorTick
    *  onDestroyed
    *  isEnderMask
    *  canElytraFly
    *  elytraFlightTick
    *  canWalkOnPowderedSnow
    *  getSweepHitBox
    *  getFoodProperties
    *  isNotReplaceableByPickAction
    *  canGrindstoneRepair
    *  canBeHurtBy
    *  applyEnchantments
    *  canFitInsideContainerItems
    */

    @ModifyReturnValue(method = "onDroppedByPlayer", at = @At("RETURN"))
    default boolean viscera$bridgeOnDroppedByPlayerHook(
            boolean original,
            ItemStack stack,
            Player player
    ) {
        if ((Object) this instanceof DropHook hook) {
            return hook.viscera$canPlayerDrop(stack, player);
        }
        return original;
    }

    @ModifyReturnValue(method = "getHighlightTip", at = @At("RETURN"))
    default Component viscera$bridgeHighlightTipHook(
            Component original,
            ItemStack stack,
            Component displayName
    ) {
        if ((Object) this instanceof HighlightTipHook hook) {
            return hook.viscera$getHighlightedName(stack, displayName);
        }
        return original;
    }

    @ModifyReturnValue(method = "onItemUseFirst", at = @At("RETURN"))
    default InteractionResult viscera$bridgeItemUseFirstHook(
            InteractionResult original,
            ItemStack stack,
            UseOnContext context
    ) {
        if ((Object) this instanceof UseHook hook) {
            InteractionResult result = hook.viscera$onPreBlockInteraction(stack, context);
            return result != InteractionResult.PASS ? result : original;
        }
        return original;
    }

    @Inject(method = "onStopUsing", at = @At("RETURN"))
    default void viscera$bridgeOnStopUsingHook(
            ItemStack stack,
            LivingEntity entity,
            int count,
            CallbackInfo ci
    ) {
        if ((Object) this instanceof UseHook hook) {
            hook.viscera$onPostBlockInteraction(stack, entity, count);
        }
    }

    @ModifyReturnValue(method = "isPiglinCurrency", at = @At("RETURN"))
    default boolean viscera$bridgePiglinCurrencyHook(
            boolean original,
            ItemStack stack
    ) {
        if ((Object) this instanceof PiglinHook hook) {
            return hook.viscera$isBarterCurrency(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "makesPiglinsNeutral", at = @At("RETURN"))
    default boolean viscera$bridgePiglinNeutralityHook(
            boolean original,
            ItemStack stack,
            LivingEntity wearer
    ) {
        if ((Object) this instanceof PiglinHook hook) {
            return hook.viscera$doesPacifyPiglin(stack, wearer);
        }
        return original;
    }

    /// isRepairable() is handled in {@link NeoForgeItemMixin} due to being abstract

    @ModifyReturnValue(method = "getXpRepairRatio", at = @At("RETURN"))
    default float viscera$bridgeXpRepairRatioHook(
            float original,
            ItemStack stack
    ) {
        if ((Object) this instanceof DurabilityHook hook) {
            return hook.viscera$getMendingEfficiency(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "onLeftClickEntity", at = @At("RETURN"))
    default boolean viscera$bridgeLeftClickEntityHook(
            boolean original,
            ItemStack stack,
            Player player,
            Entity entity
    ) {
        if ((Object) this instanceof EntityHook hook) {
            return hook.viscera$onEntityAttack(stack, player, entity) || original;
        }
        return original;
    }

    @ModifyReturnValue(method = "getCraftingRemainingItem", at = @At("RETURN"))
    default ItemStack viscera$bridgeCraftingRemainingHook(
            ItemStack original,
            ItemStack stack
    ) {
        if ((Object) this instanceof CraftingRemainderHook hook) {
            return hook.viscera$getCraftingRemaining(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "getEntityLifespan", at = @At("RETURN"))
    default int viscera$bridgeEntityLifespanHook(
            int original,
            ItemStack stack,
            Level level
    ) {
        if ((Object) this instanceof EntityHook hook) {
            return hook.viscera$getEntityDespawn(stack, level);
        }
        return original;
    }

    @ModifyReturnValue(method = "hasCustomEntity", at = @At("RETURN"))
    default boolean viscera$bridgeHasCustomEntityHook(
            boolean original,
            ItemStack stack
    ) {
        if ((Object) this instanceof EntityHook hook) {
            return hook.viscera$customItemEntity(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "createEntity", at = @At("RETURN"))
    default @Nullable Entity viscera$bridgeCreateEntityHook(
            @Nullable Entity original,
            Level level,
            Entity location,
            ItemStack stack
    ) {
        if ((Object) this instanceof EntityHook hook) {
            Entity custom = hook.viscera$createEntity(level, location, stack);
            return custom != null ? custom : original;
        }
        return original;
    }

    @ModifyReturnValue(method = "onEntityItemUpdate", at = @At("RETURN"))
    default boolean viscera$bridgeEntityItemUpdateHook(
            boolean original,
            ItemStack stack,
            ItemEntity entity
    ) {
        if ((Object) this instanceof EntityHook hook) {
            return original || hook.viscera$onEntityItemUpdate(stack, entity);
        }
        return original;
    }

    @ModifyReturnValue(method = "doesSneakBypassUse", at = @At("RETURN"))
    default boolean viscera$bridgeSneakBypassHook(
            boolean original,
            ItemStack stack,
            LevelReader level,
            BlockPos pos,
            Player player
    ) {
        if ((Object) this instanceof UseHook hook) {
            return hook.viscera$shouldBypassSneak(stack, level, pos, player);
        }
        return original;
    }

    @ModifyReturnValue(method = "canEquip", at = @At("RETURN"))
    default boolean viscera$bridgeCanEquipHook(
            boolean original,
            ItemStack stack,
            EquipmentSlot armorType,
            LivingEntity entity
    ) {
        if ((Object) this instanceof EquipmentHook hook) {
            return hook.viscera$canFitSlot(stack, armorType, entity);
        }
        return original;
    }

    @ModifyReturnValue(method = "getEquipmentSlot", at = @At("RETURN"))
    default @Nullable EquipmentSlot viscera$bridgeEquipmentSlotHook(
            @Nullable EquipmentSlot original,
            ItemStack stack
    ) {
        if ((Object) this instanceof EquipmentHook hook) {
            EquipmentSlot custom = hook.viscera$getDesiredSlot(stack);
            return custom != null ? custom : original;
        }
        return original;
    }

    @ModifyReturnValue(method = "isBookEnchantable", at = @At("RETURN"))
    default boolean viscera$bridgeIsBookEnchantableHook(
            boolean original,
            ItemStack stack,
            ItemStack book
    ) {
        if ((Object) this instanceof EnchantmentHook hook) {
            return hook.viscera$isEnchantableWithBook(stack, book);
        }
        return original;
    }

    @ModifyReturnValue(method = "getArmorTexture", at = @At("RETURN"))
    private @Nullable ResourceLocation viscera$bridgeArmorTextureHook(
            @Nullable ResourceLocation original,
            ItemStack stack,
            Entity entity,
            EquipmentSlot slot,
            ArmorMaterial.Layer layer,
            boolean innerModel
    ) {
        if ((Object) this instanceof ArmorTextureHook hooks) {
            ResourceLocation texture = hooks.viscera$getArmorTexture(stack, entity, slot, layer, innerModel);
            if (texture != null)
                return texture;
        }
        return original;
    }

    @ModifyReturnValue(method = "getDamage", at = @At("RETURN"))
    default int viscera$bridgeGetDamageHook(
            int original,
            ItemStack stack
    ) {
        if ((Object) this instanceof DurabilityHook hook) {
            return hook.viscera$getDamageValue(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "getMaxDamage", at = @At("RETURN"))
    default int viscera$bridgeGetMaxDamageHook(
            int original,
            ItemStack stack
    ) {
        if ((Object) this instanceof DurabilityHook hook) {
            return hook.viscera$getMaxDurability(stack);
        }
        return original;
    }

    @Inject(method = "setDamage", at = @At("HEAD"), cancellable = true)
    default void viscera$bridgeSetDamageHook(
            ItemStack stack,
            int damage,
            CallbackInfo ci
    ) {
        if ((Object) this instanceof DurabilityHook hook) {
            hook.viscera$setDamageValue(stack, damage);
            ci.cancel();
        }
    }

    @ModifyReturnValue(method = "isDamaged", at = @At("RETURN"))
    default boolean viscera$bridgeIsDamagedHook(
            boolean original,
            ItemStack stack
    ) {
        if ((Object) this instanceof DurabilityHook hook) {
            return hook.viscera$hasWear(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "getMaxStackSize", at = @At("RETURN"))
    default int viscera$bridgeMaxStackSize(int original, ItemStack stack) {
        if ((Object) this instanceof StackHook hook) {
            return hook.viscera$getStackLimit(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "getEnchantmentValue(Lnet/minecraft/world/item/ItemStack;)I", at = @At("RETURN"))
    default int viscera$bridgeEnchantmentValue(int original, ItemStack stack) {
        if ((Object) this instanceof EnchantmentHook hook) {
            return hook.viscera$getEnchantmentValue(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "isPrimaryItemFor", at = @At("RETURN"))
    default boolean viscera$bridgePrimaryEnchantmentHook(
            boolean original,
            ItemStack stack,
            Holder<Enchantment> enchantment
    ) {
        if ((Object) this instanceof EnchantmentHook hook) {
            return hook.viscera$isCompatibleWithEnchantment(stack, enchantment, true);
        }
        return original;
    }

    @ModifyReturnValue(method = "supportsEnchantment", at = @At("RETURN"))
    default boolean viscera$bridgeSupportsEnchantmentHook(
            boolean original,
            ItemStack stack,
            Holder<Enchantment> enchantment
    ) {
        if ((Object) this instanceof EnchantmentHook hook) {
            return hook.viscera$isCompatibleWithEnchantment(stack, enchantment, false);
        }
        return original;
    }

    @ModifyReturnValue(method = "getEnchantmentLevel", at = @At("RETURN"))
    default int viscera$bridgeGetEnchantmentLevel(
            int original,
            ItemStack stack,
            Holder<Enchantment> enchantment
    ) {
        if ((Object) this instanceof EnchantmentHook hook) {
            return hook.viscera$getEnchantmentLevel(stack, enchantment);
        }
        return original;
    }

    @ModifyReturnValue(method = "getAllEnchantments", at = @At("RETURN"))
    default ItemEnchantments viscera$bridgeGetAllEnchantments(
            ItemEnchantments original,
            ItemStack stack
    ) {
        if ((Object) this instanceof EnchantmentHook hook) {
            return hook.viscera$getAllEnchantments(stack);
        }
        return original;
    }

    @ModifyReturnValue(method = "shouldCauseReequipAnimation", at = @At("RETURN"))
    private boolean viscera$bridgeReequipAnimationHook(
            boolean original,
            ItemStack oldStack,
            ItemStack newStack,
            boolean slotChanged
    ) {
        if ((Object) this instanceof ReequipAnimationHook hooks) {
            return hooks.viscera$shouldAllowReequipAnimation(oldStack, newStack, slotChanged);
        }
        return original;
    }

    @ModifyReturnValue(method = "shouldCauseBlockBreakReset", at = @At("RETURN"))
    private boolean viscera$bridgeBlockResetHook(
            boolean original,
            ItemStack oldStack,
            ItemStack newStack
    ) {
        if ((Object) this instanceof BlockResetHook hooks) {
            if (!original || oldStack.getItem() != newStack.getItem())
                return original;
            if (hooks.viscera$shouldContinueBreaking(oldStack, newStack))
                return false;
        }
        return original;
    }

    @ModifyReturnValue(method = "canDisableShield", at = @At("RETURN"))
    default boolean viscera$bridgeCanDisableShieldHook(
            boolean original,
            ItemStack stack,
            ItemStack shield,
            LivingEntity entity,
            LivingEntity attacker
    ) {
        if ((Object) this instanceof CombatHook hook) {
            return hook.viscera$canDisableShield(stack, shield, entity, attacker);
        }
        return original;
    }

    @ModifyReturnValue(method = "damageItem", at = @At("RETURN"))
    default int viscera$bridgeDamageHook(
            int original,
            ItemStack stack,
            int amount,
            @Nullable LivingEntity entity
    ) {
        if ((Object) this instanceof DurabilityHook hook) {
            return hook.viscera$onCalculateDamage(stack, amount, entity);
        }
        return original;
    }

    @ModifyReturnValue(method = "isDamageable", at = @At("RETURN"))
    default boolean viscera$bridgeIsDamageableHook(
            boolean original,
            ItemStack stack
    ) {
        if ((Object) this instanceof DurabilityHook hook) {
            return hook.viscera$canTakeDamage(stack);
        }
        return original;
    }
}