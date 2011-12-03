package com.truwisatech.rundas.football.data.loader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PopulateDataServlet
 */
public class PopulateDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<GenericCsvLoader> loaders = new LinkedList<GenericCsvLoader>();
		
		//specify loaders, one per csv file
		loaders.add(new ScheduleCsvLoader(getServletContext().getRealPath("/WEB-INF/csvFiles/schedules.csv")));
		
		try {
			for (GenericCsvLoader l : loaders) {
				l.processFile();
			}
			
			response.getWriter().println("Data loaded");
		}
		catch(Exception e) {
			response.getWriter().println("Data load failed: " + e.getMessage() + "\nStack Trace:");
			e.printStackTrace(System.err);
			e.printStackTrace(response.getWriter());
		}
	}

}
