package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.util.ParsingUtil;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.List;
import java.util.Map;

@ParserInfo({"circle"})
public class CircleRegion extends CylinderRegion {

	public CircleRegion(String name, BlockRegion base, double radius) {
		super(name, base, radius, Double.POSITIVE_INFINITY);
	}

	public static class CircleParser extends RegionParser<CircleRegion> {

		public CircleParser() {
			super(CircleRegion.class);
		}

		@Override
		public List<CircleRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");
			BlockRegion base;
			try {
				base = ParsingUtil.parse(name, context.getElement(), compile(context.getElement().getAttributeValue("center")));
			} catch(NullPointerException ex) {
				throw new ModuleParsingException(CircleRegion.class, "Points are null for " + new XMLOutputter(Format.getPrettyFormat()).outputString(context.getElement()));
			}
			Double radius = ParsingUtil.parse(Double.class, context.getElement().getAttributeValue("radius"));

			if(radius == null) {
				throw new ModuleParsingException(CylinderRegion.class, "Invalid radius found in " + new XMLOutputter(Format.getPrettyFormat()).outputString(context.getElement()));
			}

			return Lists.newArrayList(new CircleRegion(name, base, radius));
		}

		public String compile(String value) throws ModuleParsingException {
			Map<String, String> points = ParsingUtil.parse(value);
			if(points.containsKey("y")) {
				throw new ModuleParsingException(CircleRegion.class, "CircleRegions can't have a Y coordinate value");
			}

			points.put("y", "-oo");

			List<String> keys = Lists.newArrayList(points.keySet());
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < keys.size(); i++) {
				if(i > 0) {
					builder.append(",");
				}
				builder.append(points.get(keys.get(i)));
			}

			return builder.toString();
		}

	}

}
