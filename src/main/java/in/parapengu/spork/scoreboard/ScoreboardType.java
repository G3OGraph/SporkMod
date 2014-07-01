package in.parapengu.spork.scoreboard;

public enum ScoreboardType {

	NO_SIDEBAR("no-sidebar"),
	SIDEBAR("sidebar");

	private String key;

	ScoreboardType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
