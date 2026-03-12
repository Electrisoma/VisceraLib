package net.electrisoma.visceralib.api.datagen.v1.providers.client;

import net.electrisoma.visceralib.api.core.resources.RLUtils;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class VisceralSoundProvider implements DataProvider {

	private final PackOutput output;
	private final String modid;
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;

	public VisceralSoundProvider(
			PackOutput output,
			String modid,
			CompletableFuture<HolderLookup.Provider> lookupProvider
	) {
		this.output = output;
		this.modid = modid;
		this.lookupProvider = lookupProvider;
	}

	protected abstract void registerSounds(HolderLookup.Provider lookupProvider, SoundBuilder builder);

	@Override
	public CompletableFuture<?> run(CachedOutput writer) {
		return this.lookupProvider.thenCompose(lookupProvider -> {
			TreeMap<String, SoundDefinition> sounds = new TreeMap<>();

			registerSounds(lookupProvider, (id, definition) -> {
				if (sounds.put(id.getPath(), definition) == null)
					return;
				throw new IllegalStateException("Duplicate sound event: " + id);
			});

			JsonObject root = new JsonObject();
			sounds.forEach((name, def) -> root.add(name, def.serialize()));

			Path path = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
					.resolve(this.modid)
					.resolve("sounds.json");

			return DataProvider.saveStable(writer, root, path);
		});
	}

	@Override
	public String getName() {
		return "VisceralSoundProvider: " + modid;
	}

	@FunctionalInterface
	public interface SoundBuilder {

		void add(ResourceLocation id, SoundDefinition definition);

		default void add(Supplier<SoundEvent> event, SoundDefinition definition) {
			add(event.get().getLocation(), definition);
		}

		default void add(SoundEvent event, SoundDefinition definition) {
			add(event.getLocation(), definition);
		}
	}

	public static class SoundDefinition {

		private final List<SoundEntry> entries = new ArrayList<>();
		private String subtitle;
		private boolean replace = false;

		public static SoundDefinition create() {
			return new SoundDefinition();
		}

		public SoundDefinition with(SoundEntry entry) {
			this.entries.add(entry);
			return this;
		}

		public SoundDefinition with(SoundEntry entry, int count) {
			IntStream.rangeClosed(1, count).forEach(i -> {
				ResourceLocation original = entry.location;
				ResourceLocation numbered = RLUtils.path(original.getNamespace(), original.getPath() + i);
				this.entries.add(entry.copy(numbered));
			});
			return this;
		}

		public SoundDefinition subtitle(String subtitle) {
			this.subtitle = subtitle;
			return this;
		}

		public SoundDefinition replace() {
			this.replace = true;
			return this;
		}

		public JsonObject serialize() {
			JsonObject json = new JsonObject();
			if (replace) json.addProperty("replace", true);
			if (subtitle != null) json.addProperty("subtitle", subtitle);

			JsonArray sounds = new JsonArray();
			entries.stream().map(SoundEntry::serialize).forEach(sounds::add);
			json.add("sounds", sounds);
			return json;
		}
	}

	public static class SoundEntry {

		private final ResourceLocation location;
		private final String type;
		private float volume = 1.0f;
		private float pitch = 1.0f;
		private int weight = 1;
		private int attenuationDistance = 16;
		private boolean stream = false;
		private boolean preload = false;

		private SoundEntry(ResourceLocation location, String type) {
			this.location = location;
			this.type = type;
		}

		public static SoundEntry file(ResourceLocation location) {
			return new SoundEntry(location, "file");
		}

		public static SoundEntry event(ResourceLocation location) {
			return new SoundEntry(location, "event");
		}

		public SoundEntry volume(float volume) {
			this.volume = volume;
			return this;
		}

		public SoundEntry pitch(float pitch) {
			this.pitch = pitch;
			return this;
		}

		public SoundEntry weight(int weight) {
			this.weight = weight;
			return this;
		}

		public SoundEntry attenuationDistance(int distance) {
			this.attenuationDistance = distance;
			return this;
		}

		public SoundEntry stream(boolean stream) {
			this.stream = stream;
			return this;
		}

		public SoundEntry preload(boolean preload) {
			this.preload = preload;
			return this;
		}

		protected SoundEntry copy(ResourceLocation newLoc) {
			SoundEntry copy = new SoundEntry(newLoc, this.type);
			copy.volume = this.volume;
			copy.pitch = this.pitch;
			copy.weight = this.weight;
			copy.attenuationDistance = this.attenuationDistance;
			copy.stream = this.stream;
			copy.preload = this.preload;
			return copy;
		}

		public JsonObject serialize() {
			JsonObject json = new JsonObject();
			json.addProperty("name", location.toString());
			if (!type.equals("file")) json.addProperty("type", type);
			if (volume != 1.0f) json.addProperty("volume", volume);
			if (pitch != 1.0f) json.addProperty("pitch", pitch);
			if (weight != 1) json.addProperty("weight", weight);
			if (attenuationDistance != 16) json.addProperty("attenuation_distance", attenuationDistance);
			if (stream) json.addProperty("stream", true);
			if (preload) json.addProperty("preload", true);
			return json;
		}
	}
}
