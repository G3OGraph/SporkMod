package in.parapengu.spork.scoreboard.entry;

import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

public class BoardEntryValues {

	private Score score;
	private Team team;

	public BoardEntryValues(Score score, Team team) {
		this.score = score;
		this.team = team;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public Team getTeam() {
		return team;
	}

}
