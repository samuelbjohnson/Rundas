package com.truwisatech.rundas.football.rest;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.StreamingOutput;

import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.TeamStats;

public class JsonConverters {
	public static String convertToJson(NcaaTeam[] teams) {
		StringBuffer b = new StringBuffer();
		if (teams.length == 1) {
			b.append(teams[0].toJson());
		} else if (teams.length > 1) {
			boolean first = true;
			b.append("[");
			for(NcaaTeam t : teams) {
				if (first) {
					first = false;
				} else {
					b.append(", ");
				}
				b.append(t.toJson());
			}
			b.append("]");
		}
		return b.toString();
	}
	
	public static String convertToJson(TeamStats[] stats) {
		StringBuffer b = new StringBuffer();
		if (stats.length == 1) {
			b.append(stats[0].toJson());
		} else if (stats.length > 1) {
			boolean first = true;
			b.append("[");
			for(TeamStats s : stats) {
				if (first) {
					first = false;
				} else {
					b.append(", ");
				}
				b.append(s.toJson());
			}
			b.append("]");
		}
		return b.toString();
	}
	
	public static StreamingOutput buildOutput(final String s) {
		return new StreamingOutput() {
			public void write(OutputStream os) throws IOException {
				os.write(s.getBytes());
			}
		};
	}
}
