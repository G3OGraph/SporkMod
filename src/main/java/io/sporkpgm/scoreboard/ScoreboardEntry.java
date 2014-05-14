package io.sporkpgm.scoreboard;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.ChatColor;
import io.sporkpgm.util.ClassUtils;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.ScoreboardUtil;
import io.sporkpgm.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Method;

public class ScoreboardEntry {

	private boolean active = true;
	private boolean set;

	protected String name;
	protected SporkScoreboard scoreboard;

	protected Team team;
	protected Score score;

	public ScoreboardEntry(String name, SporkScoreboard scoreboard) {
		this.name = name;
		this.scoreboard = scoreboard;
		score();
	}

	public boolean isActive() {
		return active;
	}

	public String getName() {
		return name;
	}

	public SporkScoreboard getScoreboard() {
		return scoreboard;
	}

	public Score getScore() {
		return score;
	}

	public int getValue() {
		Preconditions.checkState(active, "Scoreboard Entry is inactive");
		return score.getScore();
	}

	public void setValue(int value) {
		setValue(true, value);
	}

	public void setValue(boolean debug, int value) {
		Preconditions.checkState(active, "Scoreboard Entry is inactive");
		// Log.debug(7);
		if(debug) {
			Log.debug("Set value of '" + name + "' to " + value);
		}
		this.score.setScore(value);
		set = true;

		if(!scoreboard.getEntries().contains(this)) {
			this.scoreboard.getEntries().add(this);
		}
	}

	public void setValue(int... scores) {
		setValue(true, scores);
	}

	public void setValue(boolean debug, int... scores) {
		for(int score : scores) {
			setValue(debug, score);
		}
	}

	public void remove() {
		this.active = false;
		scoreboard.reset();
	}

	public boolean isSet() {
		return set;
	}

	public void update(String name) {
		// Log.debug("Doing okay (nothing)");
		int value = getValue();
		// Log.debug("Doing okay (fetched value)");
		remove();
		// Log.debug("Doing okay (removed)");
		this.active = true;
		this.name = name;
		// Log.debug("Doing okay (updated fields)");
		score();
		// Log.debug("Doing okay (setup score)");
		setValue(false, value);
		// Log.debug("Doing okay (set score value)");
	}

	protected void score() {
		create();
		Preconditions.checkState(score != null, "Unable to create a Score for " + scoreboard);
	}

	public void create() {
		this.score = null;
		Objective objective = scoreboard.getObjective();

		try {
			Method getScore = Objective.class.getDeclaredMethod("getScore", String.class);
			getScore.setAccessible(true);
			this.score = (Score) getScore.invoke(objective, name);
		} catch(Exception e) {
			// Log.exception(e);
		}

		if(score == null) {
			try {
				String stripped = ChatColor.stripColor(name).trim();
				String[] split = StringUtil.trim(name, 48, 3);

				OfflinePlayer player = Bukkit.getOfflinePlayer(split[1]);
				this.score = objective.getScore(player);

				StringBuilder spaces = new StringBuilder();
				boolean done = false;
				while(!done) {
					try {
						this.team = objective.getScoreboard().registerNewTeam(StringUtil.trim(spaces.toString() + stripped, 16));
						this.team.setPrefix(split[0]);
						this.team.setDisplayName(name);
						this.team.setSuffix(split[2]);
						this.team.addPlayer(player);
						done = true;
					} catch(IllegalArgumentException e) {
						spaces.append(" ");
						Log.debug("'" + StringUtil.trim(spaces.toString() + stripped, 16) + "' is in use already!");
					}
				}
			} catch(Exception e) {
				Log.exception(e);
			}
		}
	}

	@Override
	public String toString() {
		return ClassUtils.build(this);
	}

}
