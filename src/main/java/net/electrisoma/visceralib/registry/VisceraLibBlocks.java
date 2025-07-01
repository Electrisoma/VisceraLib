//package net.electrisoma.visceralib.registry;
//
//import net.electrisoma.visceralib.VisceraLib;
//import net.electrisoma.visceralib.api.registration.VisceralRegistrar;
//import net.electrisoma.visceralib.api.registration.entry.BlockEntry;
//
//import net.electrisoma.visceralib.api.registration.helpers.SharedProperties;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.CreativeModeTabs;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.material.MapColor;
//
//import java.util.function.Supplier;
//
//import static net.electrisoma.visceralib.api.registration.helpers.BuilderTransforms.pickaxeOnly;
//
//@SuppressWarnings("unused")
//public class VisceraLibBlocks {
//    private static final VisceralRegistrar REGISTRAR = VisceraLib.registrar();
//
//    public static void init() {
//        VisceraLib.LOGGER.info("Registering Blocks for " + VisceraLib.NAME);
//    }
//
//    public static final BlockEntry<Block> MACHINE_BLOCK = REGISTRAR
//            .block("machine_block", Block::new)
//            .initialProperties(SharedProperties.netheriteMetal())
//            .properties(p -> p
//                    .strength(1.0F, 10000.0F)
//                    .mapColor(MapColor.COLOR_GRAY)
//            )
//            .onRegister(block -> VisceraLib.LOGGER.info("Machine block is: {}", block.getName()))
//            .transform(pickaxeOnly())
//            .lang("Machine Thing")
//            .register();
//}
