//package net.electrisoma.visceralib.data.providers;
//
//import com.google.common.collect.Sets;
//import net.electrisoma.visceralib.VisceraLib;
//import net.minecraft.advancements.Advancement;
//import net.minecraft.advancements.AdvancementHolder;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.data.CachedOutput;
//import net.minecraft.data.DataProvider;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.PackOutput.PathProvider;
//import net.minecraft.resources.ResourceLocation;
//
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.Consumer;
//
//public class ResoAdvancementProvider implements DataProvider {
//    private final PackOutput output;
//    private final CompletableFuture<HolderLookup.Provider> registries;
//
//    public ResoAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
//        this.output = output;
//        this.registries = registries;
//    }
//
//    @Override
//    public CompletableFuture<?> run(CachedOutput cache) {
//        return registries.thenCompose(provider -> {
//            PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
//            Set<ResourceLocation> seen = Sets.newHashSet();
//            List<CompletableFuture<?>> futures = new ArrayList<>();
//
//            Consumer<AdvancementHolder> consumer = advancement -> {
//                ResourceLocation id = advancement.id();
//                if (!seen.add(id)) {
//                    throw new IllegalStateException("Duplicate advancement: " + id);
//                }
//                Path path = pathProvider.json(id);
//                VisceraLib.LOGGER.debug("Saving advancement: {}", id);
//                futures.add(DataProvider.saveStable(cache, provider, Advancement.CODEC, advancement.value(), path));
//            };
//
//            for (AdvancementBuilder advancement : ResoTechAdvancements.ENTRIES) {
//                advancement.save(consumer, provider);
//            }
//
//            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
//        });
//    }
//
//    @Override
//    public String getName() {
//        return VisceraLib.NAME + " Advancements";
//    }
//}