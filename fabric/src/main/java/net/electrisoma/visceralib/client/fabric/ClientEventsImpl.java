//package net.electrisoma.resotech.client.fabric;
//
//import net.electrisoma.resotech.client.tooltips.TooltipHandler;
//
//import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
//
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.TooltipFlag;
//
//import java.util.List;
//
//public class ClientEventsImpl {
//    public static void init() {
//        ItemTooltipCallback.EVENT.register(ClientEventsImpl::getTooltip);
//    }
//
//    private static void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
//        TooltipHandler.addTooltip(stack, lines);
//    }
//}
