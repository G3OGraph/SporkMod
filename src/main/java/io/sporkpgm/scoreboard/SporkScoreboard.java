package io.sporkpgm.scoreboard;

import io.sporkpgm.module.ObjectiveModule;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.scoreboard.objective.ObjectiveEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class SporkScoreboard {

	protected String title;
	protected ScoreboardHandler handler;
	protected Scoreboard scoreboard;
	protected Objective objective;

	protected List<ScoreboardEntry> entries;

	public SporkScoreboard(String title, ScoreboardHandler handler) {
		this.title = title;
		this.handler = handler;
		this.scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		this.entries = new ArrayList<>();
		reset();
	}

	public void reset() {
		Map<ScoreboardEntry, Integer> scores = new HashMap<>();
		for(ScoreboardEntry entry : entries) {
			if(entry.isActive()) {
				scores.put(entry, entry.getValue());
			}
		}

		if(objective != null) {
			objective.unregister();
		}

		objective = scoreboard.registerNewObjective("Sidebar", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(title);

		for(Entry<ScoreboardEntry, Integer> entry : scores.entrySet()) {
			entry.getKey().score();
			entry.getKey().setValue(entry.getValue());
		}
	}

	public ScoreboardHandler getHandler() {
		return handler;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public Objective getObjective() {
		return objective;
	}

	public boolean hasEntry(String name) {
		for(ScoreboardEntry entry : entries) {
			if(entry.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public List<ScoreboardEntry> getEntries() {
		return entries;
	}

	public ScoreboardEntry getEntry(String name) {
		return getEntry(name, false);
	}

	public ScoreboardEntry getEntry(String name, boolean create) {
		for(ScoreboardEntry entry : entries) {
			if(entry.getName().equals(name)) {
				if(create && entry.isSet()) {
					return getEntry(name + " ");
				} else {
					return entry;
				}
			}
		}

		return new ScoreboardEntry(name, this);
	}

	public ObjectiveEntry getObjective(ObjectiveModule module) {
		for(ScoreboardEntry entry : entries) {
			if(entry instanceof ObjectiveEntry) {
				ObjectiveEntry objective = (ObjectiveEntry) entry;
				if(objective.getObjective().equals(module)) {
					return objective;
				}
			}
		}

		return new ObjectiveEntry(module, this);
	}

	public TeamEntry getTeam(TeamModule module) {
		for(ScoreboardEntry entry : entries) {
			if(entry instanceof TeamEntry) {
				TeamEntry team = (TeamEntry) entry;
				if(team.getTeam().equals(module)) {
					return team;
				}
			}
		}

		return new TeamEntry(module, this);
	}

	public ScoreboardEntry blank(int score) {
		StringBuilder spaces = new StringBuilder(ChatColor.WHITE + "");
		ScoreboardEntry sbEntry = getEntry(spaces.toString());
		while(sbEntry.isSet() && spaces.length() <= 16) {
			spaces.append(" ");
			sbEntry = getEntry(spaces.toString());
		}

		sbEntry.setValue(1, score);
		return sbEntry;
	}

	public abstract void setup();

}
