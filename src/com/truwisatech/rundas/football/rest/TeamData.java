package com.truwisatech.rundas.football.rest;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.StreamingOutput;

import com.truwisatech.rundas.football.data.NcaaFootballDao;
import com.truwisatech.rundas.football.data.NcaaFootballData;
import com.truwisatech.rundas.football.data.RundasMySqlConnectionFactory;
import com.truwisatech.rundas.football.data.beans.NcaaTeam;

@Path("/teams")
public class TeamData {
	private NcaaFootballDao db = new NcaaFootballData(new RundasMySqlConnectionFactory());
	
	@GET
	@Path("/static")
	public String staticString() {
		return "static rest string";
	}
	
	@GET
	@Path("/find")
	@Produces("application/json")
	public StreamingOutput findTeams(@QueryParam("id") int teamId) {
		if (teamId < 1) {
			NcaaTeam[] allTeams = db.findTeams();
			return convertTeamToJson(allTeams);
		} else {
			NcaaTeam[] oneTeam = { db.findTeam(teamId) };
			return convertTeamToJson(oneTeam);
		}
	}
	
	private StreamingOutput convertTeamToJson(NcaaTeam[] teams) {
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
		final String json = b.toString();
		return new StreamingOutput() {
			public void write(OutputStream os) throws IOException {
				os.write(json.getBytes());
			}
		};
	}
}
