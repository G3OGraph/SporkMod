package in.parapengu.spork.scoreboard.boards;

import in.parapengu.spork.module.modules.team.TeamModule;
import in.parapengu.spork.scoreboard.entry.BoardEntry;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Board {

	protected Scoreboard scoreboard;

	public Board(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void register(TeamModule team) {

	}

	public Team getTeam(String name) {
		return null;
	}

	public Score getScore(String name) {
		return null;
	}

	public void refresh() { /* nothing */ }

	public void refresh(BoardEntry entry) { /* nothing */ }

	public void remove(String name) { /* nothing */ }

	public void remove(BoardEntry entry) { /* nothing */ }

}
