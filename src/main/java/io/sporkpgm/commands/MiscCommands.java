package io.sporkpgm.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import io.sporkpgm.user.User;
import io.sporkpgm.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MiscCommands {

	@Command(aliases = {"a"}, desc = "Admin chat", usage = "[message]", min = 1)
	@CommandPermissions("spork.misc.adminchat")
	public static void adminchat(CommandContext cmd, CommandSender sender) throws CommandException {
		if(!(sender instanceof Player)) {
			throw new CommandException("Only players can use admin chat");
		}

		for(User player : User.getUsers()) {
			if(player.getPlayer().hasPermission("spork.misc.adminchat")) {
				ChatColor white = ChatColor.WHITE;
				User user = User.getUser((Player) sender);
				player.getPlayer().sendMessage(white + "[" + ChatColor.GOLD + "A" + white + "] " + user.getTeam().getColor() + user.getName() + ChatColor.GRAY + ": " + white + cmd.getJoinedStrings(0));
			}
		}

	}

	@Command(aliases = {"staff"}, desc = "List of online staff")
	public static void staff(CommandContext cmd, CommandSender sender) throws CommandException {
		List<String> list = new ArrayList<>();
		for(User player : User.getUsers()) {
			if(player.getPlayer().hasPermission("spork.misc.staff")) {
				list.add(player.getTeam().getColor() + player.getName());
			}
		}
		String staff = StringUtil.staffList(list, ChatColor.WHITE);
		String others = ChatColor.WHITE + "[" + ChatColor.GOLD + "SporkPGM" + ChatColor.WHITE + "] " + ChatColor.GRAY + "Staff Online (" + list.size() + "): ";
		sender.sendMessage(others + staff);

	}

	@Command(aliases = {"request"}, desc = "Send a request to staff", usage = "[message]", min = 1)
	public static void request(CommandContext cmd, CommandSender sender) throws CommandException {
		if(!(sender instanceof Player)) {
			throw new CommandException("Only players can request");
		}

		for(User player : User.getUsers()) {
			if(player.getPlayer().hasPermission("spork.misc.staff")) {
				ChatColor white = ChatColor.WHITE;
				User user = User.getUser((Player) sender);
				player.getPlayer().sendMessage(white + "[" + ChatColor.GOLD + "A" + white + "] " + user.getTeam().getColor() + user.getName() + ChatColor.GRAY + " has requested " + white + "'" + cmd.getJoinedStrings(0) + "'");
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ORB_PICKUP, 10, 1);

			}
		}
		sender.sendMessage(ChatColor.RED + "Your request has been submitted.");
	}
}
