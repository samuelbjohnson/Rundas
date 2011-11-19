package com.truwisatech.rundas.football.data;

import java.util.Date;

import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.Game;

public interface NcaaFootballDao {
	public NcaaTeam findTeam(int teamId);
	public NcaaTeam[] findTeams();
	
	public boolean insertTeam(NcaaTeam team);
	
	public Game findGame(Game game);
	public Game findGame(Date gameDate, int homeId, int awayId);
	
	public Game[] findTeamGames(int teamId);
	
	public boolean insertGame(Game game);
	public int updateGame(Game game);
	
	public Game[] findTeamResults(int teamId);
}
