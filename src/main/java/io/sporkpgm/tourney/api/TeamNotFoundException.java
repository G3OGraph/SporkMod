package io.sporkpgm.tourney.api;

public class TeamNotFoundException extends Exception {

	private static final long serialVersionUID = -4563908673988596428L;

	private String search;
	private String id;

	public TeamNotFoundException(TeamSearchCriteria criteria, String string) {
		switch(criteria) {

			case SEARCH: this.search = string;
				break;
			case ID: this.id = string;
				break;
		}
	}

}
