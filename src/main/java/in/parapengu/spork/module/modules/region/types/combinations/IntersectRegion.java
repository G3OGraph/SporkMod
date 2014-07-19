package in.parapengu.spork.module.modules.region.types.combinations;

import com.google.common.collect.Lists;
import in.parapengu.commons.utils.OtherUtil;
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

@ParserInfo({"intersect"})
public class IntersectRegion extends RegionModule {

	private List<RegionModule> regions;

	public IntersectRegion(String name, List<RegionModule> regions) {
		super(name);
		this.regions = regions;
	}

	public List<RegionModule> getRegions() {
		return regions;
	}

	@Override
	public boolean isInside(BlockRegion region) {
		List<RegionModule> multiple = new ArrayList<>();

		for(RegionModule module : regions) {
			if(module.isInside(region)) {
				multiple.add(module);
			}

			if(multiple.size() > 1) {
				return true;
			}
		}

		return false;
	}

	@Override
	public BlockRegion getRandom() {
		// TODO: Optimise this code

		BlockRegion result;
		do {
			result = OtherUtil.getRandom(regions).getRandom();
		} while(!isInside(result));
		return result;
	}

	public static class IntersectParser extends RegionParser<IntersectRegion> {

		public IntersectParser() {
			super(IntersectRegion.class);
		}

		@Override
		public List<IntersectRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");

			List<RegionModule> regions = new ArrayList<>();
			for(Element element : context.getElement().getChildren()) {
				regions.addAll(new RegionBuilder().parse(element, new BuilderContext(BuildPhase.ANY).register(context.getMap())));
			}

			if(regions.size() < 2) {
				throw new ModuleParsingException(IntersectRegion.class, "IntersectRegions require at least 2 regions");
			}

			return Lists.newArrayList(new IntersectRegion(name, regions));
		}

	}

}
