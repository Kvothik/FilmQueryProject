package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

//	private void test() throws SQLException {
//		Film film = db.findFilmById(1);
//		Actor actor = db.findActorById(1);
//		List<Actor> actors = db.findActorsByFilmId(1);
//
//		for (Actor actor2 : actors) {
//			System.out.println(actor2);
//		}
//   System.out.println(actor);

//	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		int menuChoice = 0;
		do {
			System.out.println(
					"1. Look up a film by its id.\n2. Look up a film by a search keyword.\n3. Exit the application.");
			menuChoice = input.nextInt();

			switch (menuChoice) {
			case 1:
				System.out.println("Enter film ID:");
				int filmId = input.nextInt();
				Film film = db.findFilmById(filmId);
				if (film.getTitle() == null) {
					System.out.println("There is no film with this ID. Please try again.");
				} else {
					System.out.println(film);
				}
				System.out.println("1. Return to main menu\t2. Show all film details");
				int subMenu = input.nextInt();
				if (subMenu == 1) {
					break;
				} else if (subMenu == 2) {
					film = db.findFilmByIdComplete(filmId);
					System.out.println(film);
				} else {
					System.out.println("Invalid Selection, please try again.");
				}
				break;
			case 2:
				System.out.println("Enter film search keyword:");
				String keyWord = input.next();
				List<Film> filmKey = db.findFilmByKeyWord(keyWord);
				if (filmKey.size() == 0) {
					System.out.println("There is no film matching this search. Please try again.");
				} else {
					for (Film film2 : filmKey) {
						System.out.println(film2);
					}
				}
				break;
			case 3:
				System.out.println("Exiting. . .");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Selection, please try again.");
			}
		} while (menuChoice != 3);
	}

}
