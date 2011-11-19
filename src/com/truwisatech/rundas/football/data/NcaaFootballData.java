package com.truwisatech.rundas.football.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.Game;

public class NcaaFootballData implements NcaaFootballDao {
	private ConnectionFactory connectionFactory;
	private Connection conn;
	
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	public NcaaFootballData(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
		conn = this.connectionFactory.buildConnection();
	}
	
	@Override
	public NcaaTeam findTeam(int teamId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.NcaaTeam WHERE ncaaTeamId=?"
			);
			statement.setInt(1, teamId);
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			NcaaTeam t = new NcaaTeam();
			
			t.setNcaaTeamId(resultSet.getInt("ncaaTeamId"));
			t.setNcaaTeamName(resultSet.getString("ncaaTeamName"));
			
			if (resultSet.next()) {
				throw new FootballDataException("More than one team for ID: " + teamId);
			}
			
			return t;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				resultSet.close();
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public NcaaTeam[] findTeams() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.NcaaTeam"
			);
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			List<NcaaTeam> list = new LinkedList<NcaaTeam>();
			
			while (resultSet.next()) {
				NcaaTeam t = new NcaaTeam();
				
				t.setNcaaTeamId(resultSet.getInt("ncaaTeamId"));
				t.setNcaaTeamName(resultSet.getString("ncaaTeamName"));
				
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
		finally {
			try {
				resultSet.close();
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public boolean insertTeam(NcaaTeam team) {
		PreparedStatement statement = null;
		if(team == null || team.getNcaaTeamId() < 0 || findTeam(team.getNcaaTeamId()) != null) {
			return false;
		}
		
		try {
			
			statement = conn.prepareStatement(
					"INSERT INTO Rundas.NcaaTeam(ncaaTeamId, ncaaTeamName) VALUES(?, ?)"
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
		finally {
			try {
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		
		return true;
	}
	
	@Override
	public Game findGame(Game game) {
		if (game.getGameId() < 1) {
			return findGame(game.getGameDate(), game.getHomeTeam().getNcaaTeamId(), game.getAwayTeam().getNcaaTeamId());
		}
		int gameId = game.getGameId();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.Games WHERE gameId=?"
			);
			statement.setInt(1, gameId);
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			game.setGameId(resultSet.getInt("gameId"));
			game.setHomeTeam(findTeam(resultSet.getInt("homeTeamId")));
			game.setAwayTeam(findTeam(resultSet.getInt("awayTeamId")));
			game.setGameDate(resultSet.getDate("gameDate"));
			game.setHomeScore(resultSet.getInt("homeScore"));
			game.setAwayScore(resultSet.getInt("awayScore"));
			
			if (resultSet.next()) {
				throw new FootballDataException("Multiple games for id: " + gameId);
			}
			
			return game;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				resultSet.close();
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public Game findGame(Date gameDate, int homeId, int awayId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.Games WHERE gameDate=? AND awayTeamId=? AND homeTeamId=?"
			);
			statement.setDate(1, new java.sql.Date(gameDate.getTime()));
			statement.setInt(2, awayId);
			statement.setInt(3, homeId);
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			Game game = new Game();
			game.setGameId(resultSet.getInt("gameId"));
			game.setHomeTeam(findTeam(resultSet.getInt("homeTeamId")));
			game.setAwayTeam(findTeam(resultSet.getInt("awayTeamId")));
			game.setGameDate(resultSet.getDate("gameDate"));
			game.setHomeScore(resultSet.getInt("homeScore"));
			game.setAwayScore(resultSet.getInt("awayScore"));
			
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
		finally {
			try {
				resultSet.close();
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public Game[] findTeamGames(int teamId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.Games WHERE awayTeamId=? OR homeTeamId=?"
			);
			statement.setInt(1, teamId);
			statement.setInt(2, teamId);
			
			resultSet = statement.executeQuery();
			
			ArrayList<Game> list = new ArrayList<Game>();
			while (resultSet.next()) {
				Game game = new Game();
				game.setGameId(resultSet.getInt("gameId"));
				game.setHomeTeam(findTeam(resultSet.getInt("homeTeamId")));
				game.setAwayTeam(findTeam(resultSet.getInt("awayTeamId")));
				game.setGameDate(resultSet.getDate("gameDate"));
				game.setHomeScore(resultSet.getInt("homeScore"));
				game.setAwayScore(resultSet.getInt("awayScore"));
				
				list.add(game);
			}
			
			if (list.size() == 0) {
				return null;
			} else {
				return list.toArray(new Game[0]);
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				resultSet.close();
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public boolean insertGame(Game game) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		if(game.getGameDate() == null || findGame(game) != null) {
			return false;
		}
		
		try {
			statement = conn.prepareStatement(
					"INSERT INTO Rundas.Games (gameDate, homeTeamId, awayTeamId, homeScore, awayScore) " +
					"VALUES(?, ?, ?, ?, ?)"
			, Statement.RETURN_GENERATED_KEYS);

			statement.setDate(1, new java.sql.Date(game.getGameDate().getTime()));
			statement.setInt(2, game.getHomeTeam().getNcaaTeamId());
			statement.setInt(3, game.getAwayTeam().getNcaaTeamId());
			statement.setInt(4, game.getHomeScore());
			statement.setInt(5, game.getAwayScore());
			
			int result = statement.executeUpdate();
			if (result != 1) {
				throw new FootballDataException("Error inserting game between: " + game.getAwayTeam().getNcaaTeamId() + " and " + 
						game.getHomeTeam().getNcaaTeamId() + " on " + game.getGameDate());
			}
			resultSet = statement.getGeneratedKeys();
			if (!resultSet.next()) {
				return false;
				//TODO need to roll back the transaction--this should never happen
			}
			game.setGameId(resultSet.getInt(1));
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				resultSet.close();
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return true;
	}
	
	@Override
	public int updateGame(Game game) {
		PreparedStatement statement = null;
		if (game.getGameId() < 0) {
			return -1;
		}
		try {
			statement = conn.prepareStatement(
					"UPDATE Rundas.Games SET gameDate=?, awayTeamId=?, homeTeamId=?, awayScore=?, homeScore=? " +
					"WHERE gameId=?"
			);
			statement.setDate(1, new java.sql.Date(game.getGameDate().getTime()));
			statement.setInt(2, game.getAwayTeam().getNcaaTeamId());
			statement.setInt(3, game.getHomeTeam().getNcaaTeamId());
			statement.setInt(4, game.getAwayScore());
			statement.setInt(5, game.getHomeScore());
			statement.setInt(6, game.getGameId());
			
			int result = statement.executeUpdate();
			return result;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return -1;
	}
	
	@Override
	public Game[] findTeamResults(int teamId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.Games WHERE " +
					"(awayTeamId=? OR homeTeamId=?) AND " +
					"(awayScore>-1 AND homeScore>-1)"
			);
			statement.setInt(1, teamId);
			statement.setInt(2, teamId);
			
			resultSet = statement.executeQuery();
			
			ArrayList<Game> list = new ArrayList<Game>();
			while (resultSet.next()) {
				Game game = new Game();
				game.setGameId(resultSet.getInt("gameId"));
				game.setHomeTeam(findTeam(resultSet.getInt("homeTeamId")));
				game.setAwayTeam(findTeam(resultSet.getInt("awayTeamId")));
				game.setGameDate(resultSet.getDate("gameDate"));
				game.setHomeScore(resultSet.getInt("homeScore"));
				game.setAwayScore(resultSet.getInt("awayScore"));
				
				list.add(game);
			}
			
			if (list.size() == 0) {
				return null;
			} else {
				return list.toArray(new Game[0]);
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				resultSet.close();
				statement.close();
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	private void sqlError(SQLException s) {
		System.err.println("Sql issue: ");
		System.err.println(s.getMessage());
		throw new FootballDataException("SQL issue: " + s.getMessage());
	}

}
