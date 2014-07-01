package in.parapengu.spork.module.modules.team;

import in.parapengu.spork.module.Module;
import org.bukkit.ChatColor;

public class TeamModule extends Module {

	private String name;
	private ChatColor color;

	public TeamModule(String name, ChatColor color) {
		this.name = name;
		this.color = color;
	}

	@Override
	public TeamModule clone() throws CloneNotSupportedException {
		return new TeamModule(name, color);
	}

}
