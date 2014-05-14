package io.sporkpgm.module.modules.mob;

import io.sporkpgm.module.Module;
import io.sporkpgm.module.ModuleInfo;
import io.sporkpgm.module.builder.Builder;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(description = "The module that controls mob spawning", builder = MobBuilder.class)
public class MobModule extends Module implements Listener {

	private List<EntityType> mobs;
	private List<SpawnReason> reasons;

	public MobModule(List<EntityType> mobs, List<SpawnReason> reasons) {
		this.mobs = mobs;
		this.reasons = reasons;
	}

	public List<EntityType> getMobs() {
		return mobs;
	}

	public List<SpawnReason> getReasons() {
		return reasons;
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		List<String> mobNames = new ArrayList<>();
		List<String> reasonNames = new ArrayList<>();
		if(mobs == null && reasons == null) {
			event.setCancelled(true);
			return;
		}

		for(EntityType e : mobs) {
			if(e != null) {
				mobNames.add(e.name());
			}
		}

		for(CreatureSpawnEvent.SpawnReason r : reasons) {
			if(r != null) {
				reasonNames.add(r.name());
			}
		}

		if(mobs == null) {
			if(!reasonNames.contains(event.getSpawnReason().name())) {
				event.setCancelled(true);
				//Log.info("Blocked spawn because of reason: " + event.getSpawnReason().toString());
				return;
			}
		} else if(reasons == null) {
			if(!mobNames.contains(event.getEntityType().name())) {
				event.setCancelled(true);
				//Log.info("Blocked spawn because of mob type: " + event.getEntityType().getName());
				return;
			}
		} else if(mobs != null && reasons != null) {
			if(mobNames.contains(event.getEntityType().name()) && reasonNames.contains(event.getSpawnReason().name())) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
				//Log.info("Blocked spawn because of reason: " + event.getSpawnReason().toString());
				//Log.info("Blocked spawn because of mob type: " + event.getEntityType().getName());
			}
		}
	}

}

