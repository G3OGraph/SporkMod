package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.util.ParsingUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

@ParserInfo({"block", "point"})
public class BlockRegion extends RegionModule {

	private String x;
	private Double xD;
	private Integer xI;

	private String y;
	private Double yD;
	private Integer yI;

	private String z;
	private Double zD;
	private Integer zI;

	public BlockRegion(String name, String x, String y, String z) throws NumberFormatException {
		super(name);
		this.x = x;
		this.y = y;
		this.z = z;

		String parse = "none";
		if(x.equals("-oo") || x.equals("oo")) {
			xD = x.equals("-oo") ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			xI = x.equals("-oo") ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		if(y.equals("-oo") || y.equals("oo")) {
			yD = y.equals("-oo") ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			yI = y.equals("-oo") ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		if(z.equals("-oo") || z.equals("oo")) {
			zD = z.equals("-oo") ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			zI = z.equals("-oo") ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		if(xD == null) {
			parse = x.contains(".") ? "double" : "integer";
		} else if(yD == null) {
			parse = y.contains(".") ? "double" : "integer";
		} else if(zD == null) {
			parse = z.contains(".") ? "double" : "integer";
		}

		if(parse.equals("none")) {
			return;
		}

		if(xD == null) {
			if(parse.equals("double")) {
				xD = Double.parseDouble(x);
				xI = new Location(Bukkit.getWorlds().get(0), xD, 0, 0).getBlockX();
			} else {
				xI = Integer.parseInt(x);
				xD = new Location(Bukkit.getWorlds().get(0), xI, 0, 0).getX();
			}
		}

		if(yD == null) {
			if(parse.equals("double")) {
				yD = Double.parseDouble(y);
				yI = new Location(Bukkit.getWorlds().get(0), 0, yD, 0).getBlockY();
			} else {
				yI = Integer.parseInt(y);
				yD = new Location(Bukkit.getWorlds().get(0), 0, yI, 0).getY();
			}
		}

		if(zD == null) {
			if(parse.equals("double")) {
				zD = Double.parseDouble(z);
				zI = new Location(Bukkit.getWorlds().get(0), 0, zD, 0).getBlockZ();
			} else {
				zI = Integer.parseInt(z);
				zD = new Location(Bukkit.getWorlds().get(0), 0, zI, 0).getZ();
			}
		}
	}

	public BlockRegion(String name, double x, double y, double z) {
		this(name, x + "", y + "", z + "");
	}

	public BlockRegion(String name, int z, int y, int x) {
		this(name, x + "", y + "", z + "");
	}

	public BlockRegion(String name, Location location) {
		this(name, location.getX(), location.getY(), location.getZ());
	}

	public String getStringX() {
		return x;
	}

	public double getX() {
		return xD;
	}

	public int getBlockX() {
		return xI;
	}

	public String getStringY() {
		return y;
	}

	public double getY() {
		return yD;
	}

	public int getBlockY() {
		return yI;
	}

	public String getStringZ() {
		return z;
	}

	public double getZ() {
		return zD;
	}

	public int getBlockZ() {
		return zI;
	}

	public Location getLocation() {
		return new Location(Bukkit.getWorlds().get(0), xD, yD, zD);
	}

	@Override
	public boolean isInside(BlockRegion region) {
		return region.getBlockX() == getBlockX() && region.getBlockY() == getBlockY() && region.getBlockZ() == getBlockZ();
	}

	@Override
	public BlockRegion getRandom() {
		return this;
	}

	public static class BlockParser extends RegionParser<BlockRegion> {

		public BlockParser() {
			super(BlockRegion.class);
		}

		@Override
		public List<BlockRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");
			return Lists.newArrayList(ParsingUtil.parse(name, context.getElement(), context.getElement().getText()));
		}

	}

}
