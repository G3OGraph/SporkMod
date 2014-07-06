package in.parapengu.spork;

import in.parapengu.commons.utils.OtherUtil;
import in.parapengu.commons.utils.countdown.Countdown;
import in.parapengu.spork.exception.module.ModuleBuildException;
import in.parapengu.spork.listeners.BlockListener;
import in.parapengu.spork.listeners.ConnectionListener;
import in.parapengu.spork.map.MapFactory;
import in.parapengu.spork.module.ModuleFactory;
import in.parapengu.spork.module.ModuleRegistration;
import in.parapengu.spork.module.modules.filter.FilterModule;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.modules.team.TeamModule;
import in.parapengu.spork.rotation.Rotation;
import in.parapengu.spork.rotation.RotationSlot;
import in.parapengu.spork.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Spork extends JavaPlugin {

	private static Spork instance;
	private static Rotation rotation;

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
		registration.register(RegionModule.class);
		registration.register(FilterModule.class);

		try {
			factory = new ModuleFactory(registration);
		} catch(ModuleBuildException ex) {
			Log.exception(ex);
		}

		getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
		getServer().getPluginManager().registerEvents(new BlockListener(), this);

		maps = new MapFactory();
		maps.load(new File("maps"));

		/*
		new Countdown(this, ChatColor.AQUA + "Countdown ends in " + ChatColor.RED + "{TIMING}", 60 * 60 * 24 * 7 * 52) {

			@Override
			public boolean display() {
				return true;
			}

			@Override
			public void complete() {
				Bukkit.broadcastMessage(ChatColor.AQUA + "Countdown has ended...");
			}

		}.start();
		*/
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public static Spork get() {
		return instance;
	}

	public static Rotation getRotation() {
		return rotation;
	}

	public static RotationSlot getSlot() {
		return rotation.getSlots().get(rotation.getIndex());
	}

	public static RotationSlot getNext() {
		if(!hasNext()) {
			return null;
		}

		return rotation.getSlots().get(rotation.getIndex() + 1);
	}

	public static boolean hasNext() {
		try {
			rotation.getSlots().get(rotation.getIndex() + 1);
			return true;
		} catch(IndexOutOfBoundsException ex) {
			return false;
		}
	}

	public static ModuleFactory getFactory() {
		return instance.factory;
	}

	public static MapFactory getMaps() {
		return instance.maps;
	}

}
