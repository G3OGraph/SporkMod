package in.parapengu.spork.module.modules.filter;

import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.builder.BuilderInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.filter.condition.ConditionParser;
import in.parapengu.spork.module.modules.filter.condition.FilterCondition;
import in.parapengu.spork.module.modules.filter.condition.conditions.TeamCondition.TeamParser;
import in.parapengu.spork.util.Log;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(BuildPhase.LOAD)
public class FilterBuilder extends Builder<FilterModule> {

	private static List<ConditionParser> parsers;

	static {
		parsers = new ArrayList<>();
		parsers.add(new TeamParser());
	}

	public FilterBuilder() {
		super(FilterModule.class);
	}

	@Override
	public List<FilterModule> list(BuilderContext context) throws ModuleLoadException {
		List<FilterModule> list = new ArrayList<>();

		Element root = context.getRoot().getChild("filters");
		if(root == null) {
			return list;
		}

		List<Element> elements = root.getChildren("filter");
		for(Element element : elements) {
			list.add(parse(element, context));
		}

		return list;
	}

	public FilterModule parse(Element element, BuilderContext context) throws ModuleLoadException {
		String name = element.getAttributeValue("name");
		FilterCondition condition = conditions(element, context);

		if(condition == null) {
			throw new ModuleLoadException(FilterModule.class, "All Filters require at least 1 condition");
		}

		return new FilterModule(name, condition);
	}

	public FilterCondition conditions(Element element, BuilderContext context) {
		String name = element.getName();

		for(ConditionParser parser : parsers) {
			if(parser.getNames().contains(name)) {
				try {
					return parser.parse(new ParsingContext(element).addMap(context.getMap()));
				} catch(ModuleParsingException ex) {
					Log.exception(ex);
				}
			}
		}

		return null;
	}

}
