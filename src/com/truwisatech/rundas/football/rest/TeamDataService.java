package com.truwisatech.rundas.football.rest;

import static com.truwisatech.rundas.football.rest.JsonConverters.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.StreamingOutput;

import com.truwisatech.rundas.football.data.NcaaFootballDao;
import com.truwisatech.rundas.football.data.NcaaFootballData;
import com.truwisatech.rundas.football.data.RundasMySqlConnectionFactory;
import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.TeamStats;

@Path("/teams")
public class TeamDataService {
	private NcaaFootballDao db = new NcaaFootballData(new RundasMySqlConnectionFactory());
	
	@GET
	@Path("/static")
	public String staticString() {
		return "static rest string";
	}
	
	@GET
	@Path("/findTeams")
	@Produces("application/json")
	public StreamingOutput findTeams(@QueryParam("id") int teamId) {
		if (teamId < 1) {
			NcaaTeam[] allTeams = db.findTeams();
			return buildOutput(convertToJson(allTeams));
		} else {
			NcaaTeam[] oneTeam = { db.findTeam(teamId) };
			return buildOutput(convertToJson(oneTeam));
		}
	}
	
	@GET
	@Path("/findStats")
	@Produces("application/json")
	public StreamingOutput findTeamStats(@QueryParam("id") int teamId) {
		if (teamId < 1) {
			return buildOutput("{}");
		} else {
			TeamStats[] stats = db.findSeasonTeamStats(teamId);
			return buildOutput(convertToJson(stats));
		}
	}
}
