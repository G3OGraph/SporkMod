package in.parapengu.spork.map;

import in.parapengu.spork.Spork;
import in.parapengu.spork.exception.map.MapLoadException;
import in.parapengu.spork.map.features.Contributor;
import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.ModuleCollection;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.BuilderContext;
import org.jdom2.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {

	protected File folder;
	protected Document document;
	protected String name;
	protected String version;
	protected String objective;
	protected List<Contributor> authors;
	protected List<Contributor> contributors;
	protected List<String> rules;
	protected ModuleCollection modules;

	public MapLoader(File folder, Document document, String name, String version, String objective, List<Contributor> authors, List<Contributor> contributors, List<String> rules) throws MapLoadException {
		this.folder = folder;
		this.document = document;
		this.name = name;
		this.version = version;
		this.objective = objective;
		this.authors = authors;
		this.contributors = contributors;
		this.rules = rules;
		this.modules = new ModuleCollection();
		this.modules.addAll(Spork.getFactory().build(new BuilderContext(BuildPhase.CREATE).register(document)));
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

	public List<String> getRules() {
		return rules;
	}

	public ModuleCollection getModules() {
		return modules;
	}

}
