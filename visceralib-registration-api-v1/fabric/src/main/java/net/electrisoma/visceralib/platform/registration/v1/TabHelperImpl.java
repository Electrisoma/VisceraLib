package net.electrisoma.visceralib.platform.registration.v1;

import com.google.auto.service.AutoService;
import net.electrisoma.visceralib.platform.registration.v1.services.ITabHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AutoService(ITabHelper.class)
public class TabHelperImpl implements ITabHelper {

    @Override
    public CreativeModeTab create(Consumer<CreativeModeTab.Builder> builderConfig) {
        CreativeModeTab.Builder builder = FabricItemGroup.builder();
        builderConfig.accept(new DelegatingBuilder(builder));
        return builder.build();
    }

    private static class DelegatingBuilder extends CreativeModeTab.Builder {

        private final CreativeModeTab.Builder parent;

        public DelegatingBuilder(CreativeModeTab.Builder parent) {
            super(CreativeModeTab.Row.TOP, 0);
            this.parent = parent;
        }

        @Override
        public CreativeModeTab.@NotNull Builder title(@NotNull Component title) {
            parent.title(title);
            return this;
        }

        @Override
        public CreativeModeTab.@NotNull Builder icon(@NotNull Supplier<ItemStack> icon) {
            parent.icon(icon);
            return this;
        }

        @Override
        public CreativeModeTab.@NotNull Builder displayItems(CreativeModeTab.@NotNull DisplayItemsGenerator generator) {
            parent.displayItems((params, output) -> {
                Set<Item> seen = Collections.newSetFromMap(new ConcurrentHashMap<>());
                CreativeModeTab.Output interceptedOutput = (stack, visibility) -> {
                    if (seen.add(stack.getItem()))
                        output.accept(stack, visibility);
                };
                generator.accept(params, interceptedOutput);
            });

            return this;
        }
    }
}