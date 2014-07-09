package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
import in.parapengu.commons.utils.OtherUtil;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.util.ParsingUtil;
import org.bukkit.Location;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.List;
import java.util.Map;
import java.util.Random;

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

	@Override
	public BlockRegion getRandom() {
		Random rndGen = new Random();
		int r = rndGen.nextInt((int) Math.floor(radius));
		int x = rndGen.nextInt(r);
		int z = (int) Math.sqrt(Math.pow(r,2) - Math.pow(x,2));
		if(rndGen.nextBoolean()) x *= -1;
		if(rndGen.nextBoolean()) z *= -1;

		int y = (int) OtherUtil.getRandom(base.getY(), base.getY() + height);

		x += base.getBlockX();
		y += base.getBlockY();
		z += base.getBlockZ();

		return new BlockRegion(null, x, y, z);
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
