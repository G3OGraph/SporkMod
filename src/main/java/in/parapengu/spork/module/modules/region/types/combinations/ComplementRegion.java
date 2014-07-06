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

@ParserInfo({"complement"})
public class ComplementRegion extends RegionModule {

	private RegionModule base;
	private List<RegionModule> regions;

	public ComplementRegion(String name, RegionModule base, List<RegionModule> regions) {
		super(name);
		this.base = base;
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

	public static class ComplementParser extends RegionParser<ComplementRegion> {

		public ComplementParser() {
			super(ComplementRegion.class);
		}

		@Override
		public List<ComplementRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");

			List<RegionModule> regions = new ArrayList<>();
			for(Element element : context.getElement().getChildren()) {
				regions.addAll(new RegionBuilder().parse(element, new BuilderContext(BuildPhase.ANY).register(context.getMap())));
			}

			if(regions.size() < 2) {
				throw new ModuleParsingException(ComplementRegion.class, "ComplementRegions require at least 2 regions");
			}

			RegionModule base = regions.get(0);
			regions.remove(base);
			return Lists.newArrayList(new ComplementRegion(name, base, regions));
		}

	}

}
