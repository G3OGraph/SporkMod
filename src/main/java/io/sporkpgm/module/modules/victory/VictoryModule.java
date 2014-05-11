package io.sporkpgm.module.modules.victory;

import io.sporkpgm.event.map.BlockChangeEvent;
import io.sporkpgm.match.phase.ServerPhase;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.ObjectiveModule;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.modules.region.types.BlockRegion;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.user.User;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(description = "Objective which tracks Block placements based on the wool material", builder = VictoryBuilder.class)
public class VictoryModule extends ObjectiveModule implements Listener {

	private DyeColor dye;
	private ChatColor color;
	private BlockRegion place;

	private User completer;
	private boolean complete;

	private List<String> touches;

	public VictoryModule(String name, TeamModule team, DyeColor dye, ChatColor color, BlockRegion place) {
		super(name, team);
		this.dye = dye;
		this.color = color;
		this.place = place;
		this.touches = new ArrayList<>();
	}

	@Override
	public TeamModule getTeam() {
		return team;
	}

	@Override
	public boolean isCompleted() {
		return complete;
	}

	@Override
	public void setCompleted(boolean complete) {
		super.setCompleted(complete);
		if(!complete) {
			return;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(completer.getTeam().getColor()).append(completer.getName());
		builder.append(ChatColor.GRAY).append(" placed ");
		builder.append(color).append(name.toUpperCase());
		builder.append(ChatColor.GRAY).append(" for the ");
		builder.append(completer.getTeam().getColoredName());

		Bukkit.broadcastMessage(builder.toString());
		Location location = completer.getPlayer().getLocation();
		Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.clearEffects();
		meta.addEffect(getFirework());
		firework.setFireworkMeta(meta);

		try {
			Method method = firework.getClass().getMethod("getHandle");
			method.setAccessible(true);
			method.invoke(firework);
		} catch(Exception e) {
			Log.warning("Server isn't running a version of Bukkit which allows the use of Firework.detonate() - resorting to manual detonation.");
			try {
				Object craft = NMSUtil.getClassBukkit("entity.CraftFirework").cast(firework);
				Method method = craft.getClass().getMethod("getHandle");
				method.setAccessible(true);
				Object handle = method.invoke(craft);
				handle.getClass().getField("expectedLifespan").set(handle, 0);
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockChange(BlockChangeEvent event) {
		if(!event.hasPlayer()) {
			if(place.isInside(event.getLocation())) {
				event.setCancelled(true);
			}

			return;
		}

		User player = event.getPlayer();
		if(place.isInside(event.getLocation())) {
			if(event.isBreak()) {
				event.setCancelled(true);
				player.getPlayer().sendMessage(ChatColor.RED + "You can't break that block");
			} else {
				if(player.getTeam() != team) {
					event.setCancelled(true);
					player.getPlayer().sendMessage(ChatColor.RED + "You can't place blocks there");
					return;
				}

				Block block = event.getNewBlock();
				if(block.getType() != Material.WOOL || block.getState().getData().getData() != dye.getWoolData()) {
					event.setCancelled(true);
					player.getPlayer().sendMessage(ChatColor.RED + "You can't place that block there");
					return;
				}

				completer = player;
				setCompleted(true);
				event.setCancelled(false);
				event.setLocked(true);
			}
		}
	}

	public FireworkEffect getFirework() {
		FireworkEffect.Builder builder = FireworkEffect.builder();
		builder.withColor(ServerPhase.getColor(color));
		builder.with(Type.BALL_LARGE);
		builder.trail(false);
		builder.flicker(true);
		return builder.build();
	}

	public Class<? extends Builder> builder() {
		return VictoryBuilder.class;
	}

	@Override
	public int getScore() {
		return (touches.size() * 50) + (completed ? 100 : 0);
	}

	@Override
	public int getMaxScore() {
		return (touches.size() * 50) + 100;
	}

	@Override
	public String toString() {
		return "VictoryModule{name=" + name + ",dye=" + dye.name() + ",color=" + color.name() + ",place=" + place.toString() + ",complete=" + complete + "}";
	}

}
