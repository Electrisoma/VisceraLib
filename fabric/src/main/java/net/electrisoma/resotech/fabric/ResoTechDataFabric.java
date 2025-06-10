package net.electrisoma.resotech.fabric;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.electrisoma.resotech.ResoTech;
import net.electrisoma.resotech.fabric.providers.*;
import net.electrisoma.resotech.registry.ResoTechAdvancements;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

//public class ResoTechDataFabric implements DataGeneratorEntrypoint {
//    @Override
//    public void onInitializeDataGenerator(FabricDataGenerator gen) {
//        Path rtResources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
//        ExistingFileHelper helper = new ExistingFileHelper(
//                Set.of(rtResources),
//                Set.of(ResoTech.MOD_ID),
//                false,
//                null,
//                null
//        );
//        FabricDataGenerator.Pack pack = gen.createPack();
//
//        pack.addProvider((Factory<BlockstateGen>) output -> new BlockstateGen(output, helper));
//        pack.addProvider((Factory<ItemModelGen>) output -> new ItemModelGen(output, helper));
//        pack.addProvider((Factory<LangGen>) LangGen::new);
//        pack.addProvider(LootTableGen::new);
//        TagGen.addGenerators(pack);
//
//        pack.addProvider(ResoTechAdvancements::new);
//    }
//}
