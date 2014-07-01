package in.parapengu.spork.listeners;

import in.parapengu.spork.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class ConnectionListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PlayerData data = new PlayerData(event.getPlayer());
		data.add();
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		remove(event.getPlayer());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		remove(event.getPlayer());
	}

	public void remove(Player player) {
		PlayerData data = PlayerData.getPlayer(player);
		data.remove();
	}

	@EventHandler
	public void onPing(ServerListPingEvent event) {

	}

}
