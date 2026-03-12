package net.electrisoma.visceralib.platform.item.v1.event.client;

import net.electrisoma.visceralib.event.item.v1.client.VisceralItemTooltipEvent;
import net.electrisoma.visceralib.platform.item.v1.service.event.client.VisceraLibItemClientEvents;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import com.google.auto.service.AutoService;


@AutoService(VisceraLibItemClientEvents.class)
public final class VisceraLibItemClientEventsImpl implements VisceraLibItemClientEvents {

	@Override
	public void registerTooltipCallback(VisceralItemTooltipEvent.Hook h) {
		ItemTooltipCallback.EVENT.register(h::onItemTooltip);
	}
}
