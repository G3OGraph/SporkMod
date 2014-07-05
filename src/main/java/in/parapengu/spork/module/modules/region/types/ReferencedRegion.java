package in.parapengu.spork.module.modules.region.types;

import com.google.common.collect.Lists;
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

@ParserInfo({"rectangle"})
public class ReferencedRegion extends RegionModule {

	private RegionModule referenced;

	public ReferencedRegion(RegionModule referenced) {
		super(null);
		this.referenced = referenced;
	}

	public RegionModule getReferenced() {
		return referenced;
	}

	@Override
	public boolean isInside(BlockRegion region) {
		return referenced.isInside(region);
	}

	public static class ReferencedParser extends RegionParser<ReferencedRegion> {

		public ReferencedParser() {
			super(ReferencedRegion.class);
		}

		@Override
		public List<ReferencedRegion> parse(ParsingContext context) throws ModuleParsingException {
			String name = context.getElement().getAttributeValue("name");
			List<RegionModule> regions = context.getRegions();
			for(RegionModule region : regions) {
				if(region.getName() != null && region.getName().equals(name)) {
					return Lists.newArrayList(new ReferencedRegion(region));
				}
			}

			throw new ModuleParsingException(ReferencedRegion.class, "Could not find Region for " + new XMLOutputter(Format.getPrettyFormat()).outputString(context.getElement()));
		}

		public String compile(String value, boolean min) throws ModuleParsingException {
			Map<String, String> points = ParsingUtil.parse(value);
			if(points.containsKey("y")) {
				throw new ModuleParsingException(ReferencedRegion.class, "RectangleRegions can't have a Y coordinate value");
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
