package io.sporkpgm.util;

import io.sporkpgm.Spork;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SporkConfig {

	public static class Maps {

		public static List<File> getFiles() {
			FileConfiguration config = Spork.get().getConfig();
			List<File> files = new ArrayList<>();

			String pre = "settings.folders";
			Set<String> folders = config.getConfigurationSection(pre).getKeys(false);
			if(folders != null) {
				for(String folder : folders) {
					String fetch = pre + "." + folder + ".path";
					String path = config.getString(fetch);
					if(path != null) {
						File file = new File(path);

						if(file.exists()) {
							files.add(file);
						} else {
							Log.info("Could not find a folder specified in the config (" + path + " @ " + fetch + ")");
						}
					} else {
						Log.info("Failed to specify path in Config (" + fetch + ")");
					}
				}
			}

			return files;
		}

	}

}
