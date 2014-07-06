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

@ParserInfo({"union"})
public class UnionRegion extends RegionModule {

	private List<RegionModule> regions;

	public UnionRegion(String name, List<RegionModule> regions) {
		super(name);
		this.regions = regions;
	}

	public List<RegionModule> getRegions() {
		return regions;
	}

	@Override
	public boolean isInside(BlockRegion region) {
		for(RegionModule module : regions) {
			if(module.isInside(region)) {
				return true;
			}
		}

		return false;
	}

	public static class UnionParser extends RegionParser<UnionRegion> {

		public UnionParser() {
			super(UnionRegion.class);
		}

		@Override
		public List<UnionRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");

			List<RegionModule> regions = new ArrayList<>();
			for(Element element : context.getElement().getChildren()) {
				regions.addAll(new RegionBuilder().parse(element, new BuilderContext(BuildPhase.ANY).register(context.getMap())));
			}

			if(regions.size() < 1) {
				throw new ModuleParsingException(UnionRegion.class, "UnionRegions require at least 1 region");
			}

			return Lists.newArrayList(new UnionRegion(name, regions));
		}

	}

}
