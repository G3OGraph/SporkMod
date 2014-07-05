package in.parapengu.spork.module.builder.parsers;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.Module;

import java.util.List;

public abstract class ModuleParser<M extends Module> {

	private Class<M> module;

	protected ModuleParser(Class<M> module) {
		this.module = module;
	}

	public Class<M> getModule() {
		return module;
	}

	public List<String> getNames() {
		return Lists.newArrayList(module.getAnnotation(ParserInfo.class).value());
	}

	public abstract List<M> parse(ParsingContext context) throws ModuleParsingException;

}
