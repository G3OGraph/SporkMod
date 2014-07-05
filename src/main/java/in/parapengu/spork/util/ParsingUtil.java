package in.parapengu.spork.util;

import org.bukkit.ChatColor;

import java.util.HashMap;
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
		return points;
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
