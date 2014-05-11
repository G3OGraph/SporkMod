package io.sporkpgm.module.modules.team;

import io.sporkpgm.map.SporkMap;
import io.sporkpgm.module.ObjectiveModule;
import io.sporkpgm.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

public class TeamCollection {

	private SporkMap map;
	private TeamModule observers;
	private List<TeamModule> teams;

	public TeamCollection(SporkMap map, List<TeamModule> teams) {
		this.map = map;
		this.observers = TeamBuilder.observers(map);
		this.teams = new ArrayList<>();
		for(TeamModule team : teams) {
			this.teams.add(team);
		}
	}

	public SporkMap getMap() {
		return map;
	}

	public TeamModule getObservers() {
		return observers;
	}

	public List<TeamModule> getTeams() {
		return teams;
	}

	public List<TeamModule> getTeams(String search) {
		List<TeamModule> test = new ArrayList<>();
		test.addAll(getTeams());
		test.add(getObservers());

		List<TeamModule> teams = new ArrayList<>();
		for(TeamModule team : test) {
			if(!teams.contains(team)) {
				String name = team.getName().toLowerCase();
				String colour = team.getColor().name().replace("_", " ").toLowerCase();
				if(name.equalsIgnoreCase(search) || colour.equalsIgnoreCase(search)) {
					teams.add(team);
				}
			}
		}

		if(teams.size() > 0) {
			return teams;
		}

		for(TeamModule team : test) {
			if(!teams.contains(team)) {
				String name = team.getName().toLowerCase();
				String colour = team.getColor().name().replace("_", " ").toLowerCase();
				if(name.startsWith(search.toLowerCase()) || colour.startsWith(search.toLowerCase())) {
					teams.add(team);
				}
			}
		}

		if(teams.size() > 0) {
			return teams;
		}

		for(TeamModule team : test) {
			if(!teams.contains(team)) {
				String name = team.getName().toLowerCase();
				String colour = team.getColor().name().replace("_", " ").toLowerCase();
				if(name.contains(search.toLowerCase()) || colour.contains(search.toLowerCase())) {
					teams.add(team);
				}
			}
		}

		return teams;
	}

	public TeamModule getTeam(String search) {
		List<TeamModule> teams = getTeams(search);
		return (teams.size() > 0 ? teams.get(0) : null);
	}

	public List<TeamModule> getLowestTeams() {
		List<TeamModule> teams = new ArrayList<>();

		teams.add(this.teams.get(0));
		int low = this.teams.get(0).getPlayers().size();

		for(TeamModule team : this.teams) {
			if(!teams.contains(team)) {
				if(team.getPlayers().size() <= low) {
					if(team.getPlayers().size() == low) {
						teams.add(team);
					} else {
						teams = new ArrayList<>();
						teams.add(team);
					}
				}
			}
		}

		return teams;
	}

	public TeamModule getLowestTeam() {
		List<TeamModule> teams = getLowestTeams();
		if(teams.size() == 1)
			return teams.get(0);
		if(teams.size() == 0)
			return null;

		return teams.get(NumberUtil.getRandom(0, teams.size() - 1));
	}

	public List<ObjectiveModule> getObjectives(TeamModule module) {
		List<ObjectiveModule> objectives = new ArrayList<>();
		for(ObjectiveModule objective : map.getModules().getModules(ObjectiveModule.class)) {
			if(objective.getTeam().equals(module)) {
				objectives.add(objective);
			}
		}
		return objectives;
	}

}
