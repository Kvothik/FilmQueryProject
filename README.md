# Film Query Project for Skill Distillery

## Overview
The purpose of this program is to read data from an SQL database through java.  The user is prompted with a menu to either search for a film from the database table film, by film ID or searching the database from keyword which searches the title and description of each film.

## DatabaseAccessorObject
This class declares the connection to the database.

setUp(): method which is called in each of the filtering methods. The setUp() method logs into the database with the username and password so this code is not repeated.

findFilmByIdComplete():  This method returns every field from table film by the film ID.

findActorById(): This method returns the ID, first  name, and last name of the actor by actor ID.

findFilmsByActorId(): This method returns every field of the film table by actor ID.

findActorsByFilmId(): This method returns all actors in a list that played in a film, by film ID.

findFilmById(): This method returns the title, release year, rating, description, and language of a film by its ID.

findFilmByKeyWord(): This method returns a film if the title or description matches the keyword entered by the user.
