package net.electrisoma.visceralib.api.dsp.v1;

import net.electrisoma.visceralib.event.dsp.v1.client.DSPPipelineDefinition;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class DSPContext implements DSPPipelineDefinition.Context {

	private final SoundInstance sound;
	private final LocalPlayer player;
	private final ClientLevel level;
	private final List<AppliedChain> pipeline = new ArrayList<>();

	public DSPContext(SoundInstance sound, LocalPlayer player, ClientLevel level) {
		this.sound = sound;
		this.player = player;
		this.level = level;
	}

	@Override
	public SoundInstance getSound() {
		return sound;
	}

	@Override
	public LocalPlayer getPlayer() {
		return player;
	}

	@Override
	public ClientLevel getLevel() {
		return level;
	}

	@Override
	public void addPipeline(ResourceLocation id, float wetness, float width, boolean global) {
		pipeline.add(new DSPPipelineDefinition.Context.AppliedChain(id, wetness, width, global));
	}

	@Override
	public List<DSPPipelineDefinition.Context.AppliedChain> getAppliedChains() {
		return pipeline;
	}
}
