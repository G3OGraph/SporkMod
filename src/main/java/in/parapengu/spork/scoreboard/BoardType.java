package in.parapengu.spork.scoreboard;

import in.parapengu.spork.scoreboard.boards.Board;
import in.parapengu.spork.scoreboard.boards.SidebarBoard;
import in.parapengu.spork.util.Log;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Constructor;

public enum BoardType {

	SIDEBAR("sidebar", SidebarBoard.class),
	OTHER("other", Board.class);

	private String key;
	private Class<? extends Board> type;

	BoardType(String key, Class<? extends Board> type) {
		this.key = key;
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public Class<? extends Board> getType() {
		return type;
	}

	public static <T extends Board> T getBoard(Scoreboard scoreboard, Class<T> type) {
		try {
			Constructor<?> constructor = type.getDeclaredConstructor(Scoreboard.class);
			constructor.setAccessible(true);
			return (T) constructor.newInstance(scoreboard);
		} catch(Exception ex) {
			Log.exception(ex);
		}

		return (T) new Board(scoreboard);
	}

	public static Board getBoard(String name, Scoreboard scoreboard) {
		BoardType type = OTHER;
		for(BoardType board : values()) {
			if(board.getKey().equals(name)) {
				type = board;
				break;
			}
		}

		return getBoard(scoreboard, type.getType());
	}

}
