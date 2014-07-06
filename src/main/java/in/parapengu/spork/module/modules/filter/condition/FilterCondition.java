package in.parapengu.spork.module.modules.filter.condition;

import in.parapengu.spork.module.modules.filter.FilterContext;

public interface FilterCondition {

	public FilterState matches(FilterContext context);

}
