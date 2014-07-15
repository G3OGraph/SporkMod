package in.parapengu.spork.util.config;

import com.google.common.collect.Lists;
import in.parapengu.commons.utils.Config;
import in.parapengu.spork.Spork;
import in.parapengu.spork.util.Log;
import in.parapengu.spork.util.config.groups.GitHubConfig;

import java.io.File;
import java.util.List;

public class SporkConfig extends Config {

	private static List<String> boolValues = Lists.newArrayList("true", "false", "0", "1");

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
		String result = getValue("debug.enabled", "false", "No suitable value was found for Debug mode, automatically disabling...", boolValues);
		return result.equalsIgnoreCase("true") || result.equalsIgnoreCase("1");
	}

	public boolean hasGithub() {
		String result = getValue("github.enabled", "false", "No suitable value was found for GitHub mode, automatically disabling...", boolValues);
		return result.equalsIgnoreCase("true") || result.equalsIgnoreCase("1");
	}

	public String getValue(String path, String def) {
		return getValue(path, def, null);
	}

	public String getValue(String path, String def, List<String> suitable) {
		return getValue(path, def, null, suitable);
	}

	public String getValue(String path, String def, String message, String... suitable) {
		return getValue(path, def, message, Lists.newArrayList(suitable));
	}

	public String getValue(String path, String def, String message, List<String> suitable) {
		String result = getString(path);
		if(result == null || (suitable.size() > 0 && !suitable.contains(result.toLowerCase()))) {
			if(message != null) {
				Log.info(message);
			}

			set(path, def);
			save();
			result = def;
		}

		return result;
	}

	public GitHubConfig getGitHub() {
		return new GitHubConfig(hasGithub(), getValue("github.username", "user"), getValue("github.password", "pass"));
	}

}
