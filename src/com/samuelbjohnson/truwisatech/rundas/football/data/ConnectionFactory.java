package com.samuelbjohnson.truwisatech.rundas.football.data;

import java.sql.Connection;

public interface ConnectionFactory {
	public Connection buildConnection();
}
