package in.parapengu.spork.util;

import com.google.common.collect.Lists;
import in.parapengu.commons.utils.Config;
import in.parapengu.spork.Spork;

import java.io.File;
import java.util.List;

public class SporkConfig extends Config {

	public SporkConfig() {
		super(Spork.get(), new File(Spork.get().getDataFolder(), "config.yml"), "config.yml");
		reload();
	}

	public File getRotation() {
		String result = getString("rotation.file");
		if(result == null) {
			set("rotation.file", "rotation.txt");
			result = "rotation.txt";
		}

		return new File(result);
	}

	public File getRepository() {
		String result = getString("repository.path");
		if(result == null) {
			set("repository.path", "maps");
			result = "maps";
		}

		return new File(result);
	}

	public boolean isDebug() {
		List<String> possibles = Lists.newArrayList("true", "false", "0", "1");
		String result = getString("debug");
		if(result == null || !possibles.contains(result.toLowerCase())) {
			if(result != null && !possibles.contains(result.toLowerCase())) {
				Log.info("No suitable value was found for debug mode, automatically disabling...");
			}

			set("debug", "false");
			result = "false";
		}

		return result.equalsIgnoreCase("true") || result.equalsIgnoreCase("1");
	}

}
