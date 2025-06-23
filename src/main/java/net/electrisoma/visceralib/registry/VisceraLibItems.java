package net.electrisoma.visceralib.registry;

import net.electrisoma.visceralib.VisceraLib;
import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.ItemEntry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.Optional;

public class VisceraLibItems {
    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();

    public static void init() {
        VisceraLib.LOGGER.info("Registering Items for " + VisceraLib.NAME);
    }

    public static final ItemEntry<Item> TEST_ITEM = REGISTRAR
            .item("test_item", Item::new)
            .properties(p -> p
                    .stacksTo(1)
                    .rarity(Rarity.COMMON)
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(0.1f)
                            .alwaysEdible()
                            .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0),0.8f)
                            .effect(new MobEffectInstance(MobEffects.POISON, 300, 2),0.8f)
                            .build())
            )
            .lang("Test Item")
            .tab(VisceraLibTabs.MACHINES::getKey)
            .register();
}
