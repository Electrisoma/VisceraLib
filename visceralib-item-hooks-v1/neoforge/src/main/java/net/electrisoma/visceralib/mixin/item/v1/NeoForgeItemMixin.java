package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.*;
import net.electrisoma.visceralib.api.item.v1.client.ext.VisceralClientExtensionsManager;

import net.neoforged.neoforge.common.extensions.IItemExtension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class NeoForgeItemMixin implements IItemExtension {

	@Shadow(aliases = "isRepairable")
	public abstract boolean viscera$originalIsRepairable(ItemStack stack);

	@Unique
	private <T> T viscera$as(Class<T> clazz) {
		return clazz.isInstance(this) ? clazz.cast(this) : null;
	}

	@Override
	public boolean onDroppedByPlayer(@NotNull ItemStack stack, @NotNull Player player) {
		if ((Object) this instanceof DropHook hook)
			return hook.viscera$canPlayerDrop(stack, player);
		return IItemExtension.super.onDroppedByPlayer(stack, player);
	}

	@Override
	public @NotNull Component getHighlightTip(ItemStack stack, @NotNull Component displayName) {
		Component component = VisceralClientExtensionsManager.get(stack.getItem())
				.viscera$getHighlightedName(stack, displayName);
		return component != null ? component :
				IItemExtension.super.getHighlightTip(stack, displayName);
	}

	@Override
	public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, @NotNull UseOnContext context) {
		UseHook hook = viscera$as(UseHook.class);
		if (hook != null) {
			InteractionResult result = hook.viscera$onPreBlockInteraction(stack, context);
			if (result != InteractionResult.PASS) return result;
		}
		return IItemExtension.super.onItemUseFirst(stack, context);
	}

	@Override
	public void onStopUsing(@NotNull ItemStack stack, @NotNull LivingEntity entity, int count) {
		UseHook hook = viscera$as(UseHook.class);
		if (hook != null) hook.viscera$onPostBlockInteraction(stack, entity, count);
		IItemExtension.super.onStopUsing(stack, entity, count);
	}

	@Override
	public boolean isPiglinCurrency(@NotNull ItemStack stack) {
		PiglinHook hook = viscera$as(PiglinHook.class);
		return hook != null ? hook.viscera$isBarterCurrency(stack) :
				IItemExtension.super.isPiglinCurrency(stack);
	}

	@Override
	public boolean makesPiglinsNeutral(@NotNull ItemStack stack, @NotNull LivingEntity wearer) {
		PiglinHook hook = viscera$as(PiglinHook.class);
		return hook != null ? hook.viscera$doesPacifyPiglin(stack, wearer) :
				IItemExtension.super.makesPiglinsNeutral(stack, wearer);
	}

	@Override
	public boolean isRepairable(@NotNull ItemStack stack) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		return hook != null ? hook.viscera$canBeRepaired(stack) :
				this.isRepairable(stack);
	}

	@Override
	public float getXpRepairRatio(@NotNull ItemStack stack) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		return hook != null ? hook.viscera$getMendingEfficiency(stack) :
				IItemExtension.super.getXpRepairRatio(stack);
	}

	@Override
	public boolean onLeftClickEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity entity) {
		EntityHook hook = viscera$as(EntityHook.class);
		return (hook != null && hook.viscera$onEntityAttack(stack, player, entity)) ||
				IItemExtension.super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public @NotNull ItemStack getCraftingRemainingItem(@NotNull ItemStack stack) {
		CraftingRemainderHook hook = viscera$as(CraftingRemainderHook.class);
		return hook != null ? hook.viscera$getCraftingRemaining(stack) :
				IItemExtension.super.getCraftingRemainingItem(stack);
	}

	@Override
	public int getEntityLifespan(@NotNull ItemStack stack, @NotNull Level level) {
		EntityHook hook = viscera$as(EntityHook.class);
		return hook != null ? hook.viscera$getEntityDespawn(stack, level) :
				IItemExtension.super.getEntityLifespan(stack, level);
	}

	@Override
	public boolean hasCustomEntity(@NotNull ItemStack stack) {
		EntityHook hook = viscera$as(EntityHook.class);
		return hook != null ? hook.viscera$customItemEntity(stack) :
				IItemExtension.super.hasCustomEntity(stack);
	}

	@Override
	public @Nullable Entity createEntity(@NotNull Level level, @NotNull Entity location, @NotNull ItemStack stack) {
		EntityHook hook = viscera$as(EntityHook.class);
		if (hook != null) {
			Entity custom = hook.viscera$createEntity(level, location, stack);
			if (custom != null) return custom;
		}
		return IItemExtension.super.createEntity(level, location, stack);
	}

	@Override
	public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
		EntityHook hook = viscera$as(EntityHook.class);
		return (hook != null && hook.viscera$onEntityItemUpdate(stack, entity)) ||
				IItemExtension.super.onEntityItemUpdate(stack, entity);
	}

	@Override
	public boolean doesSneakBypassUse(@NotNull ItemStack stack, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
		UseHook hook = viscera$as(UseHook.class);
		return hook != null ? hook.viscera$shouldBypassSneak(stack, level, pos, player) :
				IItemExtension.super.doesSneakBypassUse(stack, level, pos, player);
	}

	@Override
	public boolean canEquip(@NotNull ItemStack stack, @NotNull EquipmentSlot armorType, @NotNull LivingEntity entity) {
		EquipmentHook hook = viscera$as(EquipmentHook.class);
		return hook != null ? hook.viscera$canFitSlot(stack, armorType, entity) :
				IItemExtension.super.canEquip(stack, armorType, entity);
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(@NotNull ItemStack stack) {
		EquipmentHook hook = viscera$as(EquipmentHook.class);
		if (hook != null) {
			EquipmentSlot custom = hook.viscera$getDesiredSlot(stack);
			if (custom != null) return custom;
		}
		return IItemExtension.super.getEquipmentSlot(stack);
	}

	@Override
	public boolean isBookEnchantable(@NotNull ItemStack stack, @NotNull ItemStack book) {
		EnchantmentHook hook = viscera$as(EnchantmentHook.class);
		return hook != null ? hook.viscera$isEnchantableWithBook(stack, book) :
				IItemExtension.super.isBookEnchantable(stack, book);
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, @NotNull Entity entity, @NotNull EquipmentSlot slot, ArmorMaterial.@NotNull Layer layer, boolean innerModel) {
		ResourceLocation texture = VisceralClientExtensionsManager.get(stack.getItem())
				.viscera$getArmorTexture(stack, entity, slot, layer, innerModel);
		return texture != null ? texture :
				IItemExtension.super.getArmorTexture(stack, entity, slot, layer, innerModel);
	}

	@Override
	public boolean onEntitySwing(@NotNull ItemStack stack, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
		EntityHook hook = viscera$as(EntityHook.class);
		return (hook != null && hook.viscera$shouldCancelSwing(stack, entity, hand)) ||
				IItemExtension.super.onEntitySwing(stack, entity, hand);
	}

	@Override
	public int getDamage(@NotNull ItemStack stack) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		return hook != null ? hook.viscera$getDamageValue(stack) :
				IItemExtension.super.getDamage(stack);
	}

	@Override
	public int getMaxDamage(@NotNull ItemStack stack) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		return hook != null ? hook.viscera$getMaxDurability(stack) :
				IItemExtension.super.getMaxDamage(stack);
	}

	@Override
	public void setDamage(@NotNull ItemStack stack, int damage) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		if (hook != null) {
			hook.viscera$setDamageValue(stack, damage);
		} else {
			IItemExtension.super.setDamage(stack, damage);
		}
	}

	@Override
	public boolean isDamaged(@NotNull ItemStack stack) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		return hook != null ? hook.viscera$hasWear(stack) :
				IItemExtension.super.isDamaged(stack);
	}

	@Override
	public int getMaxStackSize(@NotNull ItemStack stack) {
		StackHook hook = viscera$as(StackHook.class);
		return hook != null ? hook.viscera$getStackLimit(stack) :
				IItemExtension.super.getMaxStackSize(stack);
	}

	@Override
	public int getEnchantmentValue(@NotNull ItemStack stack) {
		EnchantmentHook hook = viscera$as(EnchantmentHook.class);
		return hook != null ? hook.viscera$getEnchantmentValue(stack) :
				IItemExtension.super.getEnchantmentValue(stack);
	}

	@Override
	public boolean isPrimaryItemFor(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		EnchantmentHook hook = viscera$as(EnchantmentHook.class);
		return hook != null ? hook.viscera$isCompatibleWithEnchantment(stack, enchantment, true) :
				IItemExtension.super.isPrimaryItemFor(stack, enchantment);
	}

	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		EnchantmentHook hook = viscera$as(EnchantmentHook.class);
		return hook != null ? hook.viscera$isCompatibleWithEnchantment(stack, enchantment, false) :
				IItemExtension.super.supportsEnchantment(stack, enchantment);
	}

	@Override
	public int getEnchantmentLevel(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		EnchantmentHook hook = viscera$as(EnchantmentHook.class);
		return hook != null ? hook.viscera$getEnchantmentLevel(stack, enchantment) :
				IItemExtension.super.getEnchantmentLevel(stack, enchantment);
	}

	@Override
	public @NotNull ItemEnchantments getAllEnchantments(@NotNull ItemStack stack, HolderLookup.@NotNull RegistryLookup<Enchantment> lookup) {
		EnchantmentHook hook = viscera$as(EnchantmentHook.class);
		return hook != null ? hook.viscera$getAllEnchantments(stack) :
				IItemExtension.super.getAllEnchantments(stack, lookup);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
		return VisceralClientExtensionsManager.get(oldStack.getItem())
				.viscera$shouldAllowReequipAnimation(oldStack, newStack, slotChanged);
	}

	@Override
	public boolean shouldCauseBlockBreakReset(@NotNull ItemStack oldStack, @NotNull ItemStack newStack) {
		if (!IItemExtension.super.shouldCauseBlockBreakReset(oldStack, newStack))
			return false;
		return !VisceralClientExtensionsManager.get(oldStack.getItem())
				.viscera$shouldContinueBreaking(oldStack, newStack);
	}

	@Override
	public boolean canDisableShield(@NotNull ItemStack stack, @NotNull ItemStack shield, @NotNull LivingEntity entity, @NotNull LivingEntity attacker) {
		CombatHook hook = viscera$as(CombatHook.class);
		return hook != null ? hook.viscera$canDisableShield(stack, shield, entity, attacker) :
				IItemExtension.super.canDisableShield(stack, shield, entity, attacker);
	}

	@Override
	public <T extends LivingEntity> int damageItem(@NotNull ItemStack stack, int amount, @Nullable T entity, @NotNull Consumer<Item> onBroken) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		return hook != null ? hook.viscera$onCalculateDamage(stack, amount, entity) :
				IItemExtension.super.damageItem(stack, amount, entity, onBroken);
	}

	@Override
	public boolean isDamageable(@NotNull ItemStack stack) {
		DurabilityHook hook = viscera$as(DurabilityHook.class);
		return hook != null ? hook.viscera$canTakeDamage(stack) :
				IItemExtension.super.isDamageable(stack);
	}
}
