package com.truwisatech.rundas.football.rest;

import static com.truwisatech.rundas.football.rest.JsonConverters.*;

import java.util.Arrays;
import java.util.LinkedList;

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
	
	@GET
	@Path("findConferenceTeams")
	@Produces("application/json")
	//TODO add parameter for team or conference id--right now, default to Big Ten
	public StreamingOutput findConferenceTeams() {
		int[] teamIds = {539, 518, 418, 416, 428, 306, 301, 559, 509, 312, 463, 796};
		LinkedList<NcaaTeam> teams = new LinkedList<NcaaTeam>();
		for(int id : teamIds) {
			teams.add(db.findTeam(id));
		}
		return buildOutput(convertToJson(teams.toArray(new NcaaTeam[0])));
	}
}
