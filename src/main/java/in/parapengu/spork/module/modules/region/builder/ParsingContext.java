package in.parapengu.spork.module.modules.region.builder;

import in.parapengu.spork.map.SporkMap;
import org.jdom2.Element;

public class ParsingContext {

	private SporkMap map;
	private Element element;

	public ParsingContext(Element element) {
		this.element = element;
	}

	public SporkMap getMap() {
		return map;
	}

	public Element getElement() {
		return element;
	}

	public ParsingContext register(SporkMap map) {
		this.map = map;
		return this;
	}

}
