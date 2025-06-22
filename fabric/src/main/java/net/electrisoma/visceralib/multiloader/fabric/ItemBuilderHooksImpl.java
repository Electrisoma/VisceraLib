//package net.electrisoma.resotech.multiloader.fabric;
//
//import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
//import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
//import net.fabricmc.fabric.api.registry.FuelRegistry;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.Item;
//
//import java.util.Set;
//
//public class ItemBuilderHooksImpl {
//    public static void addFuel(Item item, int ticks) {
//        FuelRegistry.INSTANCE.add(item, ticks);
//    }
//
//    public static void addCompostable(Item item, float chance) {
//        CompostingChanceRegistry.INSTANCE.add(item, chance);
//    }
//
//    public static void addToCreativeTabs(Item item, Set<ResourceKey<CreativeModeTab>> tabs) {
//        for (ResourceKey<CreativeModeTab> tab : tabs) {
//            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
//                entries.accept(item);
//            });
//        }
//    }
//}
