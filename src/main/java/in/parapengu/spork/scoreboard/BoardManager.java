package in.parapengu.spork.scoreboard;

import com.google.common.collect.Lists;
import in.parapengu.spork.map.SporkMap;
import in.parapengu.spork.module.modules.team.TeamModule;
import in.parapengu.spork.scoreboard.boards.Board;
import in.parapengu.spork.scoreboard.entry.BoardEntry;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public List<Board> getBoards() {
		return Lists.newArrayList(scoreboards.values());
	}

	public Board getBoard(BoardType type) {
		return getBoard(type.getKey());
	}

	public Board getBoard(String key) {
		return scoreboards.get(key);
	}

	public Board create(BoardType type) {
		return create(type.getKey());
	}

	public Board create(String key) {
		Board board = new Board(manager.getNewScoreboard());
		scoreboards.put(key, board);
		return board;
	}

	public List<Team> getTeams(TeamModule team) {
		List<Team> teams = new ArrayList<>();
		for(Board board : getBoards()) {
			teams.add(board.getTeam(team));
		}
		return teams;
	}

	public void add(BoardEntry entry) {
		entry.register(this);
	}

	public void remove(BoardEntry entry) {
		for(Entry<String, Board> en : scoreboards.entrySet()) {
			en.getValue().remove(entry);
		}
	}

	public void refresh() {
		for(Board board : getBoards()) {
			board.refresh();
		}
	}

}
