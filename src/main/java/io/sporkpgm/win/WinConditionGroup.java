package io.sporkpgm.win;

import java.util.ArrayList;
import java.util.List;

public class WinConditionGroup implements WinCondition {

	protected List<WinCondition> conditions;

	public WinConditionGroup() {
		this.conditions = new ArrayList<>();
	}

	public void addCondition(WinCondition condition) {
		conditions.add(condition);
	}

	public boolean isCompleted() {
		if(conditions.size() == 0) {
			return false;
		}

		for(WinCondition condition : conditions) {
			if(!condition.isCompleted()) {
				return false;
			}
		}

		return true;
	}

}
