package net.electrisoma.visceralib.impl.registration.v1.test.helper.builders;

import net.electrisoma.visceralib.api.registration.v1.registry.AbstractRegistrationHelper;
import net.electrisoma.visceralib.api.registration.v1.registry.register.RegistryObject;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TabBuilder<H extends AbstractRegistrationHelper<?>> {

    private final H helper;
    private final String name;
    private Consumer<CreativeModeTab.Builder> config = builder -> {};

    public TabBuilder(H helper, String name) {
        this.helper = helper;
        this.name = name;
    }

    public TabBuilder<H> configure(Consumer<CreativeModeTab.Builder> consumer) {
        this.config = this.config.andThen(consumer);
        return this;
    }

    public TabBuilder<H> icon(Supplier<ItemStack> iconSupplier) {
        return configure(builder -> builder.icon(iconSupplier));
    }

    public TabBuilder<H> title(Component title) {
        return configure(builder -> builder.title(title));
    }

    public TabBuilder<H> displayItems(CreativeModeTab.DisplayItemsGenerator generator) {
        return configure(builder -> builder.displayItems(generator));
    }

    public RegistryObject<CreativeModeTab> register() {
        return helper.tab(name, () -> ITabHelper.INSTANCE.create(config));
    }
}
