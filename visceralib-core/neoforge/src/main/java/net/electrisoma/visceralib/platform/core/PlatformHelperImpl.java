package net.electrisoma.visceralib.platform.core;

import net.electrisoma.visceralib.platform.core.services.IPlatformHelper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import com.google.auto.service.AutoService;

@AutoService(IPlatformHelper.class)
public final class PlatformHelperImpl implements IPlatformHelper {

	private String cachedModVersion;
	private String cachedMcVersion;
	private IEventBus modBus;

	@Override
	public <T> void registerModBus(T modBus) {
		if (!(modBus instanceof IEventBus bus))
			return;
		this.modBus = bus;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getModEventBus() {
		return (T) modBus;
	}

	@Override
	public PlatformEnum getPlatformInfo() {
		return PlatformEnum.NEOFORGE;
	}

	@Override
	public String getVersion(String id) {
		if (cachedModVersion == null) {
			cachedModVersion = ModList.get()
					.getModFileById(id).getMods().stream()
					.filter(info -> info.getModId().equals(id))
					.findFirst()
					.map(info -> info.getVersion().toString())
					.orElse("UNKNOWN");
		}
		return cachedModVersion;
	}

	@Override
	public String getMinecraftVersion() {
		if (cachedMcVersion == null) {
			cachedMcVersion = ModList.get()
					.getModContainerById("minecraft")
					.map(container -> container
							.getModInfo()
							.getVersion()
							.toString()
					)
					.orElse("UNKNOWN");
		}
		return cachedMcVersion;
	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDev() {
		return !FMLLoader.isProduction();
	}
}
