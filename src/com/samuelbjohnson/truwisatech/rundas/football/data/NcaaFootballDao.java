package com.samuelbjohnson.truwisatech.rundas.football.data;

import java.util.Date;
import com.samuelbjohnson.truwisatech.rundas.football.data.beans.GameResult;
import com.samuelbjohnson.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.samuelbjohnson.truwisatech.rundas.football.data.beans.ScheduledGame;
import com.samuelbjohnson.truwisatech.rundas.football.data.beans.Team;

public interface NcaaFootballDao {
	public NcaaTeam findTeam(int teamId);
	public NcaaTeam[] findTeams();
	
	public boolean insertTeam(NcaaTeam team);
	
	public ScheduledGame findScheduledGame(int gameId);
	public ScheduledGame findScheduledGame(Date gameDate, int homeId, int awayId);
	
	public ScheduledGame[] findScheduledGames(int teamId);
	
	public boolean insertScheduledGame(ScheduledGame game);
	
	public GameResult findGame(int gameId);
	public GameResult[] findTeamResults(int teamId);
	
	public boolean insertGameResult(GameResult game);
}
