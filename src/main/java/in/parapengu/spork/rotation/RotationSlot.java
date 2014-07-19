package in.parapengu.spork.rotation;

import in.parapengu.spork.exception.map.MapLoadException;
import in.parapengu.spork.map.MapLoader;
import in.parapengu.spork.map.SporkMap;
import in.parapengu.spork.match.Match;

public class RotationSlot {

	private int id;
	private boolean loaded;

	private MapLoader loader;
	private SporkMap map;
	private Match match;

	public RotationSlot(MapLoader loader) {
		this.loader = loader;
	}

	public MapLoader getLoader() {
		return loader;
	}

	public void setLoader(MapLoader loader) throws MapLoadException {
		this.loader = loader;

		if(loaded) {
			map.unload();
			load(id);
		}
	}

	public SporkMap getMap() {
		return map;
	}

	public Match getMatch() {
		return match;
	}

	public void load(int id) throws MapLoadException {
		map = new SporkMap(loader);
		match = new Match(id, map);
		map.load(match);
		loaded = true;
		this.id = id;
	}

}
