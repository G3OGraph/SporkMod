package in.parapengu.spork.module.modules.spawn;

import com.google.common.collect.Lists;
import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.exception.region.ModuleParsingException;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.builder.BuilderInfo;
import in.parapengu.spork.module.builder.parsers.ParsingContext;
import in.parapengu.spork.module.modules.region.RegionModule;
import in.parapengu.spork.module.modules.region.types.combinations.UnionRegion;
import in.parapengu.spork.module.modules.region.types.combinations.UnionRegion.UnionParser;
import in.parapengu.spork.module.modules.team.TeamModule;
import in.parapengu.spork.util.ParsingUtil;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(value = BuildPhase.LOAD, required = true)
public class SpawnBuilder extends Builder<SpawnModule> {

	public SpawnBuilder() {
		super(SpawnModule.class);
	}

	@Override
	public List<? extends SpawnModule> list(BuilderContext context) throws ModuleLoadException {
		Element spawns = context.getRoot().getChild("spawns");
		if(spawns == null) {
			throw new ModuleLoadException(SpawnModule.class, "No list of spawns was found");
		}

		return getModules(spawns.getChildren(), context);
	}

	private List<SpawnModule> getModules(List<Element> elements, BuilderContext context) throws ModuleLoadException {
		List<SpawnModule> spawns = new ArrayList<>();
		List<TeamModule> teams = context.getModules().getModules(TeamModule.class);

		List<String> criteria = Lists.newArrayList("spawns", "spawn", "default");
		for(Element element : elements) {
			if(!criteria.contains(element.getName())) {
				continue;
			}

			if(element.getName().equalsIgnoreCase("spawns")) {
				spawns.addAll(getModules(element.getChildren(), context));
				continue;
			}

			String search = "observers";
			boolean obs = element.getName().equals("default");
			TeamModule team = TeamModule.getObservers(teams);
			if(!obs) {
				search = ParsingUtil.getAttributeValue(element, "team");
				if(search == null) {
					throw new ModuleLoadException(SpawnModule.class, "No team name specified for " + new XMLOutputter(Format.getPrettyFormat()).outputString(element));
				}

				team = TeamModule.search(teams, search);
			}

			if(team == null) {
				throw new ModuleLoadException(SpawnModule.class, "No team was found matching the search query: " + search);
			}

			UnionRegion region;
			try {
				List<UnionRegion> regions = new UnionParser().parse(new ParsingContext(element).addMap(context.getMap()).addRegions(context.getModules().getModules(RegionModule.class)));
				if(regions.size() < 1) {
					throw new ModuleLoadException(SpawnModule.class, "No region was specified");
				}

				region = regions.get(0);
			} catch(ModuleParsingException ex) {
				throw new ModuleLoadException(SpawnModule.class, ex);
			}

			SpawnModule spawn = new SpawnModule(region);
			spawns.add(spawn);
			team.addSpawn(spawn);
		}

		return spawns;
	}



}
