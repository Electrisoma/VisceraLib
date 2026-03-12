package net.electrisoma.visceralib.event.model.loading.v1.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VisceralModelEventHandlers {

	private static final List<Consumer<VisceralModelEvent.ModifyBakingResult>> BAKING_HANDLERS = new ArrayList<>();
	private static final List<Consumer<VisceralModelEvent.RegisterAdditional>> ADDITIONAL_HANDLERS = new ArrayList<>();
	private static final List<Consumer<VisceralModelEvent.RegisterGeometryLoaders>> GEOMETRY_HANDLERS = new ArrayList<>();

	public static void addBakingHandler(Consumer<VisceralModelEvent.ModifyBakingResult> handler) {
		BAKING_HANDLERS.add(handler);
	}

	public static void addAdditionalHandler(Consumer<VisceralModelEvent.RegisterAdditional> handler) {
		ADDITIONAL_HANDLERS.add(handler);
	}

	public static void addGeometryHandler(Consumer<VisceralModelEvent.RegisterGeometryLoaders> handler) {
		GEOMETRY_HANDLERS.add(handler);
	}

	public static void fireModifyBakingResult(VisceralModelEvent.ModifyBakingResult event) {
		BAKING_HANDLERS.forEach(h -> h.accept(event));
	}

	public static void fireRegisterAdditional(VisceralModelEvent.RegisterAdditional event) {
		ADDITIONAL_HANDLERS.forEach(h -> h.accept(event));
	}

	public static void fireRegisterGeometryLoaders(VisceralModelEvent.RegisterGeometryLoaders event) {
		GEOMETRY_HANDLERS.forEach(h -> h.accept(event));
	}
}
