package net.electrisoma.resotech.api.registration.builders;

import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.advancement.RTrigger;
import net.electrisoma.resotech.api.registration.advancement.RTriggers;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Objects;
import java.util.function.*;

@SuppressWarnings("unused")
public class AdvancementBuilder {
    private static final ResourceLocation BACKGROUND = ResoTech.path("textures/gui/advancements.png");
    private static final String LANG_PREFIX = "advancement." + ResoTech.MOD_ID + ".";
    private static final String SECRET_SUFFIX = "\n§7(Hidden Advancement)";

    private final Advancement.Builder mcBuilder = Advancement.Builder.advancement();
    private final Builder builder = new Builder();

    private RTrigger builtinTrigger;
    private AdvancementHolder datagenResult;
    private AdvancementBuilder parent;

    private final String id;
    private String title;
    private String description;

    public AdvancementBuilder(String id, UnaryOperator<Builder> builderOp) {
        this.id = id;
        builderOp.apply(builder);

        if (!builder.externalTrigger) {
            builtinTrigger = RTriggers.addTrigger(id + "_builtin");
            mcBuilder.addCriterion("0", builtinTrigger.createCriterion(builtinTrigger.instance()));
        }

        if (builder.type == TaskType.SECRET) {
            this.description += SECRET_SUFFIX;
        }
    }

    public boolean isAlreadyAwardedTo(Player player) {
        if (!(player instanceof ServerPlayer sp)) return true;

        AdvancementHolder adv = Objects.requireNonNull(sp.getServer())
                .getAdvancements()
                .get(ResoTech.path(id));

        return adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone();
    }

    public void awardTo(Player player) {
        if (!(player instanceof ServerPlayer sp)) return;

        if (builtinTrigger == null) {
            throw new UnsupportedOperationException("Advancement " + id + " uses external Triggers, cannot be awarded directly");
        }

        builtinTrigger.trigger(sp);
    }

    public void save(Consumer<AdvancementHolder> consumer, HolderLookup.Provider registries) {
        if (parent != null) mcBuilder.parent(parent.datagenResult);
        if (builder.func != null) builder.icon(builder.func.apply(registries));

        mcBuilder.display(
                builder.icon,
                Component.translatable(titleKey()),
                Component.translatable(descriptionKey()).withStyle(style -> style.withColor(0xDBA213)),
                id.equals("root") ? BACKGROUND : null,
                builder.type.advancementType,
                builder.type.toast,
                builder.type.announce,
                builder.type.hide
        );

        datagenResult = mcBuilder.save(consumer, ResoTech.path(id).toString());
    }

    public void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept(titleKey(), title);
        consumer.accept(descriptionKey(), description);
    }

    private String titleKey() {
        return LANG_PREFIX + id;
    }

    private String descriptionKey() {
        return titleKey() + ".desc";
    }

    public enum TaskType {
        SILENT(AdvancementType.TASK, false, false, false),
        NORMAL(AdvancementType.TASK, true, false, false),
        NOISY(AdvancementType.TASK, true, true, false),
        EXPERT(AdvancementType.GOAL, true, true, false),
        SECRET(AdvancementType.GOAL, true, true, true);

        public final AdvancementType advancementType;
        public final boolean toast;
        public final boolean announce;
        public final boolean hide;

        TaskType(AdvancementType advancementType, boolean toast, boolean announce, boolean hide) {
            this.advancementType = advancementType;
            this.toast = toast;
            this.announce = announce;
            this.hide = hide;
        }
    }

    public class Builder {
        private TaskType type = TaskType.NORMAL;
        private boolean externalTrigger = false;
        private int keyIndex = 0;
        private ItemStack icon;
        private Function<HolderLookup.Provider, ItemStack> func;

        public Builder type(TaskType type) {
            this.type = type;
            return this;
        }

        public Builder after(AdvancementBuilder parent) {
            AdvancementBuilder.this.parent = parent;
            return this;
        }

        public Builder icon(ItemLike item) {
            return icon(new ItemStack(item));
        }

        public Builder icon(ItemStack stack) {
            this.icon = stack;
            return this;
        }

        public Builder icon(Function<HolderLookup.Provider, ItemStack> func) {
            this.func = func;
            return this;
        }

        public Builder title(String title) {
            AdvancementBuilder.this.title = title;
            return this;
        }

        public Builder description(String description) {
            AdvancementBuilder.this.description = description;
            return this;
        }

        public Builder whenBlockPlaced(Block block) {
            return externalTrigger(ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block));
        }

        public Builder whenIconCollected() {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(icon.getItem()));
        }

        public Builder whenItemCollected(ItemLike item) {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(item));
        }

        public Builder whenItemCollected(TagKey<Item> tag) {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(
                    ItemPredicate.Builder.item().of(tag).build()
            ));
        }

        public Builder awardedForFree() {
            return externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{}));
        }

        public Builder externalTrigger(Criterion<?> criterion) {
            mcBuilder.addCriterion(String.valueOf(keyIndex++), criterion);
            externalTrigger = true;
            return this;
        }
    }
}
