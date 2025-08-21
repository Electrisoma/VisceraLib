package net.electrisoma.visceralib.api.registration.builders;

import net.electrisoma.visceralib.api.registration.VisceralRegistry;
import net.electrisoma.visceralib.api.registration.holders.CreativeTabHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class CreativeTabBuilder extends AbstractBuilder<CreativeModeTab, CreativeModeTab, Holder.Reference<CreativeModeTab>> {
    private CreativeModeTab.Row row;
    private int column;
    private Component title = Component.empty();
    private Supplier<ItemStack> iconSupplier = () -> ItemStack.EMPTY;
    private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = (params, output) -> {};
    private boolean showTitle = true;
    private boolean canScroll = true;

    public CreativeTabBuilder(VisceralRegistry owner, String name, CreativeModeTab.Row row, int column) {
        super(owner, name, BuiltInRegistries.CREATIVE_MODE_TAB);
        this.row = row;
        this.column = column;
    }

    public CreativeTabBuilder title(Component title) {
        this.title = title;
        return this;
    }

    public CreativeTabBuilder icon(Supplier<ItemStack> iconSupplier) {
        this.iconSupplier = iconSupplier;
        return this;
    }

    public CreativeTabBuilder displayItems(CreativeModeTab.DisplayItemsGenerator generator) {
        this.displayItemsGenerator = generator;
        return this;
    }

    public CreativeTabBuilder showTitle(boolean showTitle) {
        this.showTitle = showTitle;
        return this;
    }

    public CreativeTabBuilder canScroll(boolean canScroll) {
        this.canScroll = canScroll;
        return this;
    }

    @Override
    public CreativeModeTab build() {
        CreativeModeTab.Builder builder = CreativeModeTab.builder(row, column)
                .title(title)
                .icon(iconSupplier)
                .displayItems(displayItemsGenerator);

        if (!showTitle) {
            builder.hideTitle();
        }
        if (!canScroll) {
            builder.noScrollBar();
        }

        return builder.build();
    }

    @Override
    public CreativeTabHolder getHolder(HolderOwner<CreativeModeTab> owner, ResourceKey<CreativeModeTab> key) {
        return new CreativeTabHolder(owner, key);
    }
}
