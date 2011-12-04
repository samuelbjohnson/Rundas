package com.truwisatech.rundas.football.data.loader;

import java.text.SimpleDateFormat;

public abstract class GenericCsvFileLine {
	protected String line;
	
	protected static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	
	protected String[] parsedInputs;
	
	public GenericCsvFileLine(String line) {
		this.line = line;
		parseLine();
		parseInputs();
	}
	
	protected abstract void parseInputs();
	
	private void parseLine() {
		if (line.length() < 2) {
			parsedInputs = new String[0];
			return;
		}
		
		//remove opening quote
		line = line.substring(1);
		
		//remove ending quote
		line = line.substring(0, line.length() - 1);
		
		parsedInputs = line.split("\",\"");
	}
	
	protected void removeQuotes(String[] parsedInputs) {
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
	}

}
