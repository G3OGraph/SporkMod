package io.sporkpgm.module.modules.core;

import com.google.common.collect.Lists;
import io.sporkpgm.event.map.BlockChangeEvent;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.ObjectiveModule;
import io.sporkpgm.module.modules.region.Region;
import io.sporkpgm.module.modules.region.types.BlockRegion;
import io.sporkpgm.module.modules.region.types.groups.UnionRegion;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.user.User;
import io.sporkpgm.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.*;

@ModuleInfo(description = "Objective where liquid flowing from a region is tracked until a specified distance", builder = CoreBuilder.class)
public class CoreModule extends ObjectiveModule implements Listener {

	private UnionRegion core;
	private UnionRegion casing;
	private Liquid liquid;
	private Material material;
	private Region region;
	private int leak;

	private boolean touched;
	private boolean completed;

	public CoreModule(String name, Material material, Region region, TeamModule team, int leak) {
		super(name, team);
		this.material = material;
		this.region = region;
		this.leak = leak;
	}

	public Material getMaterial() {
		return material;
	}

	public Region getRegion() {
		return region;
	}

	@Override
	public void setCompleted(boolean complete) {
		super.setCompleted(complete);
		if(!complete) {
			return;
		}

		TeamModule opposite = team.getOpposite();
		Bukkit.broadcastMessage(opposite.getColoredName() + "'s " + ChatColor.DARK_AQUA + name + ChatColor.GRAY + " has leaked!");
	}

	public void start() {
		this.liquid = Liquid.getLiquid(region, team.getMap().getWorld());

		List<Region> regions = new ArrayList<>();
		regions.addAll(region.getValues(liquid.getMaterials(), team.getMap().getWorld()));
		this.core = new UnionRegion(name, regions);

		regions = new ArrayList<>();
		regions.addAll(region.getValues(material, team.getMap().getWorld()));
		this.casing = new UnionRegion(name, regions);
	}

	public void stop() { /* nothing */ }

	@EventHandler
	public void onCoreLeak(BlockChangeEvent event) {
		if(isCompleted()) {
			return;
		}

		BlockState old = event.getOldState();
		BlockState now = event.getNewState();
		if(!Liquid.matches(liquid, old.getType()) && !Liquid.matches(liquid, now.getType())) {
			Log.info(liquid.name() + " doesn't match " + old.getType().name() + " or " + now.getType().name());
			return;
		}

		BlockRegion location = event.getRegion();
		if(event.getEvent() instanceof BlockFromToEvent) {
			BlockFromToEvent from = (BlockFromToEvent) event.getEvent();
			if(Liquid.matches(liquid, old.getType())) {
				location = new BlockRegion(from.getBlock().getLocation());
			} else {
				location = new BlockRegion(from.getToBlock().getLocation());
			}
		}

		/*
		if(!event.isPlace()) {
			// Thread.dumpStack();
			Log.info("Ignoring event because it was not a block being changed from air (" + event.getOldState().getType() + " => " + event.getNewState().getType() + ")");
			return;
		}
		*/

		if(event.hasPlayer()) {
			Log.info("Ignoring event because it was caused by a player");
			return;
		}

		double distance = region.distance(location);
		if(distance >= leak && distance <= leak + 4) {
			setCompleted(true);
		}
	}

	@EventHandler
	public void onTeamDamage(BlockChangeEvent event) {
		if(isCompleted()) {
			return;
		}

		if(event.isPlace()) {
			if(core.isBelow(event.getRegion())) {
				event.setCancelled(true);
				if(event.hasPlayer()) {
					event.getPlayer().getPlayer().sendMessage(ChatColor.RED + "You can't modify the contents of the core");
				}
			}
			return;
		}

		if(!event.hasPlayer()) {
			return;
		}

		if(casing.isInside(event.getRegion())) {
			TeamModule team = event.getPlayer().getTeam();
			if(team != getTeam()) {
				event.setCancelled(true);
				event.getPlayer().getPlayer().sendMessage(ChatColor.RED + "You can't leak your own core");
			} else {
				touched = true;

				String message = team.getColor() + "[Team] " + ChatColor.AQUA + event.getPlayer().getName() + ChatColor.GRAY + " broke a peice of the " + ChatColor.RED + name;
				for(User user : team.getPlayers()) {
					user.getPlayer().sendMessage(message);
				}
			}
		}
	}

	public int getScore() {
		return (touched ? 50 : 0) + (completed ? 100 : 0);
	}

	public int getMaxScore() {
		return 50 + 100;
	}

	public enum Liquid {

		LAVA(new Material[]{Material.LAVA, STATIONARY_LAVA}),
		WATER(new Material[]{Material.WATER, STATIONARY_WATER}),
		NONE(new Material[0]);

		Material[] materials;

		Liquid(Material[] materials) {
			this.materials = materials;
		}

		public Material[] getMaterials() {
			return materials;
		}

		public boolean matches(Material material) {
			return Lists.newArrayList(materials).contains(material);
		}

		public boolean contains(Region region, World world) {
			for(Material material : materials) {
				if(region.isInside(material, world)) {
					return true;
				}
			}

			return false;
		}

		public static Liquid getLiquid(Region region, World world) {
			for(Liquid liquid : values()) {
				if(liquid.contains(region, world)) {
					return liquid;
				}
			}

			return NONE;
		}

		public static boolean matches(Liquid liquid, Material material) {
			return liquid.matches(material);
		}

	}

}
