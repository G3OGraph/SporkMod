package io.sporkpgm.module.modules.friendlyfire;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.user.User;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@ModuleInfo(description = "The module that toggles friendly fire", builder = FriendlyFireBuilder.class)
public class FriendlyFireModule extends Module implements Listener {

	private boolean enabled;

	public FriendlyFireModule(boolean enabled) {
		this.enabled = enabled;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDamageEvent(EntityDamageByEntityEvent event) {
		if(enabled) {
			return;
		}

		Player entity = null;
		Player damager = null;

		if(event.getEntity() instanceof Player) {
			entity = (Player) event.getEntity();
		}

		if(event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
		} else if(event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			if(projectile.getShooter() instanceof Player) {
				damager = (Player) projectile.getShooter();
			}
		}

		if(damager == null || entity == null) {
			return;
		}

		TeamModule victimTeam = User.getUser(entity).getTeam();
		TeamModule attackerTeam = User.getUser(damager).getTeam();
		if(victimTeam == attackerTeam) {
			event.setCancelled(true);
		}
	}

}
