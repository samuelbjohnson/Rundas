package com.truwisatech.rundas.football.data.beans;

import static com.truwisatech.rundas.football.Constants.DATE_FORMAT;

public class TeamStats extends PlayerStats {
	private NcaaTeam opponent;
	private Game game;
	private boolean atHome;
	private int teamId;
	
	public TeamStats(int teamId) {
		this.teamId = teamId;
	}
	
	public NcaaTeam getOpponent() {
		return opponent;
	}
	
	public void setOpponent(NcaaTeam opponent) {
		this.opponent = opponent;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGame(Game game) {
		this.game = game;
		if (teamId == game.getHomeTeam().getNcaaTeamId()) {
			atHome = true;
		}
	}
	
	public String toJson() {
		StringBuffer b = new StringBuffer();
		b.append("{\"gameDate\":\"" + DATE_FORMAT.format(game.getGameDate()) + "\", ");
		b.append("\"opponentId\":\"" + opponent.getNcaaTeamId() + "\", ");
		b.append("\"opponentName\":\"" + (atHome ? "" : "@") + opponent.getNcaaTeamName() + "\", ");
		b.append("\"atHome\":\"" + (atHome ? "true" : "false") + "\", ");
		b.append("\"awayScore\":\"" + game.getAwayScore() + "\", ");
		b.append("\"homeScore\":\"" + game.getHomeScore() + "\", ");
		b.append("\"rushingYards\":\"" + getNetRushingYards() + "\", ");
		b.append("\"passingYards\":\"" + getTotalPassingYards() + "\", ");
		b.append("\"receivingYards\":\"" + getTotalReceivingYards() + "\"");
		b.append("}");
		return b.toString();
	}
}
