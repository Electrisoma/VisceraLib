package net.electrisoma.resotech.registry;

import com.google.common.collect.Sets;
import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.api.registration.advancement.AdvancementBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static net.electrisoma.resotech.api.registration.advancement.AdvancementBuilder.TaskType.*;

@SuppressWarnings("unused")
public class ResoTechAdvancements implements DataProvider {

    public static final List<AdvancementBuilder> ENTRIES = new ArrayList<>();

    public static final AdvancementBuilder ROOT =
            register("root", b -> b
            .title("ResoTech")
            .description("Here Be Resonance")
            .icon(ResoTechItems.TEST_ITEM.get())
            .awardedForFree()
            .type(SILENT)
    );

    @SuppressWarnings("SameParameterValue")
    private static AdvancementBuilder register(String id, UnaryOperator<AdvancementBuilder.Builder> builder) {
        return new AdvancementBuilder(id, builder);
    }

    // DataGen infrastructure
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public ResoTechAdvancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this.output = output;
        this.registries = registries;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return registries.thenCompose(provider -> {
            PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancement");
            Set<ResourceLocation> seen = Sets.newHashSet();
            List<CompletableFuture<?>> futures = new ArrayList<>();

            Consumer<AdvancementHolder> consumer = advancement -> {
                ResourceLocation id = advancement.id();
                if (!seen.add(id))
                    throw new IllegalStateException("Duplicate advancement: " + id);
                Path path = pathProvider.json(id);
                ResoTech.LOGGER.debug("Saving advancement: {}", id);
                futures.add(DataProvider.saveStable(cache, provider, Advancement.CODEC, advancement.value(), path));
            };

            for (AdvancementBuilder advancement : ENTRIES) {
                advancement.save(consumer, provider);
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return ResoTech.NAME + " Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (AdvancementBuilder advancement : ENTRIES)
            advancement.provideLang(consumer);
    }
}
