package io.sporkpgm.rotation;

import io.sporkpgm.map.SporkLoader;
import io.sporkpgm.map.SporkMap;
import io.sporkpgm.match.Match;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.SporkConfig.Settings;

public class RotationSlot {

	private SporkLoader loader;
	private boolean set;

	private SporkMap map;
	private Match match;

	public RotationSlot(SporkLoader loader) {
		this(loader, false);
	}

	public RotationSlot(SporkLoader loader, boolean set) {
		this.loader = loader;
		this.set = set;
	}

	public SporkLoader getLoader() {
		return loader;
	}

	public boolean getSet() {
		return set;
	}

	public SporkMap getMap() {
		return map;
	}

	public Match getMatch() {
		return match;
	}

	public void load() {
		if(map != null) {
			if(match == null) {
				Log.debug("Loading " + Settings.prefix() + (Rotation.get().getID() + 1) + " (current: " + Settings.prefix() + Rotation.get().getID() + ")");
				match = new Match(map, Rotation.get().getID() + 1);
				map.load(match);
			}

			return;
		}

		Log.debug("Loading " + Settings.prefix() + (Rotation.get().getID() + 1) + " (current: " + Settings.prefix() + Rotation.get().getID() + ")");
		map = loader.build();
		match = new Match(map, Rotation.get().getID() + 1);
		map.load(match);
	}

	public void unload() {
		this.map.unload(match);
	}

	public static Rotation get() {
		return Rotation.get();
	}

}
