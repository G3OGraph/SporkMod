package in.parapengu.spork;

import in.parapengu.spork.exception.module.ModuleBuildException;
import in.parapengu.spork.listeners.ConnectionListener;
import in.parapengu.spork.map.MapFactory;
import in.parapengu.spork.module.ModuleFactory;
import in.parapengu.spork.module.ModuleRegistration;
import in.parapengu.spork.module.modules.team.TeamModule;
import in.parapengu.spork.util.Log;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Spork extends JavaPlugin {

	private static Spork instance;

	private ModuleFactory factory;
	private MapFactory maps;

	@Override
	public void onLoad() {
		instance = this;
		Log.setDebug(true);
	}

	@Override
	public void onEnable() {
		ModuleRegistration registration = new ModuleRegistration();
		registration.register(TeamModule.class);

		try {
			factory = new ModuleFactory(registration);
		} catch(ModuleBuildException ex) {
			Log.exception(ex);
		}

		getServer().getPluginManager().registerEvents(new ConnectionListener(), this);

		maps = new MapFactory();
		maps.load(new File("maps"));
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public static Spork get() {
		return instance;
	}

	public static ModuleFactory getFactory() {
		return instance.factory;
	}

	public static MapFactory getMaps() {
		return instance.maps;
	}

}
