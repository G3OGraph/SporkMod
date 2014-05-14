package io.sporkpgm.tourney.api;

import io.sporkpgm.util.UrlUtil;
import org.json.JSONObject;

public class TourneyTeam {

	private boolean disbanded;
	private String name;
	private String id;

	protected TourneyTeam(String id) throws TeamNotFoundException {
		this.id = id;
		JSONObject json = UrlUtil.getAPI("team/" + id);
		if(json == null) {
			throw new TeamNotFoundException(TeamSearchCriteria.ID, id);
		}
	}

}
