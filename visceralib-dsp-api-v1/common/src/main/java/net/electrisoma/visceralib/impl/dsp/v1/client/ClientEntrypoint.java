package net.electrisoma.visceralib.impl.dsp.v1.client;

import net.electrisoma.visceralib.api.core.debug.EntrypointMessages;
import net.electrisoma.visceralib.api.core.resources.RLUtils;
import net.electrisoma.visceralib.impl.dsp.v1.Constants;
import net.electrisoma.visceralib.platform.dsp.v1.services.event.client.VisceraLibDSPClientEvents;

import net.minecraft.world.level.block.Blocks;

public final class ClientEntrypoint {

	public static void init() {
		EntrypointMessages.onClient(Constants.LOG, Constants.MOD_ID, Constants.NAME);

		VisceraLibDSPClientEvents.INSTANCE.registerPipelineProvider(context -> {

			var player = context.getPlayer();
			var level = context.getLevel();
			if (player == null || level == null) return;

			var pos = player.blockPosition();
			var stateAtHead = level.getBlockState(pos.above());

			boolean isCaveAir = stateAtHead.is(Blocks.CAVE_AIR);
			boolean isEnclosed = !level.canSeeSky(pos) && pos.getY() < level.getSeaLevel();

//			if (isCaveAir || isEnclosed) {
//				context.addPipeline(
//						RLUtils.path("testmod", "spring"),
//						1.0f,
//						1.0f,
//						true
//				);
//			}

			if (context.getPlayer().isInvisible()) {
				context.addPipeline(
						RLUtils.path("testmod", "spring"),
						1.0f,
						1.0f,
						false
				);
			}
		});
	}
}
