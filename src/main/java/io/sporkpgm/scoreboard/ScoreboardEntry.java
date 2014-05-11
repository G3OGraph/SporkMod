package io.sporkpgm.scoreboard;

import com.google.common.base.Preconditions;
import io.sporkpgm.util.ClassUtils;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.ScoreboardUtil;
import io.sporkpgm.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.lang.reflect.Method;

public class ScoreboardEntry {

	private boolean active = true;
	private boolean set;

	protected String name;
	protected SporkScoreboard scoreboard;
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
		if(debug) {
			Log.debug(7);
			Log.info("Set value of '" + name + "' to " + value);
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
		Log.debug("Doing okay (nothing)");
		int value = getValue();
		Log.debug("Doing okay (fetched value)");
		remove();
		Log.debug("Doing okay (removed)");
		this.active = true;
		this.name = name;
		Log.debug("Doing okay (updated fields)");
		score();
		Log.debug("Doing okay (setup score)");
		setValue(false, value);
		Log.debug("Doing okay (set score value)");
	}

	protected void score() {
		create();
		Preconditions.checkState(score != null, "Unable to create a Score for " + scoreboard);
	}

	public void create() {
		this.score = null;
		Objective objective = scoreboard.getObjective();
		String[] split = StringUtil.trim(name, 48, 3);

		try {
			Method getScore = Objective.class.getDeclaredMethod("getScore", String.class);
			getScore.setAccessible(true);
			this.score = (Score) getScore.invoke(objective, split[1]);
		} catch(Exception e) {
			// Log.exception(e);
		}

		if(score == null) {
			try {
				this.score = objective.getScore(Bukkit.getOfflinePlayer(split[1]));
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
