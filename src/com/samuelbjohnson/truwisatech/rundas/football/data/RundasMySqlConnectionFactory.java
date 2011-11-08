package com.samuelbjohnson.truwisatech.rundas.football.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RundasMySqlConnectionFactory implements ConnectionFactory {

	@Override
	public Connection buildConnection() {
		Connection conn = null;
		
		String DRIVER = "org.sqlite.JDBC";
		String URL = "jdbc:sqlite:localhost:footballStats";
		
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL);
			
		}
		catch (SQLException s) {
			System.err.println("Sql issue: ");
			System.err.println(s.getMessage());
			//TODO implement better logging
			return null;
		}
		catch (ClassNotFoundException c) {
			System.err.println("Problem finding JDBC driver");
			//TODO implement better logging
			return null;
		}
		return conn;
	}
	
}
