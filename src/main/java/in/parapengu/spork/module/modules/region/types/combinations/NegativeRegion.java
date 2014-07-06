package in.parapengu.spork.module.modules.region.types.combinations;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionBuilder;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.module.modules.region.types.BlockRegion;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@ParserInfo({"negative"})
public class NegativeRegion extends UnionRegion {

	public NegativeRegion(String name, List<RegionModule> regions) {
		super(name, regions);
	}

	@Override
	public boolean isInside(BlockRegion region) {
		return !super.isInside(region);
	}

	public static class NegativeParser extends RegionParser<NegativeRegion> {

		public NegativeParser() {
			super(NegativeRegion.class);
		}

		@Override
		public List<NegativeRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");

			List<RegionModule> regions = new ArrayList<>();
			for(Element element : context.getElement().getChildren()) {
				regions.addAll(new RegionBuilder().parse(element, new BuilderContext(BuildPhase.ANY).register(context.getMap())));
			}

			if(regions.size() < 1) {
				throw new ModuleParsingException(NegativeRegion.class, "NegativeRegions require at least 1 region");
			}

			return Lists.newArrayList(new NegativeRegion(name, regions));
		}

	}

}
