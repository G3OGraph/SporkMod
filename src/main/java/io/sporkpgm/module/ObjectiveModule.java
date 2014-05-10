package io.sporkpgm.module;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.scoreboard.exceptions.IllegalScoreboardException;
import io.sporkpgm.scoreboard.objective.ObjectiveScoreboard;
import io.sporkpgm.scoreboard.objective.TeamObjective;
import io.sporkpgm.tourney.TourneyScore;
import io.sporkpgm.util.ClassUtils;
import io.sporkpgm.util.Log;

public abstract class ObjectiveModule extends Module implements TeamObjective, TourneyScore {

	protected SporkMap map;
	protected ObjectiveScoreboard scoreboard;

	protected boolean complete;
	protected String name;
	protected TeamModule team;

	public ObjectiveModule(String name, TeamModule team) {
		this.map = team.getMap();
		try {
			this.scoreboard = map.getScoreboard().get(ObjectiveScoreboard.class);
		} catch(IllegalScoreboardException e) {
			Log.exception(e);
		}

		this.name = name;
		this.team = team;
		this.complete = false;
	}

	public SporkMap getMap() {
		return map;
	}

	public String getName() {
		return name;
	}

	public TeamModule getTeam() {
		return team;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
		this.scoreboard.update();
	}

	@Override
	public String toString() {
		return ClassUtils.build(this);
	}

}
