package net.electrisoma.visceralib.impl.registration.v1;

import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistrationHelper;
import net.electrisoma.visceralib.impl.registration.v1.test.TestPickItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class EntrypointFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Entrypoint.init();
    }

    public static final VisceralRegistrationHelper REGISTRY = Constants.registry();

    public static final RegistryObject<Item> TEST_ITEM = REGISTRY.item(
            "test_item",
            () -> new TestPickItem(new Item.Properties().rarity(Rarity.EPIC))
    );
}
