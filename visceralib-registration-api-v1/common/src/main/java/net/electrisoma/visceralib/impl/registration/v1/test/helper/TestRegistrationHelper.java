package net.electrisoma.visceralib.impl.registration.v1.test.helper;

import net.electrisoma.visceralib.api.registration.v1.registry.register.AbstractRegistrationHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.VisceralRegistry;
import net.electrisoma.visceralib.impl.registration.v1.test.helper.builders.ItemBuilder;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public final class TestRegistrationHelper extends AbstractRegistrationHelper<TestRegistrationHelper> {

    private TestRegistrationHelper(String modId) {
        super(new VisceralRegistry(modId));
    }

    public static TestRegistrationHelper of(String modId) {
        return new TestRegistrationHelper(modId);
    }

    public <T extends Item> ItemBuilder<T, TestRegistrationHelper> item(
            String name,
            Function<Item.Properties, T> factory
    ) {
        return new ItemBuilder<>(this, name, factory);
    }
}