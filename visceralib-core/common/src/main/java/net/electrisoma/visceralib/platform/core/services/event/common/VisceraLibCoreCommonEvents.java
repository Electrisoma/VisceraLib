package net.electrisoma.visceralib.platform.core.services.event.common;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.core.common.VisceralTagsEvent;

public interface VisceraLibCoreCommonEvents {

	VisceraLibCoreCommonEvents INSTANCE = ServiceHelper.load(VisceraLibCoreCommonEvents.class);

	void registerTagsLoaded(VisceralTagsEvent.Hook handler);
}
