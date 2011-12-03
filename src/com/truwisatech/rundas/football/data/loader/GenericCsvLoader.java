package com.truwisatech.rundas.football.data.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.truwisatech.rundas.football.data.NcaaFootballDao;
import com.truwisatech.rundas.football.data.NcaaFootballData;
import com.truwisatech.rundas.football.data.RundasMySqlConnectionFactory;

public abstract class GenericCsvLoader {
	protected NcaaFootballDao db = new NcaaFootballData(new RundasMySqlConnectionFactory());
	
	private File input;
	
	public GenericCsvLoader(File file) {
		input = file;
	}
	
	public void processFile() {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String line = reader.readLine(); //First line is just headings
			int lineNumber = 0;
			while ((line = reader.readLine()) != null) {
				lineNumber += 1;
				System.out.print("Line: " + lineNumber + " ");
				GenericCsvFileLine fileLine = getFileLineImpl(line);
				if (! processFileLine(fileLine)) {
					System.err.println("Line: " + line + " didn't process");
				}
				
			}
			
			System.out.println("\nComplete\nTotal Lines: " + lineNumber);
			
		}
		catch (IOException i) {
			System.err.print("Problem with file ");
			System.err.println(i.getMessage());
			throw new RuntimeException(i);
		}

	}
	
	protected abstract GenericCsvFileLine getFileLineImpl(String line);
	
	protected abstract boolean processFileLine(GenericCsvFileLine fileLine);
}
