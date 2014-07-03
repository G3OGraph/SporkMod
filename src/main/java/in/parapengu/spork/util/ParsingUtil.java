package in.parapengu.spork.util;

import org.bukkit.ChatColor;

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
