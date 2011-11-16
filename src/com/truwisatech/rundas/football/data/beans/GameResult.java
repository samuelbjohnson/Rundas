package com.truwisatech.rundas.football.data.beans;

public class GameResult {	
	
	private int id;
	
	private int homeScore;
	private int awayScore;
	
	private ScheduledGame scheduledGame;
	
	public String toString() {
		return super.toString() + ", " + (
				homeScore > awayScore ?
				scheduledGame.getHomeTeam() : scheduledGame.getAwayTeam()
				) + " wins " + awayScore + "-" + homeScore;
	}
	
	public GameResult() {
		
	}
	
	public GameResult(ScheduledGame game) {
		scheduledGame.setGameId(game.getGameId());
		scheduledGame.setGameDate(game.getGameDate());
		scheduledGame.setAwayTeam(game.getAwayTeam());
		scheduledGame.setHomeTeam(game.getHomeTeam());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHomeScore() {
		return homeScore;
	}
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}
	public int getAwayScore() {
		return awayScore;
	}
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public ScheduledGame getScheduledGame() {
		return scheduledGame;
	}

	public void setScheduledGame(ScheduledGame scheduledGame) {
		this.scheduledGame = scheduledGame;
	}
}
