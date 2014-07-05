package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.util.ParsingUtil;

import java.util.List;
import java.util.Map;

@ParserInfo({"cuboid"})
public class CuboidRegion extends RegionModule {

	private BlockRegion min;
	private BlockRegion max;

	public CuboidRegion(String name, BlockRegion a, BlockRegion b) {
		super(name);
		update(a, b);
	}

	public BlockRegion getMin() {
		return min;
	}

	public BlockRegion getMax() {
		return max;
	}
	
	private void update(BlockRegion a, BlockRegion b) {
		int minX = a.getBlockX(), maxX = a.getBlockX();
		int minY = a.getBlockY(), maxY = a.getBlockY();
		int minZ = a.getBlockZ(), maxZ = a.getBlockZ();
		
		if(b.getBlockX() > maxX) {
			maxX = b.getBlockX();
		} else {
			minX = b.getBlockX();
		}

		if(b.getBlockY() > maxY) {
			maxY = b.getBlockY();
		} else {
			minY = b.getBlockY();
		}

		if(b.getBlockZ() > maxZ) {
			maxZ = b.getBlockZ();
		} else {
			minZ = b.getBlockZ();
		}

		this.min = new BlockRegion(null, minX, minY, minZ);
		maxX--;
		maxY--;
		maxZ--;
		this.max = new BlockRegion(null, maxX, maxY, maxZ);
	}

	@Override
	public boolean isInside(BlockRegion region) {
		int minX = min.getBlockX(), maxX = max.getBlockX();
		int minY = min.getBlockY(), maxY = max.getBlockY();
		int minZ = min.getBlockZ(), maxZ = max.getBlockZ();

		boolean x = minX <= region.getBlockX() && region.getBlockX() <= maxX;
		boolean y = minY <= region.getBlockY() && region.getBlockY() <= maxY;
		boolean z = minZ <= region.getBlockZ() && region.getBlockZ() <= maxZ;
		return x && y && z;
	}

	public static class CuboidParser extends RegionParser<CuboidRegion> {

		public CuboidParser() {
			super(CuboidRegion.class);
		}

		@Override
		public List<CuboidRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");
			BlockRegion a = ParsingUtil.parse(name, context.getElement(), check(context.getElement().getAttributeValue("min")));
			BlockRegion b = ParsingUtil.parse(name, context.getElement(), check(context.getElement().getAttributeValue("max")));
			return Lists.newArrayList(new CuboidRegion(name, a, b));
		}

		public String check(String value) throws ModuleParsingException {
			Map<String, String> points = ParsingUtil.parse(value);
			if(!points.containsKey("y")) {
				throw new ModuleParsingException(RectangleRegion.class, "CuboidRegions require a Y coordinate value");
			}

			return value;
		}

	}

}
