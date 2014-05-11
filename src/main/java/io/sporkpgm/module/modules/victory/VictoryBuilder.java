package io.sporkpgm.module.modules.victory;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleBuildException;
import io.sporkpgm.module.modules.region.RegionBuilder;
import io.sporkpgm.module.modules.region.types.BlockRegion;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.util.OtherUtil;
import io.sporkpgm.util.StringUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo
public class VictoryBuilder extends Builder {

	@Override
	public VictoryModule[] array(BuilderContext context) throws ModuleBuildException {
		if(!context.only("document", "loader", "map")) {
			return null;
		}

		SporkMap map = context.getMap();
		Document document = context.getDocument();
		Element root = document.getRootElement();

		List<VictoryModule> modules = new ArrayList<>();

		for(Element element : root.getChildren("wools")) {
			String teamS = element.getAttributeValue("team");

			for(Element wool : element.getChildren("wool")) {
				teamS = (wool.getAttributeValue("team") != null ? wool.getAttributeValue("team") : teamS);
				TeamModule team = map.getTeams().getTeam(teamS);

				DyeColor dye = StringUtil.convertStringToDyeColor(wool.getAttributeValue("color"));
				if(dye == null) {
					throw new ModuleBuildException(wool, "'" + wool.getAttributeValue("color") + "' is not a supported DyeColor");
				}

				String name = WordUtils.capitalizeFully(StringUtil.fromTechnicalName(dye.name()) + " Wool");
				ChatColor color = StringUtil.convertDyeColorToChatColor(dye);
				BlockRegion block = RegionBuilder.parseBlock(wool.getChild("block"));
				modules.add(new VictoryModule(name, team, dye, color, block));
			}
		}

		return OtherUtil.toArray(VictoryModule.class, modules);
	}

}
