package net.electrisoma.visceralib.api.dsp.v1.openal;

public class DSPResource {

	public enum Type {
		FILTER,
		EFFECT,
		SLOT
	}

	public record Tracked(int id, Type type) {}
}
