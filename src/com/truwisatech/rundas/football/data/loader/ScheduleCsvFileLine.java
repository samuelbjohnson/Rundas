package com.truwisatech.rundas.football.data.loader;

import java.util.Date;

public class ScheduleCsvFileLine extends GenericCsvFileLine {
	
	private int teamId;
	private String teamName;
	private Date gameDate;
	private int oppTeamId;
	private String oppTeamName;
	
	private int scoreFor;
	private int scoreAgainst;
	
	private boolean homeGame;
	
	public ScheduleCsvFileLine(String line) {
		super(line);
	}
	
	@Override
	protected void parseInputs() {
		if (parsedInputs.length != 8) {
			System.err.println("File line: " + line + " is invalid");
			throw new CsvFileFormatException();
		}
		
		removeQuotes(parsedInputs);
		
		teamId = Integer.valueOf(parsedInputs[0]);
		teamName = parsedInputs[1];
		
		gameDate = parseDate(parsedInputs[2]);
		
		oppTeamId = Integer.valueOf(parsedInputs[3]);
		oppTeamName = parsedInputs[4];
		
		scoreFor = !parsedInputs[5].equals("") ? Integer.valueOf(parsedInputs[5]) : -1;
		scoreAgainst = !parsedInputs[6].equals("") ? Integer.valueOf(parsedInputs[6]) : -1;
		
		homeGame = parsedInputs[7].equals("Home");
		
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

	public int getOppTeamId() {
		return oppTeamId;
	}

	public void setOppTeamId(int oppTeamId) {
		this.oppTeamId = oppTeamId;
	}

	public String getOppTeamName() {
		return oppTeamName;
	}

	public void setOppTeamName(String oppTeamName) {
		this.oppTeamName = oppTeamName;
	}

	public int getScoreFor() {
		return scoreFor;
	}

	public void setScoreFor(int scoreFor) {
		this.scoreFor = scoreFor;
	}

	public int getScoreAgainst() {
		return scoreAgainst;
	}

	public void setScoreAgainst(int scoreAgainst) {
		this.scoreAgainst = scoreAgainst;
	}

	public boolean isHomeGame() {
		return homeGame;
	}

	public void setHomeGame(boolean homeGame) {
		this.homeGame = homeGame;
	}
}
