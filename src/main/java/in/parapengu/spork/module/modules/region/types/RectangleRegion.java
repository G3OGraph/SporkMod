package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.parsers.ParserInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionParser;
import in.parapengu.spork.util.ParsingUtil;

import java.util.List;
import java.util.Map;

@ParserInfo({"rectangle"})
public class RectangleRegion extends CuboidRegion {

	public RectangleRegion(String name, BlockRegion a, BlockRegion b) {
		super(name, a, b);
	}

	public static class RectangleParser extends RegionParser<RectangleRegion> {

		public RectangleParser() {
			super(RectangleRegion.class);
		}

		@Override
		public List<RectangleRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");
			BlockRegion a = ParsingUtil.parse(name, context.getElement(), compile(context.getElement().getAttributeValue("min"), true));
			BlockRegion b = ParsingUtil.parse(name, context.getElement(), compile(context.getElement().getAttributeValue("max"), false));
			return Lists.newArrayList(new RectangleRegion(name, a, b));
		}

		public String compile(String value, boolean min) throws ModuleParsingException {
			Map<String, String> points = ParsingUtil.parse(value);
			if(points.containsKey("y")) {
				throw new ModuleParsingException(RectangleRegion.class, "RectangleRegions can't have a Y coordinate value");
			}

			points.put("y", min ? "-oo" : "oo");

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
