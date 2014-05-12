package io.sporkpgm.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import io.sporkpgm.map.SporkFactory;
import io.sporkpgm.map.SporkLoader;
import io.sporkpgm.rotation.Rotation;
import io.sporkpgm.rotation.RotationSlot;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.PaginatedResult;
import io.sporkpgm.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RotationCommands {

	@Command(aliases = {"rotation", "rot"}, desc = "View the current rotation", usage = "[page]", max = 1)
	public static void rotation(CommandContext cmd, CommandSender sender) throws CommandException {
		String bar = ChatColor.RED + "" + ChatColor.STRIKETHROUGH + " ----------- ";
		String loaded = ChatColor.DARK_AQUA + "Rotation (" + ChatColor.AQUA + "[page]" + ChatColor.DARK_AQUA + " of "
				+ ChatColor.AQUA + "[pages]" + ChatColor.DARK_AQUA + ")";

		String header = bar + loaded + bar;
		List<String> rows = new ArrayList<>();
		for(RotationSlot slot : Rotation.get().getRotation().getSlots()) {
			rows.add(ChatColor.GOLD + slot.getLoader().getName() + " " + ChatColor.DARK_PURPLE + "by " + StringUtil.listToEnglishCompound(slot.getLoader().getAuthors(), ChatColor.RED.toString(), ChatColor.DARK_PURPLE.toString()));
		}

		int results = 8;

		PaginatedResult result = new PaginatedResult(header, rows, results, true);
		result.display(sender, cmd.getInteger(0, 1));
	}

	@Command(aliases = {"setnext", "sn"}, desc = "Set the next map", usage = "[map]", flags = "f", min = 1)
	@CommandPermissions("spork.match.setnext")
	public static void setnext(CommandContext cmd, CommandSender sender) throws CommandException {
		String search = cmd.getJoinedStrings(0);
		Log.debug(sender.getName() + " searches " + search);
		if(cmd.hasFlag('f')) {
			search = search.replace(" -f", "");
			search = search.replace("-f", "");
		}

		SporkLoader map = SporkFactory.getMap(search);
		if(map == null) {
			sender.sendMessage(ChatColor.RED + "Could not find a map by that name");
			return;
		}

		Rotation.get().setRestart();
		boolean req = Rotation.get().setNext(map, cmd.hasFlag('f')) == null;
		if(!req) {
			Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + ChatColor.DARK_PURPLE + " set the next map to " + ChatColor.GOLD + map.getName());
		} else {
			throw new CommandException("Can't set next because the server is restarting. Use -f to force");
		}
	}

}
