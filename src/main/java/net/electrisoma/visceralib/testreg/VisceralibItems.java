package net.electrisoma.visceralib.testreg;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.ItemEntry;
import net.electrisoma.visceralib.testreg.items.TestItem;
import net.minecraft.world.effect.MobEffectInstance;

public class VisceralibItems {
    public static void init() {
        VisceraLib.LOGGER.info("Registering VisceralibItems for " + VisceraLib.NAME);
    }

    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

//    public static final ItemEntry<TestItem> TEST_ITEM = REGISTRAR
//            .item("test_item", TestItem::new)
//            .properties(p -> p
//                    .stacksTo(1)
//                    .rarity(Rarity.COMMON)
//                    .food(new FoodProperties.Builder()
//                            .nutrition(4)
//                            .saturationModifier(0.1f)
//                            .alwaysEdible()
//                            .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0),0.8f)
//                            .effect(new MobEffectInstance(MobEffects.POISON, 300, 2),0.8f)
//                            .build())
//            )
//            .register();
}
