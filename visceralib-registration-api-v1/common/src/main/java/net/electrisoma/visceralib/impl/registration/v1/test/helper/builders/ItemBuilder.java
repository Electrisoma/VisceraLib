package net.electrisoma.visceralib.impl.registration.v1.test.helper.builders;

import net.electrisoma.visceralib.api.registration.v1.registry.register.AbstractRegistrationHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ItemBuilder<T extends Item, H extends AbstractRegistrationHelper<?>> {

    private final H helper;
    private final String name;
    private final Function<Item.Properties, T> factory;
    private Supplier<Item.Properties> properties = Item.Properties::new;

    public ItemBuilder(H helper, String name, Function<Item.Properties, T> factory) {
        this.helper = helper;
        this.name = name;
        this.factory = factory;
    }

    public ItemBuilder<T, H> properties(UnaryOperator<Item.Properties> op) {
        Supplier<Item.Properties> supp = this.properties;
        this.properties = () -> op.apply(supp.get());
        return this;
    }

    public RegistryObject<T> register() {
        return helper.register(BuiltInRegistries.ITEM, name, () ->
                factory.apply(properties.get())
        );
    }
}