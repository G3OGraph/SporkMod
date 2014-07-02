package in.parapengu.spork.scoreboard.entry;

import in.parapengu.commons.utils.StringUtils;
import in.parapengu.spork.scoreboard.BoardManager;
import in.parapengu.spork.scoreboard.boards.Board;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BoardEntry {

	private static int count = 1;

	private BoardManager manager;
	private String content;
	private String[] entry;

	private Map<Board, BoardEntryValues> boards;

	public BoardEntry(String content) {
		this.content = content;
	}

	public void setContent(String content) {
		this.content = content;
		update();
	}

	public void register(BoardManager manager) {
		this.manager = manager;

		entry = StringUtils.trim(this.content, 48, 3);
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

		String[] content = StringUtils.trim(this.content, 48, 3);
		if(content[1].equals(entry[1])) {
			for(Entry<Board, BoardEntryValues> entry : boards.entrySet()) {
				Team team = entry.getValue().getTeam();
				team.setPrefix(content[0]);
				team.setSuffix(content[2]);
			}

			return;
		}

		entry = content;
		for(Entry<Board, BoardEntryValues> entry : boards.entrySet()) {
			int value = entry.getValue().getScore().getScore();
			entry.getKey().remove(content[1]);

			Score score = entry.getKey().getScore(content[1]);
			score.setScore(value);
			entry.getValue().setScore(score);
		}
	}

	public void remove(Board board) {
		boards.remove(board);
		board.remove(entry[1]);
	}

}
