package io.sporkpgm.scoreboard.objective;

import io.sporkpgm.module.modules.team.TeamModule;

public interface TeamObjective {

	public String getName();

	public TeamModule getTeam();

	public boolean isComplete();

}
