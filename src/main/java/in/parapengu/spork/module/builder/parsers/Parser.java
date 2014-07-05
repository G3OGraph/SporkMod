package in.parapengu.spork.module.builder.parsers;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.Module;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.util.Log;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class Parser<M extends Module, P extends ModuleParser> extends Builder<M> {

	private String child;
	private List<P> parsers;

	public Parser(String child, List<P> parsers, Class<M> module) {
		this.child = child;
		this.parsers = parsers;
		this.module = module;
	}

	@Override
	public List<M> list(BuilderContext context) throws ModuleLoadException {
		List<M> list = new ArrayList<>();

		Element root = context.getRoot().getChild(child);
		if(root == null) {
			return list;
		}

		List<Element> elements = root.getChildren();
		for(Element element : elements) {
			list.addAll(parse(element, context));
		}

		return list;
	}

	public List<M> parse(Element element, BuilderContext context) {
		String name = element.getName();
		List<M> regions = new ArrayList<>();

		for(P parser : parsers) {
			if(parser.getNames().contains(name)) {
				try {
					regions.addAll(Lists.newArrayList(parser.parse(new ParsingContext(element).register(context.getMap()))));
				} catch(ModuleParsingException ex) {
					Log.exception(ex);
				}
			}
		}

		return regions;
	}

}
