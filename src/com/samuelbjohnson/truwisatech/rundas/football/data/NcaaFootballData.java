package com.samuelbjohnson.truwisatech.rundas.football.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.samuelbjohnson.truwisatech.rundas.football.data.beans.GameResult;
import com.samuelbjohnson.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.samuelbjohnson.truwisatech.rundas.football.data.beans.ScheduledGame;
import com.samuelbjohnson.truwisatech.rundas.football.data.beans.Team;

public class NcaaFootballData implements NcaaFootballDao {
	private ConnectionFactory connectionFactory;
	private Connection conn;
	
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	public NcaaFootballData() {
		conn = connectionFactory.buildConnection();
	}
	
	@Override
	public NcaaTeam findTeam(int teamId) {
			
		try {
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM rundas.NcaaTeam WHERE ncaaTeamId=?"
			);
			statement.setInt(1, teamId);
			
			ResultSet resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			NcaaTeam t = new NcaaTeam();
			
			t.setNcaaTeamId(resultSet.getInt(1));
			t.setNcaaTeamName(resultSet.getString(2));
			
			if (resultSet.next()) {
				throw new FootballDataException("More than one team for ID: " + teamId);
			}
			
			return t;
		}
		catch (SQLException s) {
			sqlError(s);
		}		
		return null;
	}
	
	@Override
	public NcaaTeam[] findTeams() {
		try {
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM rundas.NcaaTeam"
			);
			
			ResultSet resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			List<NcaaTeam> list = new LinkedList<NcaaTeam>();
			
			while (resultSet.next()) {
				NcaaTeam t = new NcaaTeam();
				
				t.setNcaaTeamId(resultSet.getInt(1));
				t.setNcaaTeamName(resultSet.getString(2));
				
				list.add(t);
			}
			
			if (list.size() == 0) {
				return null;
			} else {
				return list.toArray(new NcaaTeam[0]);
			}
			
		}
		catch (SQLException s) {
			sqlError(s);
		}		
		return null;
	}
	
	@Override
	public boolean insertTeam(NcaaTeam team) {
		try {
			
			PreparedStatement statement = conn.prepareStatement(
					"INSERT INTO rundas.NcaaTeam VALUES(?, ?)"
			);
			statement.setInt(1, team.getNcaaTeamId());
			statement.setString(2, team.getNcaaTeamName());
			
			int result = statement.executeUpdate();
			if (result != 1) {
				throw new FootballDataException("Error on insert of team: " + team);
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		
		return true;
	}
	
	@Override
	public ScheduledGame findScheduledGame(int gameId) {
		try {
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM rundas.ScheduledGames WHERE gameId=?"
			);
			statement.setInt(1, gameId);
			ResultSet resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			ScheduledGame game = new ScheduledGame();
			game.setGameId(resultSet.getInt(1));
			game.setHomeTeam(findTeam(resultSet.getInt(4)));
			game.setAwayTeam(findTeam(resultSet.getInt(3)));
			game.setGameDate(resultSet.getDate(4));
			game.setResult(findGame(resultSet.getInt(5)));
			
			if (resultSet.next()) {
				throw new FootballDataException("Multiple games for id: " + gameId);
			}
			
			return game;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		
		return null;
	}
	
	@Override
	public ScheduledGame findScheduledGame(Date gameDate, int homeId, int awayId) {
		try {
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM rundas.ScheduledGames WHERE gameDate=? AND awayTeamId=? AND homeTeamId=?"
			);
			statement.setDate(1, new java.sql.Date(gameDate.getTime()));
			statement.setInt(2, awayId);
			statement.setInt(3, homeId);
			ResultSet resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			ScheduledGame game = new ScheduledGame();
			game.setGameId(resultSet.getInt(1));
			game.setHomeTeam(findTeam(resultSet.getInt(4)));
			game.setAwayTeam(findTeam(resultSet.getInt(3)));
			game.setGameDate(resultSet.getDate(4));
			game.setResult(findGame(resultSet.getInt(5)));
			
			if (resultSet.next()) {
				throw new FootballDataException(
						"Multiple games between: " + awayId + " and " + 
						homeId + " on " + gameDate);
			}
			
			return game;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		
		return null;
	}
	
	@Override
	public ScheduledGame[] findScheduledGames(int teamId) {
		try {
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM rundas.scheduledGames WHERE " +
					"awayTeamId=? OR homeTeamId=?"
			);
			statement.setInt(1, teamId);
			statement.setInt(2, teamId);
			
			ResultSet resultSet = statement.executeQuery();
			
			ArrayList<ScheduledGame> list = new ArrayList<ScheduledGame>();
			while (resultSet.next()) {
				ScheduledGame game = new ScheduledGame();
				game.setGameId(resultSet.getInt(1));
				game.setHomeTeam(findTeam(resultSet.getInt(4)));
				game.setAwayTeam(findTeam(resultSet.getInt(3)));
				game.setGameDate(resultSet.getDate(4));
				game.setResult(findGame(resultSet.getInt(5)));
				
				list.add(game);
			}
			
			if (list.size() == 0) {
				return null;
			} else {
				return list.toArray(new ScheduledGame[0]);
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		return null;
	}
	
	@Override
	public boolean insertScheduledGame(ScheduledGame game) {
		try {
			PreparedStatement statement = conn.prepareStatement(
					"INSERT INTO rundas.ScheduledGames (resultId, gameDate, awayTeamId, homeTeamId) " +
					"VALUES(?, ?, ?)"
			);
			statement.setInt(1, game.getResult().getId());
			statement.setDate(2, new java.sql.Date(game.getGameDate().getTime()));
			statement.setInt(3, game.getAwayTeam().getNcaaTeamId());
			statement.setInt(4, game.getHomeTeam().getNcaaTeamId());
			
			int result = statement.executeUpdate();
			if (result != 1) {
				throw new FootballDataException("Error inserting game between: " + game.getAwayTeam().getNcaaTeamId() + " and " + 
						game.getHomeTeam().getNcaaTeamId() + " on " + game.getGameDate());
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		
		return true;
	}
	
	@Override
	public GameResult findGame(int gameId) {
		try {
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM rundas.GameResults WHERE gameId = ?"
			);
			statement.setInt(1, gameId);
			
			ResultSet resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			GameResult game = new GameResult();
			game.setId(resultSet.getInt(1));
			game.setScheduledGame(findScheduledGame(resultSet.getInt(2)));
			game.setHomeScore(resultSet.getInt(3));
			game.setAwayScore(resultSet.getInt(4));
			
			
			if (resultSet.next()) {
				throw new FootballDataException("More than one game found for id: " + gameId);
			}
			
			return game;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		
		return null;
	}
	
	@Override
	public GameResult[] findTeamResults(int teamId) {
		try {
			PreparedStatement statement = conn.prepareStatement(
					"SELECT * FROM rundas.GameResults r WHERE r.Id in " +
					"SELECT resultId FROM rundas.ScheduledGames" +
					"WHERE resultId <> null AND (homeTeam = ? OR awayTeam = ?)"
			);
			statement.setInt(1, teamId);
			statement.setInt(2, teamId);
			
			ResultSet resultSet = statement.executeQuery();
			
			ArrayList<GameResult> list = new ArrayList<GameResult>();
			while(resultSet.next()) {
				GameResult game = new GameResult();
				game.setId(resultSet.getInt(1));
				game.setScheduledGame(findScheduledGame(resultSet.getInt(2)));
				game.setHomeScore(resultSet.getInt(3));
				game.setAwayScore(resultSet.getInt(4));
				
				list.add(game);
			}
			
			if (list.size() == 0) {
				return null;
			} else {
				return list.toArray(new GameResult[0]);
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		
		return null;
	}
	
	@Override
	public boolean insertGameResult(GameResult game) {
		if (game.getScheduledGame() == null) {
			return false;
		}
		if (findScheduledGame(game.getScheduledGame().getGameId()) == null) {
			if (!insertScheduledGame(game.getScheduledGame())) {
				return false;
			}
		}
		
		if (findGame(game.getId()) != null) {
			return false;
		}
		
		try {
			PreparedStatement statement = conn.prepareStatement(
					"INSERT INTO footballStats.results VALUES(?, ?, ?)"
			);
			statement.setInt(1, game.getGameId());
			statement.setInt(2, game.getAwayScore());
			statement.setInt(3, game.getHomeScore());
			
			int result = statement.executeUpdate();
			
			if (result != 1) {
				throw new FootballDataException("Error inserting game " + game.getGameId() + " into results table");
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		
		return true;
	}
	
	private void sqlError(SQLException s) {
		System.err.println("Sql issue: ");
		System.err.println(s.getMessage());
		System.exit(0);
	}

}
