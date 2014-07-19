package in.parapengu.spork.module.modules.filter.condition.conditions;

import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.filter.FilterContext;
import in.parapengu.spork.module.modules.filter.condition.ConditionParser;
import in.parapengu.spork.module.modules.filter.condition.FilterCondition;
import in.parapengu.spork.module.modules.filter.condition.FilterState;
import in.parapengu.spork.module.modules.team.TeamModule;

@ParserInfo({"team"})
public class TeamCondition implements FilterCondition {

	private TeamModule module;

	public TeamCondition(TeamModule module) {
		this.module = module;
	}

	@Override
	public FilterState matches(FilterContext context) {
		return FilterState.ALLOW;
	}

	public static class TeamParser extends ConditionParser<TeamCondition> {

		public TeamParser() {
			super(TeamCondition.class);
		}

		@Override
		public TeamCondition parse(ParsingContext context) {
			return null;
		}

	}

}
