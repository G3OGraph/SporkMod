package in.parapengu.spork.scoreboard.boards;

import in.parapengu.spork.scoreboard.entry.BoardEntry;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class SidebarBoard extends Board {

	private int count = 1;
	private Objective objective;
	private List<BoardEntry> entries;

	public SidebarBoard(Scoreboard scoreboard) {
		super(scoreboard);
		this.entries = new ArrayList<>();
		refresh();
	}

	public void refresh() {
		this.objective = scoreboard.registerNewObjective("sidebar-" + count, "dummy");
		for(BoardEntry entry : entries) {
			entry.update();
		}
		count++;
	}

	@Override
	public Team getTeam(String name) {
		Team team = scoreboard.getTeam(name);
		if(team != null) {
			return team;
		}

		return scoreboard.registerNewTeam("name");
	}

	@Override
	public Score getScore(String name) {
		try {
			Objective.class.getMethod("getScore", String.class);
			return objective.getScore(name);
		} catch(NoSuchMethodException ex) {
			return objective.getScore(Bukkit.getOfflinePlayer(name));
		}
	}

	@Override
	public void refresh(BoardEntry entry) {
		super.refresh(entry);
	}

	@Override
	public void remove(String name) {
		Score score = getScore(name);
		if(score == null) {
			return;
		}

		try {
			Scoreboard.class.getMethod("resetScores", String.class);
			scoreboard.resetScores(name);
		} catch(NoSuchMethodException ex) {
			scoreboard.resetScores(Bukkit.getOfflinePlayer(name));
		}
	}

	@Override
	public void remove(BoardEntry entry) {
		entries.remove(entry);
		entry.remove(this);
	}

}
