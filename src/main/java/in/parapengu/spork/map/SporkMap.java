package in.parapengu.spork.map;

import com.google.common.collect.Lists;
import in.parapengu.spork.Spork;
import in.parapengu.spork.exception.map.MapLoadException;
import in.parapengu.spork.map.features.Contributor;
import in.parapengu.spork.match.Match;
import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.ModuleCollection;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.modules.team.TeamModule;
import in.parapengu.spork.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jdom2.Document;

import java.io.File;
import java.util.List;

public class SporkMap {

	private static String MATCH_STYLE = "match-{id}";

	private MapLoader loader;

	private File folder;
	private Document document;
	private String name;
	private String version;
	private String objective;
	private List<Contributor> authors;
	private List<Contributor> contributors;
	private ModuleCollection modules;

	private World world;

	public SporkMap(MapLoader loader) {
		this.loader = loader;
		this.folder = loader.folder;
		this.document = loader.document;
		this.name = loader.name;
		this.version = loader.version;
		this.objective = loader.objective;
		this.authors = Lists.newArrayList(loader.authors);
		this.contributors = Lists.newArrayList(loader.contributors);
		this.modules = loader.getModules().clone();
	}

	public MapLoader getLoader() {
		return loader;
	}

	public File getFolder() {
		return folder;
	}

	public Document getDocument() {
		return document;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getObjective() {
		return objective;
	}

	public List<Contributor> getAuthors() {
		return authors;
	}

	public List<Contributor> getContributors() {
		return contributors;
	}

	public ModuleCollection getModules() {
		return modules;
	}

	public List<TeamModule> getTeams() {
		return modules.getModules(TeamModule.class);
	}

	public boolean load(Match match) throws MapLoadException {
		String name = MATCH_STYLE.replace("{id}", match.getId() + "");

		File destination = new File(Bukkit.getServer().getWorldContainer(), name);
		if(destination.exists()) {
			FileUtil.delete(destination);
		}

		boolean success = destination.mkdir();
		if(!success) {
			return false;
		}

		String[] copy = new String[]{"region true", "level.dat true", "data false"};
		for(String string : copy) {
			String[] split = string.split(" ");
			String file = split[0];
			boolean required = split[0].equalsIgnoreCase("true");

			File src = new File(folder, file);
			File dest = new File(destination, file);
			if(!src.exists() && required) {
				throw new MapLoadException(this, "Required file '" + file + "' does not exist");
			}
		}

		return true;
	}

}
