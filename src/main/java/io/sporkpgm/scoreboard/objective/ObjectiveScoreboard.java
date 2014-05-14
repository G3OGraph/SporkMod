package io.sporkpgm.scoreboard.objective;

import io.sporkpgm.module.ObjectiveModule;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.scoreboard.ScoreboardEntry;
import io.sporkpgm.scoreboard.ScoreboardHandler;
import io.sporkpgm.scoreboard.SporkScoreboard;
import io.sporkpgm.scoreboard.UpdateableEntry;
import io.sporkpgm.util.Log;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectiveScoreboard extends SporkScoreboard {

	private List<ObjectiveModule> objectives;
	private Map<TeamModule, List<ObjectiveModule>> teams;

	public ObjectiveScoreboard(ScoreboardHandler handler) {
		super(ChatColor.GOLD + "Objectives", handler);
	}

	public void setup() {
		objectives = handler.getMap().getModules().getModules(ObjectiveModule.class);
		teams = new HashMap<>();

		for(ObjectiveModule objective : objectives) {
			if(!teams.containsKey(objective.getTeam())) {
				teams.put(objective.getTeam(), new ArrayList<ObjectiveModule>());
			}

			teams.get(objective.getTeam()).add(objective);
			Log.debug("Added " + objective + " to " + objective.getTeam());
		}

		int score = 1;
		for(TeamModule team : teams.keySet()) {
			List<ObjectiveModule> objectives = teams.get(team);

			if(objectives.size() > 0) {
				if(score > 1) {
					blank(score);
					score++;
				}

				for(ObjectiveModule objective : objectives) {
					getObjective(objective).setValue(score);
					score++;
				}

				getTeam(team).setValue(score);
				score++;
			}
		}
	}

	public void update() {
		for(TeamModule team : teams.keySet()) {
			getTeam(team).update();

			for(ObjectiveModule objective : teams.get(team)) {
				getObjective(objective).update();
			}
		}
	}

}
