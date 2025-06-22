package net.electrisoma.visceralib.api.registration.builders;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TabBuilder {
    private final String name;
    private Component title;
    private Supplier<ItemStack> iconSupplier;
    private final AbstractVisceralRegistrar<?> registrar;

    private static final List<TabBuilder> ALL_BUILDERS = new ArrayList<>();

    private final List<Supplier<ItemStack>> tabContents = new ArrayList<>();
    private TabEntry<CreativeModeTab> tabEntry;

    public static List<TabBuilder> getAllBuilders() {
        return List.copyOf(ALL_BUILDERS);
    }

    public TabBuilder(AbstractVisceralRegistrar<?> registrar, String name) {
        this.registrar = registrar;
        this.name = name;
        ALL_BUILDERS.add(this);
    }

    public TabBuilder icon(Item item) {
        this.iconSupplier = () -> new ItemStack(item);
        return this;
    }

    public TabBuilder lang(String langString) {
        this.title = Component.literal(langString);
        return this;
    }

    public TabBuilder addItem(ItemLike item) {
        return addItem(() -> new ItemStack(item));
    }

    public TabBuilder addItem(Supplier<ItemStack> stackSupplier) {
        tabContents.add(stackSupplier);
        return this;
    }

    public List<Supplier<ItemStack>> getTabContents() {
        return List.copyOf(tabContents);
    }

    public String getName() {
        return name;
    }

    public TabEntry<CreativeModeTab> getTabEntry() {
        return tabEntry;
    }

    public TabEntry<CreativeModeTab> register() {
        Supplier<CreativeModeTab> tabSupplier = () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                .title(title != null ? title : Component.literal(name))
                .icon(iconSupplier != null ? iconSupplier : () -> ItemStack.EMPTY)
                .build();

        var deferredRegister = registrar.deferredRegister(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB);

        var registrySupplier = deferredRegister.register(name, tabSupplier);

        this.tabEntry = new TabEntry<>(new VisceralRegistrySupplier<>(registrySupplier.getKey(), registrySupplier::get));
        return tabEntry;
    }
}
