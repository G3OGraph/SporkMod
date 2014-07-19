package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
import in.parapengu.commons.utils.OtherUtil;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.util.ParsingUtil;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.List;
import java.util.Map;

@ParserInfo({"sphere"})
public class SphereRegion extends RegionModule {

	private BlockRegion base;
	private double radius;

	public SphereRegion(String name, BlockRegion base, double radius) {
		super(name);
		this.base = base;
		this.radius = radius;
	}

	@Override
	public boolean isInside(BlockRegion region) {
		double distance = region.getLocation().distance(base.getLocation());
		return distance <= radius;
	}

	@Override
	public BlockRegion getRandom() {
		// TODO: Optimise this code

		BlockRegion region;
		do {
			int x = (int) (base.getBlockX() + Math.floor(OtherUtil.getRandom(0, radius)));
			int y = (int) (base.getBlockY() + Math.floor(OtherUtil.getRandom(0, radius)));
			int z = (int) (base.getBlockZ() + Math.floor(OtherUtil.getRandom(0, radius)));
			region = new BlockRegion(null, x, y, z);
		} while(!isInside(region));
		return region;
	}

	public static class SphereParser extends RegionParser<SphereRegion> {

		public SphereParser() {
			super(SphereRegion.class);
		}

		@Override
		public List<SphereRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");
			BlockRegion base = ParsingUtil.parse(name, context.getElement(), check(context.getElement().getAttributeValue("base")));
			Double radius = ParsingUtil.parse(Double.class, context.getElement().getAttributeValue("radius"));

			if(radius == null) {
				throw new ModuleParsingException(SphereRegion.class, "Invalid radius found in " + new XMLOutputter(Format.getPrettyFormat()).outputString(context.getElement()));
			}

			return Lists.newArrayList(new SphereRegion(name, base, radius));
		}

		public String check(String value) throws ModuleParsingException {
			Map<String, String> points = ParsingUtil.parse(value);
			if(!points.containsKey("y")) {
				throw new ModuleParsingException(RectangleRegion.class, "CylinderRegions require a Y coordinate value");
			}

			return value;
		}

	}

}
