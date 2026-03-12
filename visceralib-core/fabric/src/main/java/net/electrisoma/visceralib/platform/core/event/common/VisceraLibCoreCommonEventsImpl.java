package net.electrisoma.visceralib.platform.core.event.common;

import net.electrisoma.visceralib.event.core.common.VisceralTagsEvent;
import net.electrisoma.visceralib.platform.core.services.event.common.VisceraLibCoreCommonEvents;

import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibCoreCommonEvents.class)
public final class VisceraLibCoreCommonEventsImpl implements VisceraLibCoreCommonEvents {

	@Override
	public void registerTagsLoaded(VisceralTagsEvent.Hook h) {
		CommonLifecycleEvents.TAGS_LOADED.register(h::onTagsLoaded);
	}
}
