package in.parapengu.spork.module.modules.filter;

import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;
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
public class FilterBuilder extends Builder<FilterModule> {

	public FilterBuilder() {
		super(FilterModule.class);
	}

	@Override
	public List<? extends FilterModule> list(BuilderContext context) throws ModuleLoadException {
		return super.list(context);
	}

}
