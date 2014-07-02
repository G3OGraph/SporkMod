package in.parapengu.spork.scoreboard.entry;

import in.parapengu.spork.scoreboard.BoardManager;
import in.parapengu.spork.scoreboard.boards.Board;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardEntry {

	private static int count = 1;

	private BoardManager manager;
	private String content;
	private String[] entry;

	private Map<Board, BoardEntryValues> boards;

	public BoardEntry(String content) {
		this.content = content;
	}

	public void register(BoardManager manager) {
		this.manager = manager;

		update();
		this.boards = new HashMap<>();
		for(Board board : manager.getBoards()) {
			Score score = board.getScore(entry[1]);
			Team team = board.getTeam("entry-" + count);
			if(score == null || team == null) {
				continue;
			}

			boards.put(board, new BoardEntryValues(score, team));
		}

		count++;
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

	public void save() {

	}

	public void refresh() {
		update();
		save();
	}

}
