package in.parapengu.spork.event.map;

import com.google.common.collect.Lists;
import in.parapengu.spork.map.SporkMap;
import in.parapengu.spork.module.modules.region.types.BlockRegion;
import in.parapengu.spork.player.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.List;

public class BlockChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private boolean locked;
	private Event cause;
	private SporkMap map;
	private PlayerData player;
	private BlockState oldState;
	private BlockState newState;

	public BlockChangeEvent(Event cause, SporkMap map, PlayerData player, BlockState oldState, BlockState newState) {
		this.cause = cause;
		this.map = map;
		this.player = player;
		this.oldState = oldState;
		this.newState = newState;
	}

	public BlockChangeEvent(Event cause, SporkMap map, BlockState oldState, BlockState newState) {
		this(cause, map, null, oldState, newState);
	}

	public Event getEvent() {
		return cause;
	}

	public SporkMap getMap() {
		return map;
	}

	public boolean hasPlayer() {
		if(player != null && player.getPlayer() == null) {
			this.player = null;
		}

		return player != null;
	}

	public PlayerData getPlayer() {
		return player;
	}

	public BlockState getState() {
		return getLocation().getBlock().getState();
	}

	public BlockState getOldState() {
		return oldState;
	}

	public BlockState getNewState() {
		return newState;
	}

	public Block getBlock() {
		return getLocation().getBlock();
	}

	public Block getOldBlock() {
		return getOldState().getBlock();
	}

	public Block getNewBlock() {
		return getNewState().getBlock();
	}

	public Location getLocation() {
		return getNewState().getLocation();
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}

	public boolean isCancellable() {
		return cause instanceof Cancellable;
	}

	public boolean isCancelled() {
		if(!isCancellable()) {
			BlockState state = getNewState();
			BlockState current = getBlock().getState();

			boolean type = state.getData().getItemType() != current.getData().getItemType();
			boolean data = state.getData().getData() != current.getData().getData();

			return type || data;
		}

		Cancellable cancel = (Cancellable) cause;
		return cancel.isCancelled();
	}

	public boolean setCancelled(boolean cancelled) {
		if(locked) {
			return false;
		}

		if(!isCancellable()) {
			BlockState state = getOldState();
			if(!cancelled) {
				state = getNewState();
			}

			getLocation().getBlock().setType(state.getType());
			getLocation().getBlock().setData(state.getData().getData());
			return false;
		}

		Cancellable cancel = (Cancellable) cause;
		cancel.setCancelled(cancelled);
		return true;
	}

	public boolean isBreak() {
		return getOldState().getType() != Material.AIR || getNewState().getType() == Material.AIR;
	}

	public boolean isPlace() {
		List<Material> materials = Lists.newArrayList(Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER);
		if(getEvent() instanceof BlockFromToEvent) {
			BlockFromToEvent from = (BlockFromToEvent) getEvent();
			if(materials.contains(from.getToBlock().getType()) || materials.contains(from.getBlock().getType())) {
				return true;
			}
		}

		return getOldState().getType() == Material.AIR || materials.contains(getOldState().getType());
	}

	public BlockRegion getRegion() {
		return new BlockRegion(null, getLocation());
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
