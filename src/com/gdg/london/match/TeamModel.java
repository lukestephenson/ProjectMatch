package com.gdg.london.match;

import java.util.List;

import org.codehaus.jackson.annotate.JsonSetter;
import org.codehaus.jackson.annotate.JsonValue;

public class TeamModel {
	private String team;
	
	private List<UserDetailsModel> members;
	
	public String getTeam() {
		return team;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	public List<UserDetailsModel> getMembers() {
		return members;
	}
	@JsonSetter("team_members")
	public void setMembers(List<UserDetailsModel> members) {
		this.members = members;
	}
	
	
}
