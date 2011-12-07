package com.truwisatech.rundas.football.data.beans;

public class NcaaTeam {
	private int ncaaTeamId;
	private String ncaaTeamName;
	
	public int getNcaaTeamId() {
		return ncaaTeamId;
	}
	public void setNcaaTeamId(int ncaaTeamId) {
		this.ncaaTeamId = ncaaTeamId;
	}
	public String getNcaaTeamName() {
		return ncaaTeamName;
	}
	public void setNcaaTeamName(String ncaaTeamName) {
		this.ncaaTeamName = ncaaTeamName;
	}
	
	public String toJson() {
		return "{\"teamName\":\"" + ncaaTeamName + "\", \"teamId\":\"" + ncaaTeamId + "\"}";
	}
}
