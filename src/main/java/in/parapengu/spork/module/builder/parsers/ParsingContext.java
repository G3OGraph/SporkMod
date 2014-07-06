package in.parapengu.spork.module.builder.parsers;

import in.parapengu.spork.map.SporkMap;
import in.parapengu.spork.module.modules.filter.condition.FilterCondition;
import in.parapengu.spork.module.modules.region.RegionModule;
import org.jdom2.Element;

import java.util.List;

public class ParsingContext {

	private SporkMap map;
	private Element element;
	private List<RegionModule> regions;
	private List<FilterCondition> conditions;

	public ParsingContext(Element element) {
		this.element = element;
	}

	public SporkMap getMap() {
		return map;
	}

	public Element getElement() {
		return element;
	}

	public List<RegionModule> getRegions() {
		return regions;
	}

	public ParsingContext addMap(SporkMap map) {
		this.map = map;
		return this;
	}

	public ParsingContext addRegions(List<RegionModule> regions) {
		this.regions = regions;
		return this;
	}

	public ParsingContext addConditions(List<FilterCondition> conditions) {
		this.conditions = conditions;
		return this;
	}

}
