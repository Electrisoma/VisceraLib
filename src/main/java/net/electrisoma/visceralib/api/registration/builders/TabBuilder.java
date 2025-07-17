package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.data.providers.VisceralLangProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import net.electrisoma.visceralib.api.registration.AbstractVisceralRegistrar;
import net.electrisoma.visceralib.api.registration.VisceralDeferredRegister;
import net.electrisoma.visceralib.api.registration.VisceralRegistrySupplier;
import net.electrisoma.visceralib.api.registration.entry.TabEntry;

import java.util.*;
import java.util.function.Supplier;

public class TabBuilder {
    private static final List<TabBuilder> ALL_BUILDERS = new ArrayList<>();
    public static List<TabBuilder> getAllBuilders() {
        return Collections.unmodifiableList(ALL_BUILDERS);
    }

    private final AbstractVisceralRegistrar<?> registrar;
    private final String name;
    private String langEntry;

    private Component title;
    private Supplier<ItemStack> iconSupplier;
    private final List<Supplier<ItemStack>> contents = new ArrayList<>();
    private TabEntry<CreativeModeTab> registeredTab;

    public TabBuilder(AbstractVisceralRegistrar<?> registrar, String name) {
        this.registrar = Objects.requireNonNull(registrar);
        this.name = Objects.requireNonNull(name);
        ALL_BUILDERS.add(this);
    }

    public TabBuilder lang(String langString) {
        this.title = Component.literal(langString);
        this.langEntry = langString;
        return this;
    }

    public TabBuilder icon(Item iconItem) {
        return icon(() -> new ItemStack(iconItem));
    }

    public TabBuilder icon(Supplier<ItemStack> iconSupplier) {
        this.iconSupplier = Objects.requireNonNull(iconSupplier);
        return this;
    }

    public TabBuilder addItem(ItemLike item) {
        return addItem(() -> new ItemStack(item));
    }

    public TabBuilder addItem(Supplier<ItemStack> supplier) {
        contents.add(Objects.requireNonNull(supplier));
        return this;
    }

    public TabEntry<CreativeModeTab> register() {
        VisceralDeferredRegister<CreativeModeTab> tabRegister =
                registrar.deferredRegister(Registries.CREATIVE_MODE_TAB);

        Supplier<CreativeModeTab> supplier = () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                .title(title != null ? title : Component.literal(name))
                .icon(iconSupplier != null ? iconSupplier : () -> ItemStack.EMPTY)
                .displayItems((params, output) -> {
                    for (Supplier<ItemStack> stackSupplier : contents) {
                        ItemStack stack = stackSupplier.get();
                        if (!stack.isEmpty()) {
                            output.accept(stack);
                        }
                    }
                })
                .build();

        VisceralRegistrySupplier<CreativeModeTab> registrySupplier = tabRegister.register(name, supplier);

        this.registeredTab = new TabEntry<>(new VisceralRegistrySupplier<>(
                registrySupplier.getKey(),
                registrySupplier::get
        ));

        return registeredTab;
    }

    public Optional<TabEntry<CreativeModeTab>> getTabEntry() {
        return Optional.ofNullable(registeredTab);
    }

    public String getName() {
        return name;
    }

    public AbstractVisceralRegistrar<?> getRegistrar() {
        return registrar;
    }

    public Optional<String> getLangEntry() {
        return Optional.ofNullable(langEntry);
    }

    public static void provideLang(VisceralLangProvider provider) {
        for (TabBuilder builder : getAllBuilders()) {
            var opt = builder.getTabEntry();
            if (opt.isEmpty()) continue;

            var tab = opt.get().get();
            if (tab == null || !builder.registrar.getModId().equals(provider.getModId())) continue;

            String langKey = "itemGroup." + builder.registrar.getModId() + "." + builder.name;
            String langValue = builder.langEntry != null
                    ? builder.langEntry
                    : VisceralLangProvider.toEnglishName(builder.name);

            provider.add(langKey, langValue);
        }
    }
}