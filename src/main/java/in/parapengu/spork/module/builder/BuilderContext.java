package in.parapengu.spork.module.builder;

import in.parapengu.spork.map.SporkMap;
import in.parapengu.spork.match.Match;
import org.jdom2.Document;
import org.jdom2.Element;

public class BuilderContext {

	private BuildPhase phase;
	private Document document;
	private SporkMap map;
	private Match match;

	public BuilderContext(BuildPhase phase) {
		this.phase = phase;
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

}
