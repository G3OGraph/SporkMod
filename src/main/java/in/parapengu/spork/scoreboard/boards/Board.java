package in.parapengu.spork.scoreboard.boards;

import in.parapengu.spork.module.modules.team.TeamModule;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Board {

	private Scoreboard scoreboard;

	public Board(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void register(TeamModule team) {

	}

	public Team register(String name) {
		Team team = scoreboard.getTeam(name);
		if(team != null) {
			return team;
		}

		return scoreboard.registerNewTeam("name");
	}

	public void refresh() { /* nothing */ }

}
