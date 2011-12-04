package com.truwisatech.rundas.football.data.loader;

import java.io.File;

import com.truwisatech.rundas.football.data.beans.NcaaPlayer;

public class RosterCsvLoader extends GenericCsvLoader{

	public RosterCsvLoader(String fileName) {
		super(new File(fileName), 2);
	}
	
	@Override
	protected GenericCsvFileLine getFileLineImpl(String line) {
		return new RosterCsvFileLine(line);
	}
	
	@Override
	protected boolean processFileLine(GenericCsvFileLine fileLine) {
		RosterCsvFileLine line = (RosterCsvFileLine) fileLine;
		System.out.println("Processing " + line.getTeamName() + ", #" + line.getUniformNum());
		
		if (line.getUniformNum().equals("19") && line.getLastName().equals("Team")) {
			return false;
		}
		
		NcaaPlayer p = new NcaaPlayer();
		
		p.setTeamId(line.getTeamId());
		p.setFirstName(line.getFirstName());
		p.setLastName(line.getLastName());
		p.setPlayerId(line.getPlayerId());
		p.setPosition(line.getPosition());
		p.setUniformNum(line.getUniformNum());
		p.setYear(line.getYear());
		
		NcaaPlayer existing = db.findPlayer(p.getTeamId(), p.getUniformNum());
		if (existing == null) {
			if (db.insertPlayer(p)) {
				System.out.println("Inserted " + p);
			} else {
				throw new CsvFileFormatException("Error inserting player " + p);
			}
		} else {
			if (db.updatePlayer(p) != 1) {
				System.out.println("Updated " + p);
			} else {
				throw new CsvFileFormatException("Error updating player " + p);
			}
		}
		
		return true;
	}


}
