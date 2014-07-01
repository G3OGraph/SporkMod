package in.parapengu.spork.scoreboard;

import in.parapengu.spork.scoreboard.boards.Board;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class BoardEntry {

	private static int count = 1;

	private BoardManager manager;
	private String content;
	private String[] entry;

	private List<Score> scores;
	private List<Team> teams;

	public BoardEntry(BoardManager manager, String content) {
		this.manager = manager;
		this.content = content;

		this.teams = new ArrayList<>();
		for(Board board : manager.getBoards()) {
			this.teams.add(board.register("entry-" + count));
		}

		count++;
		update();
	}

	public void update() {
		StringBuilder builder = new StringBuilder();
		for(String string : entry) {
			builder.append(string);
		}

		if(builder.toString().equals(content)) {
			return;
		}


	}

}
