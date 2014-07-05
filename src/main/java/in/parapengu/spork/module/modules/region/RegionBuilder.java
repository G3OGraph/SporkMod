package in.parapengu.spork.module.modules.region;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.builder.BuilderInfo;
import in.parapengu.spork.module.modules.region.builder.ParsingContext;
import in.parapengu.spork.module.modules.region.builder.RegionParser;
import in.parapengu.spork.module.modules.region.types.BlockRegion.BlockParser;
import in.parapengu.spork.module.modules.region.types.CuboidRegion.CuboidParser;
import in.parapengu.spork.module.modules.region.types.RectangleRegion.RectangleParser;
import in.parapengu.spork.module.modules.region.types.CylinderRegion.CylinderParser;
import in.parapengu.spork.module.modules.region.types.CircleRegion.CircleParser;
import in.parapengu.spork.module.modules.region.types.SphereRegion.SphereParser;
import in.parapengu.spork.util.Log;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(BuildPhase.LOAD)
public class RegionBuilder extends Builder<RegionModule> {

	private static List<RegionParser> parsers;

	static {
		parsers = new ArrayList<>();
		parsers.add(new BlockParser());
		parsers.add(new CuboidParser());
		parsers.add(new RectangleParser());
		parsers.add(new CylinderParser());
		parsers.add(new CircleParser());
		parsers.add(new SphereParser());
	}

	public RegionBuilder() {
		this.module = RegionModule.class;
	}

	@Override
	public List<RegionModule> list(BuilderContext context) throws ModuleLoadException {
		List<RegionModule> list = new ArrayList<>();

		Element root = context.getRoot().getChild("regions");
		if(root == null) {
			return list;
		}

		List<Element> elements = root.getChildren();
		for(Element element : elements) {
			list.addAll(parse(element, context));
		}

		return list;
	}

	public static List<RegionModule> parse(Element element, BuilderContext context) {
		String name = element.getName();
		List<RegionModule> regions = new ArrayList<>();

		for(RegionParser parser : parsers) {
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
