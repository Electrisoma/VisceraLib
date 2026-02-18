package net.electrisoma.visceralib.event.dsp.v1.client;

import net.neoforged.bus.api.Event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DSPPipelineEventNeoForge extends Event implements DSPPipelineDefinition.Context {

	private final DSPPipelineDefinition.Context context;

	public DSPPipelineEventNeoForge(DSPPipelineDefinition.Context context) {
		this.context = context;
	}

	@Override
	public SoundInstance getSound() {
		return context.getSound();
	}

	@Override
	public LocalPlayer getPlayer() {
		return context.getPlayer();
	}

	@Override
	public ClientLevel getLevel() {
		return context.getLevel();
	}

	@Override
	public void addPipeline(ResourceLocation id, float wetness, float width, boolean global) {
		context.addPipeline(id, wetness, width, global);
	}

	@Override
	public List<AppliedChain> getAppliedChains() {
		return context.getAppliedChains();
	}
}
