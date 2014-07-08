package in.parapengu.spork.module.modules.team;

import in.parapengu.spork.exception.module.ModuleLoadException;
import in.parapengu.spork.module.builder.BuildPhase;
import in.parapengu.spork.module.builder.Builder;
import in.parapengu.spork.module.builder.BuilderContext;
import in.parapengu.spork.module.builder.BuilderInfo;
import in.parapengu.spork.util.ParsingUtil;
import org.bukkit.ChatColor;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.List;

@BuilderInfo(value = BuildPhase.LOAD, required = true)
public class TeamBuilder extends Builder<TeamModule> {

	public TeamBuilder() {
		this.module = TeamModule.class;
	}

	@Override
	public List<TeamModule> list(BuilderContext context) throws ModuleLoadException {
		Element root = context.getRoot();
		List<TeamModule> list = new ArrayList<>();

		Element element = root.getChild("teams");
		if(element == null) {
			throw new ModuleLoadException(module, "No \"teams\" element was available");
		}

		List<Element> teams = element.getChildren("team");
		if(teams.size() <= 0) {
			throw new ModuleLoadException(module, "No teams were available");
		}

		for(Element child : teams) {
			String name = child.getText();
			ChatColor color = ParsingUtil.parse(ChatColor.class, child.getAttributeValue("color"));
			ChatColor overhead = ParsingUtil.parse(ChatColor.class, child.getAttributeValue("overhead-color"));
			Integer max = ParsingUtil.parse(Integer.class, child.getAttributeValue("max"));
			Integer overfill = ParsingUtil.parse(Integer.class, child.getAttributeValue("max-overfill"));

			if(name == null) {
				throw new ModuleLoadException(module, new XMLOutputter(Format.getPrettyFormat()).outputString(child) + " did not contain a name");
			}

			if(color == null) {
				throw new ModuleLoadException(module, new XMLOutputter(Format.getPrettyFormat()).outputString(child) + " (" + name + ") did not contain a suitable color");
			}

			if(max == null) {
				throw new ModuleLoadException(module, new XMLOutputter(Format.getPrettyFormat()).outputString(child) + " (" + name + ") did not contain a suitable amount of max players");
			}

			if(overhead == null) {
				overhead = color;
			}

			if(overfill == null) {
				overfill = (int) Math.ceil((((double) max) / 4) * 5);
			}

			TeamModule team = new TeamModule(name, color, overhead, max, overfill, false);
			team.setTeams(context.getMap().getBoard().getTeams(team));
			list.add(team);
		}

		list.add(new TeamModule("Observers", ChatColor.AQUA, ChatColor.AQUA, -1, -1, true));
		return list;
	}

}
