package com.truwisatech.rundas.football.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RundasMySqlConnectionFactory implements ConnectionFactory {

	@Override
	public Connection buildConnection() {
		Connection conn = null;
		
		String DRIVER = "com.mysql.jdbc.Driver";
		String URL = "jdbc:mysql://localhost:8889/Rundas?user=rundas&password=Rundas1";
		
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
