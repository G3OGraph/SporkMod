package io.sporkpgm;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import io.sporkpgm.commands.MapCommands;
import io.sporkpgm.commands.MatchCommands;
import io.sporkpgm.commands.MiscCommands;
import io.sporkpgm.commands.RotationCommands;
import io.sporkpgm.commands.TeamCommands;
import io.sporkpgm.listeners.*;
import io.sporkpgm.map.SporkFactory;
import io.sporkpgm.module.builder.BuilderFactory;
import io.sporkpgm.module.modules.core.CoreModule;
import io.sporkpgm.module.modules.damage.DisableDamageBuilder;
import io.sporkpgm.module.modules.damage.DisableDamageModule;
import io.sporkpgm.module.modules.destroyable.DestroyableModule;
import io.sporkpgm.module.modules.filter.Filter;
import io.sporkpgm.module.modules.friendlyfire.FriendlyFireModule;
import io.sporkpgm.module.modules.info.InfoModule;
import io.sporkpgm.module.modules.itemremove.ItemRemoveModule;
import io.sporkpgm.module.modules.kits.KitModule;
import io.sporkpgm.module.modules.maxheight.MaxHeightModule;
import io.sporkpgm.module.modules.mob.MobModule;
import io.sporkpgm.module.modules.region.Region;
import io.sporkpgm.module.modules.spawn.SpawnModule;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.module.modules.victory.VictoryModule;
import io.sporkpgm.rotation.Rotation;
import io.sporkpgm.util.Config;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.SporkConfig.Maps;
import io.sporkpgm.util.uuid.HandleUUID;
import io.sporkpgm.util.uuid.MojangUUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;

import static io.sporkpgm.ListenerHandler.*;

public class Spork extends JavaPlugin {

	private static Spork instance;
	private static boolean debug;
	private static HandleUUID uuid = new MojangUUID();

	private Config config;
	private BuilderFactory factory;
	private CommandsManager<CommandSender> commands;
	private CommandsManagerRegistration registration;

	@Override
	public void onEnable() {
		instance = this;
		setDebug(true);
		config = Config.load(this, "settings.yml");

		Log.debug("Registering factories, commands, maps and listeners");
		register();
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	@Override
	public Config getConfig() {
		return config;
	}

	public void register() {
		this.factory = new BuilderFactory();
		this.factory.register(InfoModule.class);
		this.factory.register(Region.class);
		this.factory.register(TeamModule.class);
		this.factory.register(KitModule.class);
		this.factory.register(SpawnModule.class);
		this.factory.register(Filter.class);
		this.factory.register(MobModule.class);
		this.factory.register(DisableDamageModule.class);
		this.factory.register(FriendlyFireModule.class);
		this.factory.register(ItemRemoveModule.class);
		this.factory.register(MaxHeightModule.class);
		this.factory.register(DestroyableModule.class);
		this.factory.register(VictoryModule.class);
		this.factory.register(CoreModule.class);
		Log.debug("Loaded " + this.factory.getBuilders().size() + " Modules into the Factory");

		new SporkFactory();
		for(File file : Maps.getFiles()) {
			SporkFactory.register(file);
		}
		Log.debug("Loaded " + SporkFactory.getMaps().size() + " Maps into the Factory");

		try {
			Rotation rotation = Rotation.provide();
			rotation.start();
		} catch(Exception e) {
			Log.exception(e);
			setEnabled(false);
			return;
		}
		Log.debug("Finished loading the Rotation");

		this.commands = new CommandsManager<CommandSender>() {
			public boolean hasPermission(CommandSender sender, String perm) {
				return sender.hasPermission(perm);
			}
		};

		this.registration = new CommandsManagerRegistration(this, this.commands);
		this.registration.register(MapCommands.class);
		this.registration.register(MatchCommands.class);
		this.registration.register(MiscCommands.class);
		this.registration.register(RotationCommands.class);
		this.registration.register(TeamCommands.class);

		registerListener(new BlockListener());
		registerListener(new ConnectionListener());
		registerListener(new FilterTriggerListener());
		// registerListener(new MapListener());
		registerListener(new PlayerListener());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			this.commands.execute(cmd.getName(), args, sender, sender);
		} catch(CommandPermissionsException e) {
			sender.sendMessage(ChatColor.RED + "You don't have permission.");
		} catch(MissingNestedCommandException e) {
			sender.sendMessage(ChatColor.RED + e.getUsage());
		} catch(CommandUsageException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
			sender.sendMessage(ChatColor.RED + e.getUsage());
		} catch(WrappedCommandException e) {
			if(e.getCause() instanceof NumberFormatException) {
				sender.sendMessage(ChatColor.RED + "Number expected, string received.");
			} else {
				sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
				e.printStackTrace();
			}
		} catch(CommandException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
		}

		return true;
	}

	public static Spork get() {
		return instance;
	}

	public static BuilderFactory getFactory() {
		return get().factory;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean value) {
		debug = value;
		Log.setDebugging(value);
	}

	public static XMLOutputter getOutputter() {
		return new XMLOutputter(Format.getPrettyFormat());
	}

	public static File getRoot() {
		return get().getDataFolder().getAbsoluteFile().getParentFile().getParentFile();
	}

	public static String getUUID(String string) {
		return uuid.getUUID(string);
	}

	public static String getName(String string) {
		return uuid.getName(string);
	}

}
