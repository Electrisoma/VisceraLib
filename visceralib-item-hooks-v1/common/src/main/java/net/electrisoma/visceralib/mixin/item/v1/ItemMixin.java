package net.electrisoma.visceralib.mixin.item.v1;

import net.electrisoma.visceralib.api.item.v1.client.ext.VisceralClientItemHooks;
import net.electrisoma.visceralib.api.item.v1.ext.VisceralItemHooks;

import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin implements VisceralItemHooks {

	@Override
	public void viscera$initializeClient(Consumer<VisceralClientItemHooks> consumer) {
		if (this instanceof VisceralClientItemHooks clientHooks) {
			consumer.accept(clientHooks);
		}
	}
}
