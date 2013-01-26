package com.gdg.london.match;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class UserDetailsModel {

	private String name;
	private String type;
	private String speciality;
	private String techs;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getTechs() {
		return techs;
	}
	public void setTechs(String techs) {
		this.techs = techs;
	}
	@JsonIgnore
	public String getSummary() {
		
		return appendNotNull(name,type,speciality,techs);
	}
	private String appendNotNull(String... fields) {
		String result = "";
		for (String field : fields) {
			if (field != null) {
				result += field + ", ";
			}
		}
		return result;
	}

	
	
	
}
