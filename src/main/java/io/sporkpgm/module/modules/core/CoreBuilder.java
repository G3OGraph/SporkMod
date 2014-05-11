package io.sporkpgm.module.modules.core;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.builder.Builder;
import io.sporkpgm.module.builder.BuilderContext;
import io.sporkpgm.module.builder.BuilderInfo;
import io.sporkpgm.module.exceptions.ModuleBuildException;
import io.sporkpgm.module.modules.region.Region;
import io.sporkpgm.module.modules.region.RegionBuilder;
import io.sporkpgm.module.modules.team.TeamModule;
import io.sporkpgm.util.OtherUtil;
import io.sporkpgm.util.StringUtil;
import io.sporkpgm.util.XMLUtil;
import org.bukkit.Material;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo
public class CoreBuilder extends Builder {

	@Override
	public CoreModule[] array(BuilderContext context) throws ModuleBuildException {
		if(!context.only("document", "loader", "map")) {
			return null;
		}

		SporkMap map = context.getMap();
		Document document = context.getDocument();
		List<CoreModule> modules = new ArrayList<>();
		Element root = document.getRootElement();
		modules.addAll(cores(map, root));
		return OtherUtil.toArray(CoreModule.class, modules);
	}

	public List<CoreModule> cores(SporkMap map, Element element) throws ModuleBuildException {
		List<CoreModule> sporks = new ArrayList<>();

		sporks.addAll(parseCores(map, element.getChildren("core")));
		for(Element spawns : element.getChildren("cores")) {
			sporks.addAll(cores(map, spawns));
		}

		return sporks;
	}

	public List<CoreModule> parseCores(SporkMap map, List<Element> elements) throws ModuleBuildException {
		List<CoreModule> objectives = new ArrayList<>();
		for(Element element : elements) {
			objectives.add(parseCore(map, element));
		}
		return objectives;
	}

	public CoreModule parseCore(SporkMap map, Element element) throws ModuleBuildException {
		String name = XMLUtil.getElementOrParentValue(element, "name");
		if(name == null) {
			name = "Core";
		}

		String type = XMLUtil.getElementOrParentValue(element, "material");
		Material material = StringUtil.convertStringToMaterial(type);
		if(material == null) {
			material = Material.OBSIDIAN;
		}

		String value = XMLUtil.getElementOrParentValue(element, "leak");
		int leak;
		try {
			leak = Integer.parseInt(value);
		} catch(Exception e) {
			throw new ModuleBuildException((value != null ? value : "null") + " is not a valid integer");
		}

		TeamModule other = null;
		String team = XMLUtil.getElementOrParentValue(element, "team");
		if(team != null) {
			other = map.getTeams().getTeam(team);
		}

		Region region = RegionBuilder.parseCuboid(element.getChildren().get(0));
		TeamModule owner = (other == null ? null : other.getOpposite());

		if(leak <= 0) {
			throw new ModuleBuildException("Leak distance must be greater than 0");
		} else if(owner == null) {
			throw new ModuleBuildException("The owner of a Core can't be null");
		} else if(region == null) {
			throw new ModuleBuildException("The region of a Core can't be null");
		}

		return new CoreModule(name, material, region, owner, leak);
	}

}
