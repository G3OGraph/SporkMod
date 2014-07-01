package in.parapengu.spork.scoreboard;

import in.parapengu.spork.map.SporkMap;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;

public class BoardManager {

	private SporkMap map;
	private ScoreboardManager manager;

	private Map<String, Board> scoreboards;

	public BoardManager(SporkMap map) {
		this.map = map;
		this.manager = Bukkit.getScoreboardManager();

		this.scoreboards = new HashMap<>();
	}

	public SporkMap getMap() {
		return map;
	}

	public ScoreboardManager getManager() {
		return manager;
	}

	public Board getBoard(ScoreboardType type) {
		return getBoard(type.getKey());
	}

	public Board getBoard(String key) {
		return scoreboards.get(key);
	}

	public Board create(ScoreboardType type) {
		return create(type.getKey());
	}

	public Board create(String key) {
		Board board = new Board(manager.getNewScoreboard());
		scoreboards.put(key, board);
		return board;
	}

}
