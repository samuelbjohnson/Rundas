package com.truwisatech.rundas.football.data.loader;

import java.io.File;

import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.Game;

public class ScheduleCsvLoader extends GenericCsvLoader {
	
	public ScheduleCsvLoader(String file) {
		super(new File(file), 1);
	}
	
	@Override
	protected GenericCsvFileLine getFileLineImpl(String line) {
		return new ScheduleCsvFileLine(line);
	}
	
	@Override
	protected boolean processFileLine(GenericCsvFileLine fileLine) {
		ScheduleCsvFileLine line = (ScheduleCsvFileLine) fileLine;
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
			} else {
				return false;
			}
		} else {
			game.setGameId(existingGame.getGameId());
			if (db.updateGame(game) >= 1) {
				System.out.println("Updated " + game.getAwayTeam().getNcaaTeamName()
						+ " " + game.getAwayScore() + " at " + game.getHomeTeam().getNcaaTeamName()
						+ " " + game.getHomeScore() + " on " + game.getGameDate());
			} else {
				return false;
			}
		} 
		
		return true;
	}

}
