package com.truwisatech.rundas.football.data.beans;

import java.util.Date;

public class PlayerStats {
	private int playerId;
	private Date gameDate;
	private int netRushingYards;
	private int totalPassingYards;
	private int totalReceivingYards;
	
	@Override
	public String toString() {
		return "PlayerStats [playerId=" + playerId + ", gameDate=" + gameDate
				+ "]";
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public Date getGameDate() {
		return gameDate;
	}
	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}
	public int getNetRushingYards() {
		return netRushingYards;
	}
	public void setNetRushingYards(int netRushingYards) {
		this.netRushingYards = netRushingYards;
	}
	public int getTotalPassingYards() {
		return totalPassingYards;
	}
	public void setTotalPassingYards(int totalPassingYards) {
		this.totalPassingYards = totalPassingYards;
	}
	public int getTotalReceivingYards() {
		return totalReceivingYards;
	}
	public void setTotalReceivingYards(int totalReceivingYards) {
		this.totalReceivingYards = totalReceivingYards;
	}
}
