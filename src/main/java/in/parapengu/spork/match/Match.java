package in.parapengu.spork.match;

import in.parapengu.spork.map.SporkMap;

public class Match {

	private int id;
	private SporkMap map;

	public Match(int id, SporkMap map) {
		this.id = id;
		this.map = map;
	}

	public int getId() {
		return id;
	}

	public SporkMap getMap() {
		return map;
	}

}
