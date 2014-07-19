package in.parapengu.spork.module.modules.team;

import in.parapengu.commons.utils.OtherUtil;
import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.builder.ModuleBuilder;
import in.parapengu.spork.module.modules.spawn.SpawnModule;
import in.parapengu.spork.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

@ModuleBuilder(TeamBuilder.class)
public class TeamModule extends Module {

	private List<Team> teams;
	private String name;
	private ChatColor color;
	private ChatColor overheadColor;
	private int max;
	private int maxOverfill;
	private boolean observer;

	private List<SpawnModule> spawns;

	public TeamModule(String name, ChatColor color, ChatColor overheadColor, int max, int maxOverfill, boolean observer) {
		this.name = name;
		this.color = color;
		this.overheadColor = overheadColor;
		this.max = max;
		this.maxOverfill = maxOverfill;
		this.observer = observer;
		this.spawns = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public ChatColor getColor() {
		return color;
	}

	public ChatColor getOverheadColor() {
		return overheadColor;
	}

	public int getMax() {
		return max;
	}

	public int getMaxOverfill() {
		return maxOverfill;
	}

	public boolean isObserver() {
		return observer;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public List<SpawnModule> getSpawns() {
		return spawns;
	}

	public SpawnModule getSpawn() {
		return OtherUtil.getRandom(spawns);
	}

	public void addSpawn(SpawnModule module) {
		spawns.add(module);
	}

	public boolean join(JoinContext context) {
		PlayerData player = context.getPlayer();
		player.setTeam(this);

		for(Team team : teams) {
			team.addPlayer(player.getPlayer());
		}

		return true;
	}

	@Override
	public TeamModule clone() throws CloneNotSupportedException {
		return new TeamModule(name, color, overheadColor, max, maxOverfill, observer);
	}

	public static TeamModule getObservers(List<TeamModule> teams) {
		for(TeamModule team : teams) {
			if(team.isObserver()) {
				return team;
			}
		}

		return null;
	}

	public static TeamModule search(List<TeamModule> teams, String search) {
		for(TeamModule team : teams) {
			if(team.getName().equalsIgnoreCase(search)) {
				return team;
			}
		}

		for(TeamModule team : teams) {
			if(team.getColor().name().replace("_", " ").equalsIgnoreCase(search)) {
				return team;
			}
		}

		for(TeamModule team : teams) {
			if(team.getName().toLowerCase().startsWith(search)) {
				return team;
			}
		}

		for(TeamModule team : teams) {
			if(team.getColor().name().replace("_", " ").toLowerCase().startsWith(search)) {
				return team;
			}
		}

		return null;
	}

}
