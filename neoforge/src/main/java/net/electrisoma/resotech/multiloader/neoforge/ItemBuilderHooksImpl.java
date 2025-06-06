//package net.electrisoma.resotech.multiloader.neoforge;
//
//import net.electrisoma.resotech.multiloader.ItemBuilderHooks;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.Item;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
//
//import java.util.*;
//
//@EventBusSubscriber
//public class ItemBuilderHooksImpl {
//    private static final Map<ResourceKey<CreativeModeTab>, List<Item>> ITEMS_TO_ADD = new HashMap<>();
//
//    public static void addToCreativeTabs(Item item, Set<ResourceKey<CreativeModeTab>> tabs) {
//        for (ResourceKey<CreativeModeTab> tab : tabs) {
//            ITEMS_TO_ADD.computeIfAbsent(tab, t -> new ArrayList<>()).add(item);
//        }
//    }
//
//    @SubscribeEvent
//    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
//        List<Item> items = ITEMS_TO_ADD.get(event.getTabKey());
//        if (items != null) {
//            for (Item item : items) {
//                event.accept(item);
//            }
//        }
//    }
//}
