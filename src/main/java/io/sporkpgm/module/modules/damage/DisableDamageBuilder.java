package io.sporkpgm.module.modules.damage;

import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.builder.BuilderResult;
import io.sporkpgm.module.exceptions.ModuleBuildException;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(result = BuilderResult.SINGLE)
public class DisableDamageBuilder extends Builder {

	@Override
	public DisableDamageModule single(BuilderContext context) throws ModuleBuildException {
		if(!context.only("document", "loader", "map", "match")) {
			return null;
		}

		Element root = context.getDocument().getRootElement();

		List<EntityDamageEvent.DamageCause> causes = new ArrayList<>();
		for(Element e : root.getChildren("disabledamage")) {
			for(Element damage : e.getChildren("damage")) {
				String damageS = damage.getText().toUpperCase();
				causes.add(EntityDamageEvent.DamageCause.valueOf(damageS));
			}
		}

		if(causes.size() == 0) {
			return null;
		}

		return new DisableDamageModule(causes);
	}
}
