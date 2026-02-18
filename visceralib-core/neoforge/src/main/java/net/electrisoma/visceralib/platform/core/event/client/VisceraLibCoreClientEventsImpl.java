package net.electrisoma.visceralib.platform.core.event.client;

import net.electrisoma.visceralib.event.core.client.VisceralClientTickEvent;
import net.electrisoma.visceralib.platform.core.services.event.client.VisceraLibCoreClientEvents;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

import net.minecraft.client.Minecraft;

import com.google.auto.service.AutoService;

@AutoService(VisceraLibCoreClientEvents.class)
public final class VisceraLibCoreClientEventsImpl implements VisceraLibCoreClientEvents {

	@Override
	public void registerPreClientTick(VisceralClientTickEvent.Pre h) {
		NeoForge.EVENT_BUS.addListener((ClientTickEvent.Pre event) ->
				h.onClientTick(Minecraft.getInstance()));
	}

	@Override
	public void registerPostClientTick(VisceralClientTickEvent.Post h) {
		NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) ->
				h.onClientTick(Minecraft.getInstance()));
	}
}
