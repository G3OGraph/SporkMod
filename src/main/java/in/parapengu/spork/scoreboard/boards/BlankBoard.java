package in.parapengu.spork.scoreboard.boards;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BlankBoard extends Board {

	public BlankBoard(Scoreboard scoreboard) {
		super(scoreboard);
	}

	@Override
	public Team register(String name) {
		return null;
	}

}
