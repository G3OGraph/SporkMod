package in.parapengu.spork.module.modules.filter;

import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.builder.ModuleBuilder;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.modules.filter.condition.FilterCondition;
import in.parapengu.spork.module.modules.filter.condition.FilterState;
import in.parapengu.spork.module.modules.region.RegionModule;

import java.util.List;

@ModuleBuilder(FilterBuilder.class)
public class FilterModule extends Module {

	private String name;
	private FilterCondition condition;

	public FilterModule(String name, FilterCondition condition) {
		this.name = name;
		this.condition = condition;
	}

	public String getName() {
		return name;
	}

	public FilterCondition getCondition() {
		return condition;
	}

	public boolean matches(FilterContext context) {
		return condition.matches(context).isAllowed();
	}

}
