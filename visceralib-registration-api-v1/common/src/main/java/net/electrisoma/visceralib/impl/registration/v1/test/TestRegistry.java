package net.electrisoma.visceralib.impl.registration.v1.test;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistrationHelper;
import net.electrisoma.visceralib.impl.registration.v1.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class TestRegistry {

    public static final VisceralRegistrationHelper REGISTRY = Constants.registry();

    static {
        REGISTRY.withTab(TestCreativeTabs.TEST_TAB);
    }

    public static RegistryObject<Item> TEST_ITEM = REGISTRY.item(
            "test_item",
            () -> new TestPickItem(new Item.Properties().rarity(Rarity.EPIC))
    );

    public static RegistryObject<Item> TEST_ITEM_1 = REGISTRY.item(
            "test_item",
            () -> new TestPickItem(new Item.Properties().rarity(Rarity.EPIC))
    );

    public static RegistryObject<Item> TEST_ARMOR = REGISTRY.item(
            "test_armor",
            () -> new TestArmor(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, new Item.Properties())
    );

//    public static final VisceralFluidAttributes OIL_ATTRIBS = new VisceralFluidAttributes.Builder()
//            .viscosity(3000)
//            .density(1200)
//            .canSwim(true)
//            .temperature(285)
//            .build();
//
//    public static final VisceralFluidProperties OIL_PROPS = new VisceralFluidProperties.Builder(
//            reg -> TestRegistry.OIL_STILL.get(),
//            reg -> TestRegistry.OIL_FLOWING.get())
//            .attributes(OIL_ATTRIBS)
//            .slope(2)
//            .tickDelay(20)
//            .dropOff(2)
//            .explosionResistance(100.0f)
//            .block(reg -> TestRegistry.OIL_BLOCK.get())
//            .bucket(reg -> BuiltInRegistries.ITEM.get(RLUtils.path("visceralib", "oil_bucket")))
//            .build();
//
//    public static final RegistryObject<FlowingFluid> OIL_STILL = REGISTRY.fluid(
//            "oil",
//            OIL_PROPS,
//            () -> new OilFluid.Source(OIL_PROPS, REGISTRY),
//            () -> new OilFluid.Flowing(OIL_PROPS, REGISTRY),
//            (fluid) -> new Item.Properties()
//    );
//
//    public static final RegistryObject<FlowingFluid> OIL_FLOWING = REGISTRY.fluid(
//            "flowing_oil",
//            () -> new OilFluid.Flowing(OIL_PROPS, REGISTRY)
//    );
//
//    public static final RegistryObject<Block> OIL_BLOCK = REGISTRY.block(
//            "oil",
//            () -> new LiquidBlock(OIL_STILL.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER))
//    );

    public static void init() {}
}