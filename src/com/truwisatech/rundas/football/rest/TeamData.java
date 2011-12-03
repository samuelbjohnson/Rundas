package com.truwisatech.rundas.football.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/teams")
public class TeamData {
	@GET
	@Path("/static")
	public String staticString() {
		return "static rest string";
	}
}
