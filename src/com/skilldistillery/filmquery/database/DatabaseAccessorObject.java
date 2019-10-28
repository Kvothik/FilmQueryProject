package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";

	public DatabaseAccessorObject() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmByIdComplete(int filmId) {
		Film film = new Film();
		try {
			String sql = "SELECT * FROM film WHERE id = ?";
			PreparedStatement stmt = setUp().prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				short releaseYear = rs.getShort("release_year");
				int langId = rs.getInt("language_id");
				int rentDur = rs.getInt("rental_duration");
				double rate = rs.getDouble("rental_rate");
				int length = rs.getInt("length");
				double repCost = rs.getDouble("replacement_cost");
				String rating = rs.getString("rating");
				String features = rs.getString("special_features");
				film = new Film(id, title, desc, releaseYear, langId, rentDur, length, repCost, rating, features, rate);
			}
			rs.close();
			stmt.close();
			setUp().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		try {
			PreparedStatement stmt = setUp().prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor(); // Create the object
				// Here is our mapping of query columns to our object fields:
				actor.setId(actorResult.getInt("id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));
				actor.setFilms(findFilmsByActorId(actorId));
				actorResult.close();
				stmt.close();
				setUp().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		try {
			String sql = "SELECT * FROM film JOIN film_actor ON film.id = film_actor.film_id " + " WHERE actor_id = ?";
			PreparedStatement stmt = setUp().prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int filmId = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				short releaseYear = rs.getShort("release_year");
				int langId = rs.getInt("language_id");
				int rentDur = rs.getInt("rental_duration");
				double rate = rs.getDouble("rental_rate");
				int length = rs.getInt("length");
				double repCost = rs.getDouble("replacement_cost");
				String rating = rs.getString("rating");
				String features = rs.getString("special_features");
				Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, length, repCost, rating,
						features, rate);
				films.add(film);
			}
			rs.close();
			stmt.close();
			setUp().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		Actor actor = null;
		try {
			String sql = "SELECT id, first_name, last_name FROM actor JOIN film_actor ON actor.id = film_actor.actor_id WHERE id = ?";
			PreparedStatement stmt = setUp().prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet actorResult = stmt.executeQuery();
			while (actorResult.next()) {
				actor = new Actor(); // Create the object
				// Here is our mapping of query columns to our object fields:
				actor.setId(actorResult.getInt("id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));
//				actor.setFilms(findFilmsByActorId(filmId));
				actors.add(actor);
			}
			actorResult.close();
			stmt.close();
			setUp().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = new Film();
		try {
			String sql = "SELECT *, language.name FROM film join language on film.language_id = language.id WHERE film.id = ?"; 
			PreparedStatement stmt = setUp().prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				short releaseYear = rs.getShort("release_year");
				String rating = rs.getString("rating");
				String desc = rs.getString("description");
				String lang = rs.getString("language.name");
				film = new Film(title, releaseYear, rating, desc, lang);
				film.setCast(findActorsByFilmId(filmId));
			}
			rs.close();
			stmt.close();
			setUp().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public List<Film> findFilmByKeyWord(String keyWord) {
		List<Film> searchResult = new ArrayList<Film>();
		Film film = new Film();
		try {
			String sql = "SELECT *, language.name FROM film join language on film.language_id = language.id where title LIKE ? OR description like ?";
			PreparedStatement stmt = setUp().prepareStatement(sql);
			stmt.setString(1, "%" + keyWord + "%");
			stmt.setString(2, "%" + keyWord + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				short releaseYear = rs.getShort("release_year");
				String rating = rs.getString("rating");
				String desc = rs.getString("description");
				String lang = rs.getString("language.name");
				film = new Film(title, releaseYear, rating, desc, lang);
				searchResult.add(film);
				film.setCast(findActorsByFilmId(id));
			}
			rs.close();
			stmt.close();
			setUp().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return searchResult;
	}

	public Connection setUp() {
		String user = "student";
		String password = "student";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
