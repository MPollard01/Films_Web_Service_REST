package org.webservice.rest.film.model;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import java.sql.*;
import org.webservice.rest.film.utils.FilmInfo;

public class FilmDAO implements FilmInfo {
	
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
//    private final String USER = "pollarma";
//    private final String PASSWORD = "ankltonD7";
//    private final String URL = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+USER;
//	private final String USER = "test";
//    private final String PASSWORD = "test";
//    private final String URL = "jdbc:mysql://34.105.191.225:3306/film_db";
    
    private final String USER = "root";
    private final String PASSWORD = "";
    private final String URL = "jdbc:mysql://localhost:3306/films";

	private FilmDAO() {}
	
	private static FilmDAO daoInstance;
	
	public static FilmDAO getInstance() {
		if(daoInstance == null) {
			return new FilmDAO();
		}
		return null;
	}

	
	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) { System.out.println(e); }

		// connecting to database
		try{
 			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		    stmt = conn.createStatement();
		} catch(SQLException se) { System.out.println(se); }	   
    }
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	

   public Film getFilmByID(int id){
		oneFilm=null;
	    this.injectSql("select * from films where id="+id, oneFilm);

	   return oneFilm;
   }


	@Override
	public void insertFilm(Film finfo) {
		String sql = "INSERT INTO films(id,title,year,director,stars,review) "
				+ "VALUES ("+ finfo.getId() + ", '" + finfo.getTitle() + "', " + finfo.getYear()
				+ ", '" + finfo.getDirector() + "', '" + finfo.getStars() + "', '" + finfo.getReview()
				+ "')";
		
		this.injectSql(sql);
	}
	
	
	@Override
	public void updateFilm(Film finfo) {
		String sql = "UPDATE films SET title = '"+ finfo.getTitle() +"', year = "+ finfo.getYear()
		+ ", director = '"+ finfo.getDirector() +"', stars = '"+ finfo.getStars()
		+ "', review = '"+ finfo.getReview() +"' WHERE id = "+ finfo.getId();
		
		this.injectSql(sql);
	}
	
	
	@Override
	public void deleteFilm(int FilmID) {
		this.injectSql("DELETE FROM films WHERE id = "+ FilmID);
	}
	
	
	@Override
	public Collection<Film> listFilm() {
		ArrayList<Film> allFilms = new ArrayList<Film>();
		
	    this.injectSql("select * from films", allFilms);
	
	   return allFilms;
	}
	
	
	@Override
	public Collection<Film> retrieveFilm(String searchStr) {
		ArrayList<Film> allFilms = new ArrayList<Film>();
		
		this.injectSql("select * from films where title='" + searchStr + "'", allFilms);
		  
	   return allFilms;
	}
   
   
	private void injectSql(String sql) {
		openConnection();
		
		try {
			
			stmt.executeUpdate(sql);
			stmt.close();
		    
		} catch(SQLException se) { System.out.println(se); }
		finally {
			closeConnection();
		}
	}
	
	private void injectSql(String sql, ArrayList<Film> films) {
		openConnection();
		
		try {
			
			ResultSet rs1 = stmt.executeQuery(sql);
		    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	films.add(oneFilm);
		   }
			stmt.close();
		    
		} catch(SQLException se) { System.out.println(se); }
		finally {
			closeConnection();
		}
	}
	
	private void injectSql(String sql, Film film) {
		openConnection();
		
		try {
			
			ResultSet rs1 = stmt.executeQuery(sql);
		    // Retrieve the results
		    while(rs1.next()){
		    	film = getNextFilm(rs1);
		   }
			stmt.close();
		    
		} catch(SQLException se) { System.out.println(se); }
		finally {
			closeConnection();
		}
	}
   
}
