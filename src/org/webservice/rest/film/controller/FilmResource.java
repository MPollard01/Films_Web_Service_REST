package org.webservice.rest.film.controller;

import java.util.ArrayList;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.webservice.rest.film.model.Film;
import org.webservice.rest.film.model.FilmDAO;


@Path("/films")
public class FilmResource {
	
	@Context
    UriInfo uriInfo;
    @Context
    Request request;
	
	private final FilmDAO fd = FilmDAO.getInstance();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ArrayList<Film> getFilms() {
		return (ArrayList<Film>) fd.listFilm();
	}
	
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/text")
	public String getText() {
		ArrayList<Film> films = (ArrayList<Film>) fd.listFilm();
		return toText(films);
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("/id/{id}")
	public Film searchFilmById(@PathParam("id") int id) {
		return fd.getFilmByID(id);
	}
	
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/id/text/{id}")
	public String searchFilm(@PathParam("id") int id) {
		return fd.getFilmByID(id).toString();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("/title/{title}")
	public ArrayList<Film> searchFilmByTitle(@PathParam("title") String title) {
		return (ArrayList<Film>) fd.retrieveFilm(title);
	}
	
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/title/text/{title}")
	public String searchTitle(@PathParam("title") String title) {
		ArrayList<Film> films = (ArrayList<Film>) fd.retrieveFilm(title);
		return toText(films);
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("/create")
	public ArrayList<Film> createFilm(Film f) {
		fd.insertFilm(f);
		return (ArrayList<Film>) fd.listFilm();
	}
	
	@POST
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/create/text")
	public String createFilmText(String s) {
		String[] film = s.split("#");
		Film f = new Film(Integer.parseInt(film[0]), film[1], Integer.parseInt(film[2]), film[3],
				film[4], film[5]);
		fd.insertFilm(f);
		ArrayList<Film> films = (ArrayList<Film>) fd.listFilm();
		return toText(films);
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Path("/update")
	public ArrayList<Film> update(Film f) {
		fd.updateFilm(f);
		return (ArrayList<Film>) fd.listFilm();
	}
	
	@PUT
	@Consumes({MediaType.TEXT_PLAIN})
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/update/text")
	public String updateText(String s) {
		String[] film = s.split("#");
		Film f = new Film(Integer.parseInt(film[0]), film[1], Integer.parseInt(film[2]), film[3],
				film[4], film[5]);
		fd.updateFilm(f);
		ArrayList<Film> films = (ArrayList<Film>) fd.listFilm();
		return toText(films);
	}
	
	@DELETE
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Path("/delete/{id}")
	public ArrayList<Film> delete(@PathParam("id") int id) {
		fd.deleteFilm(id);
		return (ArrayList<Film>) fd.listFilm();
	}
	
	@DELETE
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/delete/text/{id}")
	public String deleteText(@PathParam("id") int id) {
		fd.deleteFilm(id);
		ArrayList<Film> films = (ArrayList<Film>) fd.listFilm();
		return toText(films);
	}
	
	private String toText(ArrayList<Film> films) {
		StringBuilder sb = new StringBuilder();
		for (Film f : films)
		{
		    sb.append(f);
		    if(films.indexOf(f) < films.size()-1)
		    	sb.append("\n");
		}
		
		return sb.toString();
	}
	
}
