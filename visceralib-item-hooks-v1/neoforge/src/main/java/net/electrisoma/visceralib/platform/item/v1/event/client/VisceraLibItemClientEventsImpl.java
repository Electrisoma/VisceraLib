package net.electrisoma.visceralib.platform.item.v1.event.client;

import net.electrisoma.visceralib.api.core.event.IEventBusHelper;
import net.electrisoma.visceralib.event.item.v1.client.VisceralItemTooltipEvent;
import net.electrisoma.visceralib.platform.item.v1.service.event.client.VisceraLibItemClientEvents;

import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibItemClientEvents.class)
public final class VisceraLibItemClientEventsImpl implements VisceraLibItemClientEvents, IEventBusHelper {

	@Override
	public void registerTooltipCallback(VisceralItemTooltipEvent.Hook h) {
		withNeoForgeBus(bus -> bus.addListener((ItemTooltipEvent e) ->
				h.onItemTooltip(
						e.getItemStack(),
						e.getContext(),
						e.getFlags(),
						e.getToolTip()
				)
		));
	}
}
