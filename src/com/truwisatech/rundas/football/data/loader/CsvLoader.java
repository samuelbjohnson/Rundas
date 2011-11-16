package com.truwisatech.rundas.football.data.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.truwisatech.rundas.football.data.NcaaFootballDao;
import com.truwisatech.rundas.football.data.NcaaFootballData;
import com.truwisatech.rundas.football.data.RundasMySqlConnectionFactory;
import com.truwisatech.rundas.football.data.beans.GameResult;
import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.ScheduledGame;

public class CsvLoader {

	private static NcaaFootballDao db = new NcaaFootballData(new RundasMySqlConnectionFactory());
	
	private static int gamesInserted = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO update file location/name
		File input = new File("/Users/johnss4-1/Downloads/DIVISIONB (3).csv");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String line = reader.readLine(); //First line is just headings
			int failedLines = 0;
			int totalLines = 0;
			while ((line = reader.readLine()) != null) {
				CsvFileLine fileLine = new CsvFileLine(line);
				if (! processFileLine(fileLine)) {
					System.err.println("Line: " + line + " didn't process");
					failedLines += 1;
				}
				totalLines += 1;
			}
			
			System.out.println("\nComplete\nTotal Lines: " + totalLines);
			System.out.println("Games: " + gamesInserted);
			
		}
		catch (IOException i) {
			System.err.print("Problem with file ");
			System.err.println(i.getMessage());
			System.exit(0);
		}

	}
	
	private static boolean processFileLine(CsvFileLine line) {
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
		
		GameResult game = new GameResult();
		ScheduledGame sched = new ScheduledGame();
		boolean gamePlayed = false;
		if (line.isHomeGame()) {
			sched.setHomeTeam(t);
			sched.setAwayTeam(oppT);
			if (line.getScoreFor() > -1) {
				game.setHomeScore(line.getScoreFor());
				game.setAwayScore(line.getScoreAgainst());
				gamePlayed = true;
			}
		} else {
			sched.setHomeTeam(oppT);
			sched.setAwayTeam(t);
			if (line.getScoreFor() > -1) {
				game.setHomeScore(line.getScoreAgainst());
				game.setAwayScore(line.getScoreFor());
				gamePlayed = true;
			}
		}
		sched.setGameDate(line.getGameDate());
		
		if (!db.insertScheduledGame(sched)) {
			return false;
		}
		sched = db.findScheduledGame(sched.getGameDate(), sched.getHomeTeam().getNcaaTeamId(), sched.getAwayTeam().getNcaaTeamId());
		
		if (gamePlayed) {
			sched.setResult(game);
			if (db.updateScheduledGame(sched) < 0) {
				return false;
			}
			game.setScheduledGame(sched);
			if (db.insertGameResult(game)) {
				System.out.println("Inserted " + sched.getAwayTeam().getNcaaTeamName()
						+ " " + game.getAwayScore() + " at " + sched.getHomeTeam().getNcaaTeamName()
						+ " " + game.getHomeScore() + " on " + sched.getGameDate());
				gamesInserted += 1;
			} else {
				System.out.println(sched.getAwayTeam().getNcaaTeamName()
						+ " " + game.getAwayScore() + " at " + sched.getHomeTeam().getNcaaTeamName()
						+ " " + game.getHomeScore() + " on " + sched.getGameDate()
						+ " already in database");
			}
		} 
		
		return true;
	}

}
