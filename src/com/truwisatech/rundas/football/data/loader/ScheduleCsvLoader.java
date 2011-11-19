package com.truwisatech.rundas.football.data.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.truwisatech.rundas.football.data.NcaaFootballDao;
import com.truwisatech.rundas.football.data.NcaaFootballData;
import com.truwisatech.rundas.football.data.RundasMySqlConnectionFactory;
import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.Game;

public class ScheduleCsvLoader {

	private static NcaaFootballDao db = new NcaaFootballData(new RundasMySqlConnectionFactory());
	
	private static int gamesInserted = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO update file location/name
		File input = new File("/Users/johnss4-1/Downloads/DIVISIONB (4).csv");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String line = reader.readLine(); //First line is just headings
			int lineNumber = 0;
			while ((line = reader.readLine()) != null) {
				lineNumber += 1;
				System.out.print("Line: " + lineNumber + " ");
				ScheduleCsvFileLine fileLine = new ScheduleCsvFileLine(line);
				if (! processFileLine(fileLine)) {
					System.err.println("Line: " + line + " didn't process");
				}
				
			}
			
			System.out.println("\nComplete\nTotal Lines: " + lineNumber);
			System.out.println("Games: " + gamesInserted);
			
		}
		catch (IOException i) {
			System.err.print("Problem with file ");
			System.err.println(i.getMessage());
			System.exit(0);
		}

	}
	
	private static boolean processFileLine(ScheduleCsvFileLine line) {
		System.out.println("Processing " + line.getTeamName() + " vs " + line.getOppTeamName());
		
		NcaaTeam t = new NcaaTeam();
		t.setNcaaTeamId(line.getTeamId());
		t.setNcaaTeamName(line.getTeamName());
		NcaaTeam oppT = new NcaaTeam();
		oppT.setNcaaTeamId(line.getOppTeamId());
		oppT.setNcaaTeamName(line.getOppTeamName());
		
		if (db.findTeam(t.getNcaaTeamId()) == null) {
			if (db.insertTeam(t)) {
				System.out.println("Inserted " + t.getNcaaTeamName());
			} else {
				System.err.println("Error inserting " + t.getNcaaTeamName());
			}
		}
		if (db.findTeam(oppT.getNcaaTeamId()) == null) {
			if (db.insertTeam(oppT)) {
				System.out.println("Inserted " + oppT.getNcaaTeamName());
			} else {
				System.err.println("Error inserting " + oppT.getNcaaTeamName());
			}
		}
		
		Game game = new Game();
		if (line.isHomeGame()) {
			game.setHomeTeam(t);
			game.setAwayTeam(oppT);
			game.setHomeScore(line.getScoreFor());
			game.setAwayScore(line.getScoreAgainst());
		} else {
			game.setHomeTeam(oppT);
			game.setAwayTeam(t);
			game.setHomeScore(line.getScoreAgainst());
			game.setAwayScore(line.getScoreFor());
		}
		game.setGameDate(line.getGameDate());
		
		Game existingGame = db.findGame(game);
		
		if (existingGame == null) {
			if (db.insertGame(game)) {
				System.out.println("Inserted " + game.getAwayTeam().getNcaaTeamName()
						+ " " + game.getAwayScore() + " at " + game.getHomeTeam().getNcaaTeamName()
						+ " " + game.getHomeScore() + " on " + game.getGameDate());
				gamesInserted += 1;
			} else {
				return false;
			}
		} else {
			game.setGameId(existingGame.getGameId());
			if (db.updateGame(game) >= 1) {
				System.out.println("Updated " + game.getAwayTeam().getNcaaTeamName()
						+ " " + game.getAwayScore() + " at " + game.getHomeTeam().getNcaaTeamName()
						+ " " + game.getHomeScore() + " on " + game.getGameDate());
				gamesInserted += 1;
			} else {
				return false;
			}
		} 
		
		return true;
	}

}
