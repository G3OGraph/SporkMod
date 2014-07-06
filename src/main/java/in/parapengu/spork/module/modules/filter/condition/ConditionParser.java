package in.parapengu.spork.module.modules.filter.condition;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;

import java.util.List;

public abstract class ConditionParser<C extends FilterCondition> {

	private Class<C> condition;

	protected ConditionParser(Class<C> condition) {
		this.condition = condition;
	}

	public Class<C> getCondition() {
		return condition;
	}

	public List<String> getNames() {
		return Lists.newArrayList(condition.getAnnotation(ParserInfo.class).value());
	}

	public abstract C parse(ParsingContext context) throws ModuleParsingException;

}
