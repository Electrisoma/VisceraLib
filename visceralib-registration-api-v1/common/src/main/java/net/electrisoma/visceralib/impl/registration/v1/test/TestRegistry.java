package net.electrisoma.visceralib.impl.registration.v1.test;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.VisceralRegistrationHelper;
import net.electrisoma.visceralib.impl.registration.v1.Constants;
import net.electrisoma.visceralib.impl.registration.v1.test.helper.TestRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;

public final class TestRegistry {

    public static final VisceralRegistrationHelper NORMAL_REGISTRY = Constants.registry();
    public static final TestRegistrationHelper BUILDER_REGISTRY = Constants.testRegistry();

    static {
        NORMAL_REGISTRY.withTab(TestCreativeTabs.NORMAL_TAB);
    }

    public static RegistryObject<TestPickItem> FABRIC_ITEM = NORMAL_REGISTRY.register(
            BuiltInRegistries.ITEM,
            "fabric_test_item",
            () -> new TestPickItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1))
    );

    public static RegistryObject<TestPickItem> NORMAL_ITEM = NORMAL_REGISTRY.item(
            "normal_test_item",
            () -> new TestPickItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1))
    );

    public static RegistryObject<TestPickItem> BACKPORTED_ITEM = NORMAL_REGISTRY.item(
            "backported_test_item",
            TestPickItem::new,
            () -> new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)
    );

    public static RegistryObject<TestPickItem> BUILDER_ITEM = BUILDER_REGISTRY
            .item("builder_test_item", TestPickItem::new)
            .properties(p -> p.rarity(Rarity.EPIC))
            .properties(p -> p.stacksTo(1))
            .tab(TestCreativeTabs.BUILDER_TAB.key())
            .register();

    public static RegistryObject<TestSwordItem> SWORD_ITEM = BUILDER_REGISTRY
            .item("test_sword_item", TestSwordItem::new)
            .properties(p -> p.rarity(Rarity.EPIC))
            .properties(p -> p.stacksTo(1))
            .properties(p -> p.attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F)))
            .tab(TestCreativeTabs.BUILDER_TAB.key())
            .register();

    public static RegistryObject<Item> TEST_ARMOR = NORMAL_REGISTRY.item(
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
//    public static final RegistryObject<FlowingFluid> OIL_STILL = NORMAL_REGISTRY.fluid(
//            "oil",
//            OIL_PROPS,
//            () -> new OilFluid.Source(OIL_PROPS, NORMAL_REGISTRY),
//            () -> new OilFluid.Flowing(OIL_PROPS, NORMAL_REGISTRY),
//            (fluid) -> new Item.Properties()
//    );
//
//    public static final RegistryObject<FlowingFluid> OIL_FLOWING = NORMAL_REGISTRY.fluid(
//            "flowing_oil",
//            () -> new OilFluid.Flowing(OIL_PROPS, NORMAL_REGISTRY)
//    );
//
//    public static final RegistryObject<Block> OIL_BLOCK = NORMAL_REGISTRY.block(
//            "oil",
//            () -> new LiquidBlock(OIL_STILL.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER))
//    );

    public static void init() {}
}