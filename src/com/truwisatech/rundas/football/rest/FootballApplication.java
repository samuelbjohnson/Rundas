package com.truwisatech.rundas.football.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class FootballApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	
	public FootballApplication() {
		singletons.add(new TeamDataService());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
