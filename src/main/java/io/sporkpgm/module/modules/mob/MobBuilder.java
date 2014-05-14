package io.sporkpgm.module.modules.mob;

import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleBuildException;
import io.sporkpgm.util.Log;
import io.sporkpgm.util.OtherUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo
public class MobBuilder extends Builder {

	@Override
	public MobModule[] array(BuilderContext context) throws ModuleBuildException {
		if(!context.only("document", "loader", "map", "match")) {
			return null;
		}

		List<MobModule> modules = new ArrayList<>();
		Element root = context.getDocument().getRootElement();
		List<CreatureSpawnEvent.SpawnReason> reasons = new ArrayList<>();
		List<EntityType> mobs = new ArrayList<>();
		for(Element e : root.getChildren("mobs")) {
			for(Element filter : e.getChildren("filter")) {
				for(Element reason : filter.getChildren("spawn")) {
					String reasonS = reason.getText().toUpperCase();
					reasons.add(CreatureSpawnEvent.SpawnReason.valueOf(reasonS));
				}

				for(Element mob : filter.getChildren("mob")) {
					String mobS = mob.getText().toUpperCase();
					mobs.add(EntityType.fromName(mobS));
					Log.debug(EntityType.fromName(mobS).name() + " is the mob!");
				}
			}

			modules.add(new MobModule(mobs, reasons));
		}

		if(modules.size() == 0) {
			modules.add(new MobModule(null, null));
		}

		return OtherUtil.toArray(MobModule.class, modules);
	}

}
