package io.sporkpgm.win;

import java.util.HashMap;
import java.util.Map;

public class WinConditionSet {

	private Map<String, WinConditionGroup> map;

	public WinConditionSet() {
		this.map = new HashMap<>();
	}

	public void register(String type, WinCondition condition) {
		if(map.get(type) == null) {
			map.put(type, new WinConditionGroup());
		}

		map.get(type).addCondition(condition);
	}

	public boolean isCompleted() {
		for(String type : map.keySet()) {
			WinConditionGroup group = map.get(type);
			if(group.isCompleted()) {
				return true;
			}
		}

		return false;
	}

}
