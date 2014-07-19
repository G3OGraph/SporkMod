package in.parapengu.spork.module.builder;

import in.parapengu.spork.map.SporkMap;
import in.parapengu.spork.match.Match;
import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.ModuleCollection;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class BuilderContext {

	private BuildPhase phase;
	private Document document;
	private SporkMap map;
	private Match match;
	private ModuleCollection modules;

	public BuilderContext(BuildPhase phase) {
		this.phase = phase;
		this.modules = new ModuleCollection();
	}

	public BuildPhase getPhase() {
		return phase;
	}

	public Document getDocument() {
		return document;
	}

	public Element getRoot() {
		return document.getRootElement();
	}

	public SporkMap getMap() {
		return map;
	}

	public Match getMatch() {
		return match;
	}

	public ModuleCollection getModules() {
		return modules;
	}

	public BuilderContext register(Document document) {
		this.document = document;
		return this;
	}

	public BuilderContext register(SporkMap map) {
		this.map = map;
		return this;
	}

	public BuilderContext register(Match match) {
		this.match = match;
		return this;
	}

	public BuilderContext register(Module module) {
		this.modules.add(module);
		return this;
	}

	public BuilderContext register(Module... modules) {
		for(Module module : modules) {
			register(module);
		}

		return this;
	}

	public BuilderContext register(List<Module> modules) {
		this.modules.addAll(modules);
		return this;
	}

}
