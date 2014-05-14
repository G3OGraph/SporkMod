package io.sporkpgm.module.modules.damage;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(description = "The module that controls damage", builder = DisableDamageBuilder.class)
public class DisableDamageModule extends Module implements Listener {

	private List<EntityDamageEvent.DamageCause> damageCauses = null;

	public DisableDamageModule(List<EntityDamageEvent.DamageCause> damageCauses) {
		this.damageCauses = damageCauses;
	}

	public List<EntityDamageEvent.DamageCause> getDamageCauses() {
		return damageCauses;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		List<String> causes = new ArrayList<>();
		if(!(event.getEntity() instanceof Player) || damageCauses == null) {
			return;
		}
		for(EntityDamageEvent.DamageCause cause : damageCauses) {
			causes.add(cause.name());
		}
		if(causes.contains(event.getCause().name())) {
			event.setCancelled(true);
		}

	}

}
