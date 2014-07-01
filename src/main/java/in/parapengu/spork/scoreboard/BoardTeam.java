package in.parapengu.spork.scoreboard;

import in.parapengu.spork.module.modules.team.TeamModule;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class BoardTeam {

	private TeamModule module;
	private List<Team> teams;



	public BoardTeam(TeamModule module) {
		this.module = module;
	}

}
