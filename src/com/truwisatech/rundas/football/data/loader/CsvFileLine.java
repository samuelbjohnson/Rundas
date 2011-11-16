package com.truwisatech.rundas.football.data.loader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CsvFileLine {
	private int teamId;
	private String teamName;
	private Date gameDate;
	private int oppTeamId;
	private String oppTeamName;
	
	private int scoreFor = -1;
	private int scoreAgainst = -1;
	
	private boolean homeGame;
	
	private static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	
	public CsvFileLine(String fileLine) {
		String[] parsedInputs = fileLine.split(",");
		if (parsedInputs.length != 8) {
			System.err.println("File line: " + fileLine + " is invalid");
			System.exit(0);
		}
		
		//remove quotes
		for (int i = 0; i < parsedInputs.length; i++) {
			String s = parsedInputs[i];
			if (s.startsWith("\"")) {
				s = s.substring(1);
			}
			if(s.endsWith("\"")) {
				s = s.substring(0, s.length() - 1);
			}
			parsedInputs[i] = s;
		}
		
		teamId = Integer.valueOf(parsedInputs[0]);
		teamName = parsedInputs[1];
		
		try {
			gameDate = format.parse(parsedInputs[2]);
		}
		catch (ParseException p) {
			System.err.println("Could Not Parse Date: " + parsedInputs[2]);
			System.exit(0);
		}
		
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
