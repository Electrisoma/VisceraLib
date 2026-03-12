package net.electrisoma.visceralib.api.core.event;

import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Consumer;

public interface IEventBusHelper {

	default void withModBus(Consumer<IEventBus> action) {
		IEventBus bus = IPlatformHelper.INSTANCE.getModEventBus();
		if (bus != null) action.accept(bus);
	}

	default void withNeoForgeBus(Consumer<IEventBus> action) {
		action.accept(NeoForge.EVENT_BUS);
	}
}
