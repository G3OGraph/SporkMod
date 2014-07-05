package in.parapengu.spork.module.modules.filter;

import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.parsers.ModuleParser;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionModule;

import java.util.List;

public abstract class FilterParser<R extends RegionModule> extends ModuleParser<R> {

	protected FilterParser(Class<R> region) {
		super(region);
	}

	public Class<R> getRegion() {
		return getModule();
	}

	public abstract List<R> parse(ParsingContext context) throws ModuleParsingException;

}
