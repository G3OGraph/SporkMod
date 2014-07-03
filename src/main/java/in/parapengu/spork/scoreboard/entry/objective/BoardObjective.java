package in.parapengu.spork.scoreboard.entry.objective;

import in.parapengu.spork.scoreboard.ScoreboardObjective;
import in.parapengu.spork.scoreboard.entry.BoardEntry;
import in.parapengu.spork.util.Chars;

public class BoardObjective extends BoardEntry {

	protected boolean complete;

	public BoardObjective(String content) {
		super(content);
	}

	public boolean isComplete() {
		return complete;
	}

	public Chars getCharacter() {
		return getClass().getAnnotation(ScoreboardObjective.class).character();
	}

	public String getPrefix() {
		return getCharacter() + "";
	}

}
