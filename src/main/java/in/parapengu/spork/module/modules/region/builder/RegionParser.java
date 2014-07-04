package in.parapengu.spork.module.modules.region.builder;

import com.google.common.collect.Lists;
import in.parapengu.spork.module.modules.region.RegionModule;

import java.util.List;

public abstract class RegionParser<R extends RegionModule> {

	private Class<R> region;

	protected RegionParser(Class<R> region) {
		this.region = region;
	}

	public Class<R> getRegion() {
		return region;
	}

	public List<String> getNames() {
		return Lists.newArrayList(region.getAnnotation(ParserInfo.class).value());
	}

	public abstract List<R> parse(ParsingContext context);

}
