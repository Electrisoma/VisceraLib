package net.electrisoma.visceralib.event.splashes.v1.client;

import net.electrisoma.visceralib.api.splashes.v1.client.SplashStyle;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.List;

public abstract class VisceralSplashEventNeoForge extends Event implements VisceralSplashEvent.Context, IModBusEvent {

	private final VisceralSplashEvent.Context context;

	protected VisceralSplashEventNeoForge(VisceralSplashEvent.Context context) {
		this.context = context;
	}

	@Override
	public ResourceManager getResourceManager() {
		return context.getResourceManager();
	}

	@Override
	public SplashStyle getStyle() {
		return context.getStyle();
	}

	@Override
	public List<String> getSplashPool() {
		return context.getSplashPool();
	}

	@Override
	public void setPrioritySplash(String s) {
		context.setPrioritySplash(s);
	}

	@Override
	public void addSplash(String s) {
		context.addSplash(s);
	}

	@Override
	public void addSplashFile(ResourceLocation f) {
		context.addSplashFile(f);
	}

	public static class Pre extends VisceralSplashEventNeoForge {

		public Pre(VisceralSplashEvent.Context context) {
			super(context);
		}
	}

	public static class Post extends VisceralSplashEventNeoForge {

		public Post(VisceralSplashEvent.Context context) {
			super(context);
		}
	}
}
