package net.electrisoma.visceralib.api.dsp.v1.openal;

import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.*;
import java.util.stream.IntStream;

public class DSPManager {

	private static final Map<Integer, List<DSPResource.Tracked>> ACTIVE_RESOURCES = new HashMap<>();
	private static final Stack<Integer> FILTER_POOL = new Stack<>(), EFFECT_POOL = new Stack<>(), SLOT_POOL = new Stack<>();

	private static int maxSends = 1;
	private static boolean initialized = false;

	public static void reinitialize() {
		dispose();
		if (!checkEfxSupport()) return;
		maxSends = queryMaxSends();
		initialized = true;
	}

	public static boolean isAvailable() {
		return initialized;
	}

	public static void cleanSource(int sourceId) {
		if (!initialized) return;
		List<DSPResource.Tracked> resources = ACTIVE_RESOURCES.remove(sourceId);
		if (resources == null) return;

		AL10.alSourcei(sourceId, EXTEfx.AL_DIRECT_FILTER, EXTEfx.AL_FILTER_NULL);
		IntStream.range(0, maxSends).forEach(i -> AL11.alSource3i(
				sourceId,
				EXTEfx.AL_AUXILIARY_SEND_FILTER,
				EXTEfx.AL_EFFECTSLOT_NULL,
				i,
				EXTEfx.AL_FILTER_NULL)
		);

		resources.stream().filter(res -> res.id() != AL10.AL_NONE).forEach(res -> {
			switch (res.type()) {
				case FILTER -> FILTER_POOL.push(res.id());
				case EFFECT -> EFFECT_POOL.push(res.id());
				case SLOT -> {
					EXTEfx.alAuxiliaryEffectSloti(res.id(), EXTEfx.AL_EFFECTSLOT_EFFECT, EXTEfx.AL_EFFECT_NULL);
					SLOT_POOL.push(res.id());
				}
			}
		});
	}

	public static int acquireFilter(int type) {
		if (!initialized) return 0;
		int id = FILTER_POOL.isEmpty() ? EXTEfx.alGenFilters() : FILTER_POOL.pop();
		EXTEfx.alFilteri(id, EXTEfx.AL_FILTER_TYPE, type);
		return id;
	}

	public static int acquireEffect(int type) {
		if (!initialized) return 0;
		int id = EFFECT_POOL.isEmpty() ? EXTEfx.alGenEffects() : EFFECT_POOL.pop();
		EXTEfx.alEffecti(id, EXTEfx.AL_EFFECT_TYPE, type);
		return id;
	}

	public static int acquireSlot() {
		if (!initialized) return 0;
		return SLOT_POOL.isEmpty() ? EXTEfx.alGenAuxiliaryEffectSlots() : SLOT_POOL.pop();
	}

	public static void track(int sourceId, int id, DSPResource.Type type) {
		if (id <= 0) return;
		ACTIVE_RESOURCES.computeIfAbsent(sourceId, k -> new ArrayList<>()).add(new DSPResource.Tracked(id, type));
	}

	public static boolean isTracked(int sourceId) {
		return initialized && ACTIVE_RESOURCES.containsKey(sourceId);
	}

	public static int getTrackedId(int sourceId, DSPResource.Type type, int index) {
		if (!initialized) return 0;
		List<DSPResource.Tracked> list = ACTIVE_RESOURCES.get(sourceId);
		if (list == null) return 0;
		int count = 0;
		for (DSPResource.Tracked r : list) {
			if (r.type() == type) {
				if (count == index) return r.id();
				count++;
			}
		}
		return 0;
	}

	private static int queryMaxSends() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer sends = stack.mallocInt(1);
			ALC10.alcGetIntegerv(ALC10.alcGetContextsDevice(ALC10.alcGetCurrentContext()), EXTEfx.ALC_MAX_AUXILIARY_SENDS, sends);
			return sends.get(0);
		}
	}

	public static int getMaxSends() {
		return maxSends;
	}

	private static boolean checkEfxSupport() {
		long device = ALC10.alcGetContextsDevice(ALC10.alcGetCurrentContext());
		return device != 0 && ALC.getCapabilities().ALC_EXT_EFX;
	}

	public static void dispose() {
		initialized = false;
		ACTIVE_RESOURCES.clear();
		while (!FILTER_POOL.isEmpty()) EXTEfx.alDeleteFilters(FILTER_POOL.pop());
		while (!EFFECT_POOL.isEmpty()) EXTEfx.alDeleteEffects(EFFECT_POOL.pop());
		while (!SLOT_POOL.isEmpty()) EXTEfx.alDeleteAuxiliaryEffectSlots(SLOT_POOL.pop());
	}
}
