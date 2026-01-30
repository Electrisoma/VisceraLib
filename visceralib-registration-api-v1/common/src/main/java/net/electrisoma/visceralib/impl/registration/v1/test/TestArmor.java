package net.electrisoma.visceralib.impl.registration.v1.test;

import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public final class TestArmor extends ArmorItem {

    public TestArmor(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public boolean viscera$doesPacifyPiglin(
            ItemStack stack,
            LivingEntity wearer
    ) {
        return true;
    }

    @Override
    public ResourceLocation viscera$getArmorTexture(
            ItemStack stack,
            Entity entity,
            EquipmentSlot slot,
            ArmorMaterial.Layer layer,
            boolean innerModel
    ) {
        return RLUtils.path("visceralib", "textures/model/armor/test_armor_layer_1.png");
    }
}