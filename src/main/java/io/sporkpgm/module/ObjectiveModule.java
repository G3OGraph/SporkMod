package io.sporkpgm.module;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.scoreboard.exceptions.IllegalScoreboardException;
import io.sporkpgm.scoreboard.objective.ObjectiveScoreboard;
import io.sporkpgm.scoreboard.objective.TeamObjective;
import io.sporkpgm.tourney.TourneyScore;
import io.sporkpgm.util.ClassUtils;
import io.sporkpgm.util.Log;
import io.sporkpgm.win.WinCondition;
import org.bukkit.ChatColor;

public abstract class ObjectiveModule extends Module implements TeamObjective, TourneyScore, WinCondition {

	protected SporkMap map;
	protected ObjectiveScoreboard scoreboard;

	protected boolean completed;
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
		this.completed = false;
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

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
		this.scoreboard.update();
	}

	public ChatColor getColor() {
		return completed ? ChatColor.GREEN : ChatColor.RED;
	}

	@Override
	public void load() {
		map.getConditionSet(team).register("objective", this);
	}

	@Override
	public String toString() {
		return ClassUtils.build(this);
	}

}
