package in.parapengu.spork.util;

import com.google.common.collect.Lists;
import in.parapengu.commons.utils.OtherUtil;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.modules.region.types.BlockRegion;
import org.bukkit.ChatColor;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsingUtil {

	public static <T> T parse(Class<T> type, String string) {
		T result = null;

		switch(type.getSimpleName()) {
			case "ChatColor":
				result = (T) getChatColor(string);
				break;
			case "Integer":
				result = (T) getInteger(string);
				break;
			case "int":
				result = (T) getInteger(string);
				break;
		}

		return result;
	}

	public static Map<String, String> parse(String string) {
		try {
			return parse(string, "x", "y", "z");
		} catch(ModuleParsingException ex) {
			Log.exception(ex);
			return null;
		}
	}

	public static Map<String, String> parse(String string, String... invalid) throws ModuleParsingException {
		if(string == null) {
			return null;
		}

		String[] split = string.split(",");
		if(split.length > 3 || split.length < 2) {
			return null;
		}

		Map<String, String> points = new HashMap<>();
		points.put("x", split[0]);
		if(split.length == 3) {
			points.put("y", split[1]);
		}
		points.put("z", split[split.length - 1]);

		if(invalid == null || invalid.length == 0) {
			invalid = new String[]{"x", "y", "z"};
		}

		List<String> keys = Lists.newArrayList(points.keySet());
		for(String key : invalid) {
			keys.remove(key);
		}

		if(keys.size() > 0) {
			throw new ModuleParsingException(BlockRegion.class, string + " should only contain " + OtherUtil.listToEnglishCompound(Lists.newArrayList(invalid)));
		}
		return points;
	}

	public static BlockRegion parse(String name, Element element, String string, String... invalid) throws ModuleParsingException {
		Map<String, String> points = parse(string, invalid);
		if(points == null) {
			String message;
			if(string == null) {
				message = "No points were specified in " + new XMLOutputter(Format.getPrettyFormat()).outputString(element);
			} else {
				message = "Invalid points specified in \"" + string + "\"";
			}

			throw new ModuleParsingException(BlockRegion.class, message);
		}

		BlockRegion region;
		try {
			region = new BlockRegion(name, points.get("x"), points.containsKey("y") ? points.get("y") : "oo", points.get("z"));
		} catch(NumberFormatException ex) {
			throw new ModuleParsingException(BlockRegion.class, ex);
		}

		return region;
	}

	public static ChatColor getChatColor(String string) {
		if(string == null) {
			return null;
		}

		string = string.replace(" ", "_").toUpperCase();
		for(ChatColor color : ChatColor.values()) {
			if(color.name().equalsIgnoreCase(string)) {
				return color;
			}
		}

		return null;
	}

	public static Integer getInteger(String string) {
		if(string == null) {
			return null;
		}

		try {
			return Integer.parseInt(string);
		} catch(NumberFormatException ex) {
			return null;
		}
	}

}
