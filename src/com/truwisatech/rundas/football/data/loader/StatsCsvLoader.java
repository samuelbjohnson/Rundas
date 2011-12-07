package com.truwisatech.rundas.football.data.loader;

import java.io.File;

import com.truwisatech.rundas.football.data.FootballDataException;
import com.truwisatech.rundas.football.data.beans.NcaaPlayer;
import com.truwisatech.rundas.football.data.beans.PlayerStats;

public class StatsCsvLoader extends GenericCsvLoader {

	public StatsCsvLoader(String fileName) {
		super(new File(fileName), 2);
	}

	@Override
	protected GenericCsvFileLine getFileLineImpl(String line) {
		return new StatsCsvFileLine(line);
	}

	@Override
	protected boolean processFileLine(GenericCsvFileLine fileLine) {
		StatsCsvFileLine line = (StatsCsvFileLine) fileLine;
		
		if (! line.hasUsefulStats()) {
			//no point in storing an empty row for someone who didn't accumulate any of the measured stats
			return false;
		}
		
		NcaaPlayer p = db.findPlayer(line.getTeamId(), line.getUniformNum());
		if (p == null) {
			System.out.println("No player found for team: " + line.getTeamId() + ", uniform number; " + 
					line.getUniformNum() + ". Could not insert stats");
			return false;
		}
		
		PlayerStats stats = new PlayerStats();
		stats.setPlayerId(p.getPlayerId());
		stats.setGameDate(line.getGameDate());
		stats.setNetRushingYards(line.getNetRushingYards());
		stats.setTotalPassingYards(line.getTotalPassingYards());
		stats.setTotalReceivingYards(line.getTotalReceivingYards());
		
		if (db.findPlayerStats(p.getPlayerId(), line.getGameDate()) == null) {
			if (db.insertPlayerStats(stats)) {
				System.out.println("Inserted " + stats);
			} else {
				throw new CsvFileFormatException("Error inserting player stats " + stats);
			}
		} else {
			if (db.updatePlayerStats(stats) != 1) {
				System.out.println("Updated " + stats);
			} else {
				throw new CsvFileFormatException("Error updating player stats " + stats);
			}
		}
		return true;
	}

}
