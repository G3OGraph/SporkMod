package io.sporkpgm.map;

import io.sporkpgm.ListenerHandler;
import io.sporkpgm.map.generator.NullChunkGenerator;
import io.sporkpgm.module.Module;
import io.sporkpgm.module.ObjectiveModule;
import io.sporkpgm.module.modules.filter.FilterCollection;
import io.sporkpgm.match.Match;
import io.sporkpgm.module.ModuleCollection;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.modules.info.Contributor;
import io.sporkpgm.module.modules.kits.KitModule;
import io.sporkpgm.module.modules.team.TeamCollection;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.module.modules.region.RegionCollection;
import io.sporkpgm.scoreboard.DefaultScoreboard;
import io.sporkpgm.scoreboard.ScoreboardHandler;
import io.sporkpgm.scoreboard.exceptions.IllegalScoreboardException;
import io.sporkpgm.scoreboard.objective.ObjectiveScoreboard;
import io.sporkpgm.user.User;
import io.sporkpgm.util.ClassUtils;
import io.sporkpgm.util.FileUtil;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.SporkConfig.Settings;
import io.sporkpgm.win.WinConditionSet;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SporkMap {

	private SporkLoader loader;
	private ModuleCollection modules;

	private RegionCollection regions;
	private FilterCollection filters;
	private TeamCollection teams;
	private ScoreboardHandler scoreboard;

	private World world;
	private boolean ended;
	private TeamModule winner;
	private Map<TeamModule, WinConditionSet> conditions;

	public SporkMap(SporkLoader loader) {
		this.loader = loader;
		this.scoreboard = new ScoreboardHandler(this);
		this.modules = loader.getModules().clone(this);
		this.modules.add(new BuilderContext(this, loader, loader.getDocument()));
		this.regions = new RegionCollection(this);
		this.filters = new FilterCollection(this);

		try {
			DefaultScoreboard board = this.scoreboard.get(DefaultScoreboard.class);
			this.scoreboard.setMain(board);
		} catch(IllegalScoreboardException e) {
			Log.exception(e);
		}

		this.scoreboard.register(teams.getObservers());
		for(TeamModule team : teams.getTeams()) {
			this.scoreboard.register(team);
		}

		if(modules.getModules(ObjectiveModule.class).size() > 0) {
			try {
				ObjectiveScoreboard board = this.scoreboard.get(ObjectiveScoreboard.class);
				board.setup();
				this.scoreboard.setMain(board);
			} catch(IllegalScoreboardException e) {
				Log.exception(e);
			}
		}
	}

	public SporkLoader getLoader() {
		return loader;
	}

	public ModuleCollection getModules() {
		return modules;
	}

	public String getName() {
		return loader.getName();
	}

	public String getObjective() {
		return loader.getObjective();
	}

	public List<Contributor> getContributors() {
		return loader.getContributors();
	}

	public List<Contributor> getAuthors() {
		return loader.getAuthors();
	}

	public List<String> getRules() {
		return loader.getRules();
	}

	public String getVersion() {
		return loader.getVersion();
	}

	public RegionCollection getRegions() {
		return regions;
	}

	public FilterCollection getFilters() {
		return filters;
	}

	public TeamCollection getTeams() {
		if(teams == null) {
			this.teams = new TeamCollection(this, modules.getModules(TeamModule.class));
		}

		return teams;
	}

	public ScoreboardHandler getScoreboard() {
		return scoreboard;
	}

	public List<KitModule> getKits() {
		return modules.getModules(KitModule.class);
	}

	public World getWorld() {
		return world;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;

		if(this.winner == null) {
			this.winner = getTeams().getObservers();
		}
	}

	public boolean hasEnded() {
		return ended;
	}

	public void checkEnded() {
		if(!ended && getWinner() != null && !getWinner().isObservers()) {
			setEnded(true);
		}
	}

	public TeamModule getWinner() {
		for(TeamModule module : getConditions().keySet()) {
			WinConditionSet set = conditions.get(module);
			if(set.isCompleted()) {
				return module;
			}
		}

		return getTeams().getObservers();
	}

	public void setWinner(TeamModule winner) {
		this.winner = winner;
	}

	public Map<TeamModule, WinConditionSet> getConditions() {
		if(conditions == null) {
			this.conditions = new HashMap<>();
			for(TeamModule team : teams.getTeams()) {
				this.conditions.put(team, new WinConditionSet());
			}
		}

		return conditions;
	}

	public WinConditionSet getConditionSet(TeamModule team) {
		return getConditions().get(team);
	}

	public void start() {
		for(Module module : modules.getModules()) {
			module.start();
		}
	}

	public void stop() {
		for(Module module : modules.getModules()) {
			module.stop();
		}
	}

	public boolean load(Match match) {
		this.modules.add(new BuilderContext(this, loader, loader.getDocument(), match));

		String name = Settings.prefix() + match.getID();
		File dest = new File(Bukkit.getWorldContainer(), name);
		loader.copy(dest);

		try {
			WorldCreator creator = new WorldCreator(name);
			creator.generator(new NullChunkGenerator());
			this.world = creator.createWorld();
		} catch(Exception e) {
			return false;
		}

		Log.info("World for " + toString() + " is " + world.getName());
		return true;
	}

	public boolean unload(Match match) {
		for(Module module : modules.getModules()) {
			if(module instanceof Listener) {
				ListenerHandler.unregisterListener((Listener) module);
			}
		}

		this.world = null;
		String name = Settings.prefix() + match.getID();
		boolean success = Bukkit.unloadWorld(name, false);
		if(!success) {
			return false;
		}

		File dest = new File(Bukkit.getWorldContainer(), name);
		FileUtil.delete(dest);
		return true;
	}

	public List<User> getPlayers() {
		List<User> users = new ArrayList<>();
		for(TeamModule team : teams.getTeams()) {
			users.addAll(team.getPlayers());
		}
		return users;
	}

}
