package com.truwisatech.rundas.football.data;

import java.util.Date;

import com.truwisatech.rundas.football.data.beans.NcaaPlayer;
import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.Game;
import com.truwisatech.rundas.football.data.beans.PlayerStats;

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
	
	public NcaaPlayer findPlayer(int playerId);
	public NcaaPlayer findPlayer(int teamId, String uniformNum);
	public NcaaPlayer[] findPlayersForTeam(int teamId);
	
	public boolean insertPlayer(NcaaPlayer player);
	public int updatePlayer(NcaaPlayer player);
	
	public PlayerStats findPlayerStats(int playerId, Date gameDate);
	public PlayerStats[] findPlayerStats(int playerId);
	
	public boolean insertPlayerStats(PlayerStats stats);
	public int updatePlayerStats(PlayerStats stats);
}
