package net.electrisoma.visceralib.platform.item.v1.service.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.item.v1.client.VisceralItemTooltipEvent;

public interface VisceraLibItemClientEvents {

	VisceraLibItemClientEvents INSTANCE = ServiceHelper.load(VisceraLibItemClientEvents.class);

	void registerTooltipCallback(VisceralItemTooltipEvent.Hook handler);
}
