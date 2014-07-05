package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.modules.region.builder.ParserInfo;
import in.parapengu.spork.module.modules.region.builder.ParsingContext;
import in.parapengu.spork.module.modules.region.builder.RegionParser;
import in.parapengu.spork.util.ParsingUtil;
import org.bukkit.Location;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.List;
import java.util.Map;

@ParserInfo({"cylinder"})
public class CylinderRegion extends RegionModule {

	private BlockRegion base;
	private double radius;
	private double height;

	public CylinderRegion(String name, BlockRegion base, double radius, double height) {
		super(name);
		this.base = base;
		this.radius = radius;
		this.height = height;
	}

	@Override
	public boolean isInside(BlockRegion region) {
		double min = base.getY(), max = base.getY() + height;
		if(min > region.getY() || region.getY() > max) {
			return false;
		}

		Location location = region.getLocation();
		location.setY(region.getY());

		double distance = location.distance(base.getLocation());
		return distance <= radius;
	}

	public static class CylinderParser extends RegionParser<CylinderRegion> {

		public CylinderParser() {
			super(CylinderRegion.class);
		}

		@Override
		public List<CylinderRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");
			BlockRegion base = ParsingUtil.parse(name, context.getElement(), check(context.getElement().getAttributeValue("base")));
			Double radius = ParsingUtil.parse(Double.class, context.getElement().getAttributeValue("radius"));
			Double height = ParsingUtil.parse(Double.class, context.getElement().getAttributeValue("height"));

			if(radius == null) {
				throw new ModuleParsingException(CylinderRegion.class, "Invalid radius found in " + new XMLOutputter(Format.getPrettyFormat()).outputString(context.getElement()));
			}

			if(height == null) {
				throw new ModuleParsingException(CylinderRegion.class, "Invalid height found in " + new XMLOutputter(Format.getPrettyFormat()).outputString(context.getElement()));
			}

			return Lists.newArrayList(new CylinderRegion(name, base, radius, height));
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
