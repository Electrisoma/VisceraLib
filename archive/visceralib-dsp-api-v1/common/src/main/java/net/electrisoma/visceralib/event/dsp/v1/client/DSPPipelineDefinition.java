package net.electrisoma.visceralib.event.dsp.v1.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class DSPPipelineDefinition {

	private DSPPipelineDefinition() {}

	public interface Context {

		SoundInstance getSound();
		LocalPlayer getPlayer();
		ClientLevel getLevel();

		void addPipeline(ResourceLocation pipelineId, float wetness, float width, boolean global);

		List<AppliedChain> getAppliedChains();

		record AppliedChain(ResourceLocation id, float wetness, float width, boolean global) {}
	}

	@FunctionalInterface
	public interface Listener {

		void onApply(Context context);
	}
}
