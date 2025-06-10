package net.electrisoma.resotech.registry;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.TabBuilder;
import net.electrisoma.resotech.api.registration.ItemBuilder;
import net.electrisoma.resotech.api.registration.BlockBuilder;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.*;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ResoTechTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(ResoTech.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static void init() {
        CREATIVE_TABS.register();
        ResoTech.LOGGER.info("Registering Tabs for " + ResoTech.NAME);
    }

    public static final RegistrySupplier<CreativeModeTab> BASE =
            new TabBuilder(ResoTech.MOD_ID, "base")
                    .icon(() -> new ItemStack(ResoTechItems.TEST_ITEM.get()))
                    .displayName("ResoTech")
                    .register();

    public static final RegistrySupplier<CreativeModeTab> MACHINES =
            new TabBuilder(ResoTech.MOD_ID, "machines")
                    .icon(() -> new ItemStack(Items.SCULK))
                    .displayName("Machines")
                    .register();

    /**
     * Legacy registration method using ResoTechDisplayItemsGenerator.
     * Optional to keep or remove.
     */
    @SuppressWarnings("UnstableApiUsage")
    public static RegistrySupplier<CreativeModeTab> registerTab(
            String name,
            Supplier<? extends ItemLike> iconSupplier,
            Supplier<Collection<? extends ItemLike>> explicitContentSupplier,
            Predicate<Item> itemFilter) {

        return CREATIVE_TABS.register(name, () -> CreativeTabRegistry.create(builder -> builder
                .title(Component.translatable("itemGroup." + ResoTech.MOD_ID + "." + name))
                .icon(() -> new ItemStack(iconSupplier.get()))
                .displayItems(new ResoTechDisplayItemsGenerator(name, explicitContentSupplier, itemFilter))
                .build()));
    }

    public static ResourceKey<CreativeModeTab> getKey(RegistrySupplier<CreativeModeTab> tabSupplier) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, tabSupplier.getId());
    }

    /**
     * Internal display item generator now takes a Supplier for explicitInclusions.
     */
    public record ResoTechDisplayItemsGenerator(
            String tabName,
            Supplier<Collection<? extends ItemLike>> explicitInclusionsSupplier,
            Predicate<Item> itemFilter) implements DisplayItemsGenerator {
        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, Output output) {
            ResourceLocation tabId = ResoTech.path(tabName);
            List<Item> items = new ArrayList<>();

            for (var builder : ItemBuilder.getAllBuilders()) {
                if (builder.getTabs().contains(ResourceKey.create(Registries.CREATIVE_MODE_TAB, tabId))) {
                    items.add(BuiltInRegistries.ITEM.get(ResoTech.path(builder.getName())));
                }
            }

            for (var builder : BlockBuilder.getAllBuilders()) {
                if (builder.getTabs().contains(ResourceKey.create(Registries.CREATIVE_MODE_TAB, tabId))) {
                    items.add(BuiltInRegistries.ITEM.get(ResoTech.path(builder.getName())));
                }
            }

            items.sort(Comparator.comparing(i -> BuiltInRegistries.ITEM.getKey(i).toString()));
            for (Item item : items) {
                output.accept(new ItemStack(item), TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }

        private Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = Set.of(
                    // Place excluded items here if needed
            );
            return o -> false;
        }

        private Function<Item, ItemStack> makeStackFunc() {
            return item -> {
                ItemStack stack = new ItemStack(item);
                if (stack.getCount() != 1) stack.setCount(1);
                return stack;
            };
        }

        private Function<Item, TabVisibility> makeVisibilityFunc() {
            return item -> TabVisibility.PARENT_AND_SEARCH_TABS;
        }
    }
}
