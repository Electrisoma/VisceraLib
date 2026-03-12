package net.electrisoma.visceralib.platform.core.event.common;

import net.electrisoma.visceralib.api.core.event.IEventBusHelper;
import net.electrisoma.visceralib.event.core.common.VisceralTagsEvent;
import net.electrisoma.visceralib.platform.core.services.event.common.VisceraLibCoreCommonEvents;

import net.neoforged.neoforge.event.TagsUpdatedEvent;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibCoreCommonEvents.class)
public final class VisceraLibCoreCommonEventsImpl implements VisceraLibCoreCommonEvents, IEventBusHelper {

	@Override
	public void registerTagsLoaded(VisceralTagsEvent.Hook h) {
		withNeoForgeBus(bus -> bus.addListener((TagsUpdatedEvent e) -> {
			boolean isClient = e.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED;
			h.onTagsLoaded(e.getRegistryAccess(), isClient);
		}));
	}
}
