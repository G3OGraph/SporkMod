package in.parapengu.spork.module.modules.filter;

import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.BuilderInfo;
import in.parapengu.spork.module.builder.parsers.Parser;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.module.modules.region.types.BlockRegion.BlockParser;
import in.parapengu.spork.module.modules.region.types.CircleRegion.CircleParser;
import in.parapengu.spork.module.modules.region.types.CuboidRegion.CuboidParser;
import in.parapengu.spork.module.modules.region.types.CylinderRegion.CylinderParser;
import in.parapengu.spork.module.modules.region.types.RectangleRegion.RectangleParser;
import in.parapengu.spork.module.modules.region.types.ReferencedRegion.ReferencedParser;
import in.parapengu.spork.module.modules.region.types.SphereRegion.SphereParser;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(BuildPhase.LOAD)
public class FilterBuilder extends Parser<FilterModule, FilterParser> {

	private static List<FilterParser> parsers;

	static {
		parsers = new ArrayList<>();
	}

	public FilterBuilder() {
		super("filters", parsers, FilterModule.class);
	}

}
