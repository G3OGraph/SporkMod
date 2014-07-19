package in.parapengu.spork.util.config.groups;

public class GitHubConfig {

	private boolean enabled;
	private String user;
	private String password;

	public GitHubConfig(boolean enabled, String user, String password) {
		this.enabled = enabled;
		this.user = user;
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
