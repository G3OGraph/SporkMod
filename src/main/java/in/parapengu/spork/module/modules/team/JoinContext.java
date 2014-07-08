package in.parapengu.spork.module.modules.team;

import in.parapengu.spork.player.PlayerData;

public class JoinContext {

	private PlayerData player;
	private boolean forced;
	private boolean teleport;
	private boolean inventory;

	public JoinContext(PlayerData player) {
		this.player = player;
	}

	public PlayerData getPlayer() {
		return player;
	}

	public boolean isForced() {
		return forced;
	}

	public void setForced(boolean forced) {
		this.forced = forced;
	}

	public boolean isTeleport() {
		return teleport;
	}

	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}

	public boolean isInventory() {
		return inventory;
	}

	public void setInventory(boolean inventory) {
		this.inventory = inventory;
	}
}
