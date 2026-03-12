package net.electrisoma.visceralib.event.item.v1.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class VisceralItemTooltipEvent {

	private VisceralItemTooltipEvent() {}

	@FunctionalInterface
	public interface Hook {

		void onItemTooltip(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> components);
	}
}
