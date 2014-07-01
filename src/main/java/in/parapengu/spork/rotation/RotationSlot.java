package in.parapengu.spork.rotation;

import in.parapengu.spork.map.MapLoader;
import in.parapengu.spork.map.SporkMap;
import in.parapengu.spork.match.Match;

public class RotationSlot {

	private MapLoader loader;
	private SporkMap map;
	private Match match;

	public RotationSlot(MapLoader loader) {
		this.loader = loader;
	}

	public MapLoader getLoader() {
		return loader;
	}

	public SporkMap getMap() {
		return map;
	}

	public Match getMatch() {
		return match;
	}

	public void load() {
		/*
		 * Create the SporkMap and Match
		 */
	}

}
