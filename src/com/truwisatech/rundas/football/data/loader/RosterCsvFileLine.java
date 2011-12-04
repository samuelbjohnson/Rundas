package com.truwisatech.rundas.football.data.loader;

public class RosterCsvFileLine extends GenericCsvFileLine {

	public int getTeamId() {
		return teamId;
	}
	
	public String getTeamName() {
		return teamName;
	}

	public String getUniformNum() {
		return uniformNum;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getPosition() {
		return position;
	}

	public String getYear() {
		return year;
	}

	public int getPlayerId() {
		return playerId;
	}

	private int teamId;
	private String teamName;
	private String uniformNum;
	private String lastName;
	private String firstName;
	private String position;
	private String year;
	private int playerId;
	
	public RosterCsvFileLine(String line) {
		super(line);
	}

	@Override
	protected void parseInputs() {
		if (parsedInputs.length != 8) {
			System.err.println("File line: " + line + " is invalid");
			throw new CsvFileFormatException();
		}
		
		teamId = Integer.valueOf(parsedInputs[0]);
		
		//the second column, team name, is not stored
		teamName = parsedInputs[1];
		
		uniformNum = parsedInputs[2];
		lastName = parsedInputs[3];
		firstName = parsedInputs[4];
		position = parsedInputs[5];
		year = parsedInputs[6];
		playerId = Integer.valueOf(parsedInputs[7]);
	}

}
