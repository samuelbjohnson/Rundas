package com.truwisatech.rundas.football.data.beans;

import java.util.Date;

public class ScheduledGame {
	
	private int gameId = -1;
	
	private NcaaTeam homeTeam;
	private NcaaTeam awayTeam;
	
	private Date gameDate;
	
	private GameResult result;
	
	public ScheduledGame() {
		
	}
	
	public String toString() {
		return gameDate + ":\t" + awayTeam + " at " + homeTeam;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public NcaaTeam getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(NcaaTeam homeTeam) {
		this.homeTeam = homeTeam;
	}

	public NcaaTeam getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(NcaaTeam awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

	public GameResult getResult() {
		return result;
	}

	public void setResult(GameResult result) {
		this.result = result;
	}
}
