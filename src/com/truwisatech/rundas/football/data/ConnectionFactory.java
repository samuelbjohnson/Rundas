package com.truwisatech.rundas.football.data;

import java.sql.Connection;

public interface ConnectionFactory {
	public Connection buildConnection();
}
