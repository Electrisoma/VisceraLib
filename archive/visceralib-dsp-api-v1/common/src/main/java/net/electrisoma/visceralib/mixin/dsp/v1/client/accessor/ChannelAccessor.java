package net.electrisoma.visceralib.mixin.dsp.v1.client.accessor;

import com.mojang.blaze3d.audio.Channel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Channel.class)
public interface ChannelAccessor {

	@Accessor("source")
	int getSourceId();
}
