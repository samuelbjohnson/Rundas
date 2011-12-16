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

import com.truwisatech.rundas.football.data.beans.NcaaPlayer;
import com.truwisatech.rundas.football.data.beans.NcaaTeam;
import com.truwisatech.rundas.football.data.beans.Game;
import com.truwisatech.rundas.football.data.beans.PlayerStats;
import com.truwisatech.rundas.football.data.beans.TeamStats;

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
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
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
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
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
				if (statement != null) {
					statement.close();
				}
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
			return findGame(game.getGameDate(), game.getHomeTeam().getNcaaTeamId());
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
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
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
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public Game findGame(Date gameDate, int teamId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.Games WHERE gameDate=? AND (awayTeamId=? OR homeTeamId=?)"
			);
			statement.setDate(1, new java.sql.Date(gameDate.getTime()));
			statement.setInt(2, teamId);
			statement.setInt(3, teamId);
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
						"Multiple games for team: " + teamId + " on " + gameDate);
			}
			
			return game;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
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
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
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
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
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
				if (statement != null) {
					statement.close();
				}
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
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public NcaaPlayer findPlayer(int playerId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.NcaaPlayer WHERE ncaaId=?"
			);
			statement.setInt(1, playerId);
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			NcaaPlayer p = parsePlayerResult(resultSet);
			
			if (resultSet.next()) {
				throw new FootballDataException("More than one player for ID: " + playerId);
			}
			
			return p;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	protected NcaaPlayer parsePlayerResult(ResultSet resultSet) throws SQLException {
		NcaaPlayer p = new NcaaPlayer();
		
		p.setFirstName(resultSet.getString("firstName"));
		p.setLastName(resultSet.getString("lastName"));
		p.setPlayerId(resultSet.getInt("ncaaId"));
		p.setPosition(resultSet.getString("position"));
		p.setTeamId(resultSet.getInt("teamId"));
		p.setUniformNum(resultSet.getString("uniformNumber"));
		p.setYear(resultSet.getString("year"));
		
		return p;
	}
	
	@Override
	public NcaaPlayer findPlayer(int teamId, String uniformNum) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.NcaaPlayer WHERE teamId=? AND uniformNumber=?"
			);
			statement.setInt(1, teamId);
			statement.setString(2, uniformNum);
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			NcaaPlayer p = parsePlayerResult(resultSet);
			
			if (resultSet.next()) {
				throw new FootballDataException("More than one player for team: " + teamId + ", uniform number: " + uniformNum);
			}
			
			return p;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	@Override
	public NcaaPlayer[] findPlayersForTeam(int teamId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.NcaaPlayer WHERE teamId=?"
			);
			statement.setInt(1, teamId);
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			List<NcaaPlayer> players = new LinkedList<NcaaPlayer>();
			
			while (resultSet.next()) {
				players.add(parsePlayerResult(resultSet));
			}
			
			if (players.size() == 0) {
				return null;
			} else {
				return players.toArray(new NcaaPlayer[0]);
			}
			
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	public boolean insertPlayer(NcaaPlayer player) {
		PreparedStatement statement = null;
		if(player == null || player.getTeamId() < 0 || findPlayer(player.getPlayerId()) != null) {
			return false;
		}
		
		try {
			
			statement = conn.prepareStatement(
					"INSERT INTO Rundas.NcaaPlayer(teamId, uniformNumber, lastName, firstName, position, year, ncaaId) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?)"
			);
			statement.setInt(1, player.getTeamId());
			statement.setString(2, player.getUniformNum());
			statement.setString(3, player.getLastName());
			statement.setString(4, player.getFirstName());
			statement.setString(5, player.getPosition());
			statement.setString(6, player.getYear());
			statement.setInt(7, player.getPlayerId());
			
			int result = statement.executeUpdate();
			if (result != 1) {
				throw new FootballDataException("Error on insert of player: " + player);
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		
		return true;
	}
	public int updatePlayer(NcaaPlayer player) {
		PreparedStatement statement = null;
		if (player.getPlayerId() < 0) {
			return -1;
		}
		try {
			statement = conn.prepareStatement(
					"UPDATE Rundas.NcaaPlayer SET year=?, position=? " +
					"WHERE ncaaId=?"
			);
			statement.setString(1, player.getYear());
			statement.setString(2, player.getPosition());
			statement.setInt(3, player.getPlayerId());
			
			int result = statement.executeUpdate();
			return result;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return -1;
	}
	
	public PlayerStats findPlayerStats(int playerId, Date gameDate) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.PlayerStats WHERE playerId=? AND gameDate=?"
			);
			statement.setInt(1, playerId);
			statement.setDate(2, new java.sql.Date(gameDate.getTime()));
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			PlayerStats p = parsePlayerStatsResult(resultSet);
			
			if (resultSet.next()) {
				throw new FootballDataException("More than one set of stats for player: " + playerId + ", on game date: " + 
						gameDate);
			}
			
			return p;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	protected PlayerStats parsePlayerStatsResult(ResultSet set) throws SQLException {
		PlayerStats p = new PlayerStats();
		
		p.setPlayerId(set.getInt("playerId"));
		p.setGameDate(set.getDate("gameDate"));
		p.setNetRushingYards(set.getInt("netRushingYards"));
		p.setTotalPassingYards(set.getInt("totalPassingYards"));
		p.setTotalReceivingYards(set.getInt("totalReceivingYards"));
		
		return p;
	}
	
	public PlayerStats[] findPlayerStats(int playerId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT * FROM Rundas.PlayerStats WHERE playerId=?"
			);
			statement.setInt(1, playerId);
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			List<PlayerStats> stats = new LinkedList<PlayerStats>();
			
			while (resultSet.next()) {
				stats.add(parsePlayerStatsResult(resultSet));
			}
			
			if (stats.size() == 0) {
				return null;
			} else {
				return stats.toArray(new PlayerStats[0]);
			}
			
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	public boolean insertPlayerStats(PlayerStats stats) {
		PreparedStatement statement = null;
		if(stats == null || stats.getPlayerId() < 0 || stats.getGameDate() == null || 
				findPlayerStats(stats.getPlayerId(), stats.getGameDate()) != null) {
			return false;
		}
		
		try {
			
			statement = conn.prepareStatement(
					"INSERT INTO Rundas.PlayerStats(playerId, gameDate, netRushingYards, totalPassingYards, totalReceivingYards)" +
					"VALUES(?, ?, ?, ?, ?)"
			);
			statement.setInt(1, stats.getPlayerId());
			statement.setDate(2, new java.sql.Date(stats.getGameDate().getTime()));
			statement.setInt(3, stats.getNetRushingYards());
			statement.setInt(4, stats.getTotalPassingYards());
			statement.setInt(5, stats.getTotalReceivingYards());
			
			int result = statement.executeUpdate();
			if (result != 1) {
				throw new FootballDataException("Error on insert of player stats: " + stats);
			}
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		
		return true;
	}
	
	public int updatePlayerStats(PlayerStats stats) {
		PreparedStatement statement = null;
		if (stats.getPlayerId() < 0) {
			return -1;
		}
		try {
			statement = conn.prepareStatement(
					"UPDATE Rundas.PlayerStats SET netRushingYards=?, totalPassingYards=?, totalReceivingYards=? " +
					"WHERE playerId=? AND gameDate=?"
			);
			statement.setInt(1, stats.getNetRushingYards());
			statement.setInt(2, stats.getTotalPassingYards());
			statement.setInt(3, stats.getTotalReceivingYards());
			statement.setInt(4, stats.getPlayerId());
			statement.setDate(5, new java.sql.Date(stats.getGameDate().getTime()));
			
			int result = statement.executeUpdate();
			return result;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return -1;
	}
	
	public TeamStats findTeamStats(int teamId, Date gameDate) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT gameDate, SUM( netRushingYards ) AS netRushingYards, SUM( totalPassingYards ) AS totalPassingYards, " +
					"sum( totalReceivingYards) as totalReceivingYards FROM PlayerStats WHERE playerId IN (" +
						"SELECT ncaaId FROM NcaaPlayer WHERE teamId=?" +
					") AND gameDate=?"
			);
			statement.setInt(1, teamId);
			statement.setDate(2, new java.sql.Date(gameDate.getTime()));
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			TeamStats t = new TeamStats(teamId);
			parseTeamStatsResult(resultSet, t);
			
			Game g = findGame(gameDate, teamId);
			t.setGame(g);
			t.setOpponent(teamId == g.getHomeTeam().getNcaaTeamId() ? 
					findTeam(g.getAwayTeam().getNcaaTeamId()) : findTeam(g.getHomeTeam().getNcaaTeamId()));
			
			if (resultSet.next()) {
				throw new FootballDataException("More than one set of stats for teamId: " + teamId + ", on game date: " + 
						gameDate);
			}
			
			return t;
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException s) {
				sqlError(s);
			}
		}
		return null;
	}
	
	protected void parseTeamStatsResult(ResultSet set, TeamStats t) throws SQLException {
		t.setGameDate(set.getDate("gameDate"));
		t.setNetRushingYards(set.getInt("netRushingYards"));
		t.setTotalPassingYards(set.getInt("totalPassingYards"));
		t.setTotalReceivingYards(set.getInt("totalReceivingYards"));
	}
	
	public TeamStats[] findSeasonTeamStats(int teamId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT gameDate, SUM( netRushingYards ) AS netRushingYards, SUM( totalPassingYards ) AS totalPassingYards, " +
							"sum( totalReceivingYards) as totalReceivingYards FROM PlayerStats WHERE playerId IN (" +
								"SELECT ncaaId FROM NcaaPlayer WHERE teamId=?" +
							") group by gameDate"
			);
			statement.setInt(1, teamId);
			
			resultSet = statement.executeQuery();
			
			if (! resultSet.next()) {
				return null;
			}
			
			List<TeamStats> stats = new LinkedList<TeamStats>();
			
			while (resultSet.next()) {
				TeamStats t = new TeamStats(teamId);
				parseTeamStatsResult(resultSet, t);
				Game g = findGame(t.getGameDate(), teamId);
				t.setGame(g);
				t.setOpponent(teamId == g.getHomeTeam().getNcaaTeamId() ? 
						findTeam(g.getAwayTeam().getNcaaTeamId()) : findTeam(g.getHomeTeam().getNcaaTeamId()));
				
				stats.add(t);
			}
			
			if (stats.size() == 0) {
				return null;
			} else {
				return stats.toArray(new TeamStats[0]);
			}
			
		}
		catch (SQLException s) {
			sqlError(s);
		}
		finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
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
