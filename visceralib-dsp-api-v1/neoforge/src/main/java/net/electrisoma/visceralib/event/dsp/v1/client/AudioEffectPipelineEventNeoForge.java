package net.electrisoma.visceralib.event.dsp.v1.client;

import net.neoforged.bus.api.Event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class AudioEffectPipelineEventNeoForge extends Event implements VisceralAudioEffectPipelineEvent.Context {

	private final VisceralAudioEffectPipelineEvent.Context delegate;

	public AudioEffectPipelineEventNeoForge(VisceralAudioEffectPipelineEvent.Context delegate) {
		this.delegate = delegate;
	}

	@Override
	public SoundInstance getSound() {
		return delegate.getSound();
	}

	@Override
	public LocalPlayer getPlayer() {
		return delegate.getPlayer();
	}

	@Override
	public ClientLevel getLevel() {
		return delegate.getLevel();
	}

	@Override
	public void addPipeline(ResourceLocation id, float wetness, float width, boolean global) {
		delegate.addPipeline(id, wetness, width, global);
	}

	@Override
	public List<AppliedChain> getAppliedChains() {
		return delegate.getAppliedChains();
	}
}
