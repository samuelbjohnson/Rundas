package com.truwisatech.rundas.football.data.beans;

public class NcaaPlayer {
	@Override
	public String toString() {
		return "NcaaPlayer [uniformNum=" + uniformNum + ", lastName="
				+ lastName + ", position=" + position + ", playerId="
				+ playerId + "]";
	}
	private int teamId;
	private String uniformNum;
	private String lastName;
	private String firstName;
	private String position;
	private String year;
	private int playerId;
	
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public String getUniformNum() {
		return uniformNum;
	}
	public void setUniformNum(String uniformNum) {
		this.uniformNum = uniformNum;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
}
