package com.truwisatech.rundas.football.data.loader;

public abstract class GenericCsvFileLine {
	protected String line;
	
	public GenericCsvFileLine(String line) {
		this.line = line;
		parseLine();
	}
	
	protected abstract void parseLine();

}
