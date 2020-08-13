package hibernate.service;

import hibernate.domain.Genre;
import hibernate.domain.Movie;

import java.time.LocalDate;

public interface MovieService {


    Movie findMovie(String title);
    Movie findOrCreateMovie(String title, Genre genre, LocalDate releaseDate);
    Movie createMovie(String title, Genre genre, LocalDate releaseDate, String description); //to będzie metoda która pozwoli nam utworzyć film z pełnymi danymi



}
