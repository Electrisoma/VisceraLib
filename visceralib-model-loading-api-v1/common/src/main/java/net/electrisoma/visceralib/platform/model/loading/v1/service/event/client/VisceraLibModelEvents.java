package net.electrisoma.visceralib.platform.model.loading.v1.service.event.client;

import net.electrisoma.visceralib.api.core.services.ServiceHelper;
import net.electrisoma.visceralib.event.model.loading.v1.client.VisceralModelEvent;

import java.util.function.Consumer;

public interface VisceraLibModelEvents {

	VisceraLibModelEvents INSTANCE = ServiceHelper.load(VisceraLibModelEvents.class);

	void onModifyBakingResult(Consumer<VisceralModelEvent.ModifyBakingResult> handler);
	void onRegisterAdditional(Consumer<VisceralModelEvent.RegisterAdditional> handler);
	void onRegisterGeometryLoaders(Consumer<VisceralModelEvent.RegisterGeometryLoaders> handler);
}
