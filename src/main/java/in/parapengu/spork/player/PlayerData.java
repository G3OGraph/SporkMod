package in.parapengu.spork.player;

import in.parapengu.spork.module.modules.team.TeamModule;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

	private static List<PlayerData> players = new ArrayList<>();

	private Player player;
	private UUID uuid;
	private TeamModule team;

	public PlayerData(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
	}

	public Player getPlayer() {
		return player;
	}

	public UUID getUUID() {
		return uuid;
	}

	public TeamModule getTeam() {
		return team;
	}

	public void setTeam(TeamModule team) {
		this.team = team;
	}

	public boolean add() {
		if(players.contains(this)) {
			return false;
		}

		players.add(this);
		return true;
	}

	public boolean remove() {
		if(!players.contains(this)) {
			return false;
		}

		players.remove(this);
		return true;
	}

	public static List<PlayerData> getPlayers() {
		return players;
	}

	public static PlayerData getPlayer(Player player) {
		for(PlayerData data : players) {
			if(data.getPlayer().equals(player)) {
				return data;
			}
		}

		return null;
	}

	public static PlayerData getPlayer(UUID uuid) {
		for(PlayerData data : players) {
			if(data.getUUID().equals(uuid)) {
				return data;
			}
		}

		return null;
	}

	public static PlayerData getPlayer(String name) {
		for(PlayerData data : players) {
			if(data.getPlayer().getName().equalsIgnoreCase(name)) {
				return data;
			}
		}

		return null;
	}

}
