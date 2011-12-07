package com.truwisatech.rundas.football.data.loader;

import java.util.Date;

public class StatsCsvFileLine extends GenericCsvFileLine {
	
	private int teamId;
	private String uniformNum;
	
	private Date gameDate;
	private int netRushingYards;
	private int totalPassingYards;
	private int totalReceivingYards;
	
	private boolean usefulStats;

	public int getTeamId() {
		return teamId;
	}

	public String getUniformNum() {
		return uniformNum;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public int getNetRushingYards() {
		return netRushingYards;
	}

	public int getTotalPassingYards() {
		return totalPassingYards;
	}

	public int getTotalReceivingYards() {
		return totalReceivingYards;
	}

	public StatsCsvFileLine(String line) {
		super(line);
	}
	
	public boolean hasUsefulStats() {
		return usefulStats;
	}

	@Override
	protected void parseInputs() {
		if (parsedInputs.length < 21) {
			System.err.println("File line: " + line + " is invalid");
			throw new CsvFileFormatException();
		}
		
		teamId = Integer.parseInt(parsedInputs[0]);
		uniformNum = parsedInputs[3];
		
		gameDate = parseDate(parsedInputs[2]);
		
		netRushingYards = parseIntEmptySafe(parsedInputs[9]);
		totalPassingYards = parseIntEmptySafe(parsedInputs[14]);
		totalReceivingYards = parseIntEmptySafe(parsedInputs[20]);

	}
	
	private int parseIntEmptySafe(String num) {
		if (num.equals("")) {
			return 0;
		}
		usefulStats = true;
		return Integer.parseInt(num);
	}

}
