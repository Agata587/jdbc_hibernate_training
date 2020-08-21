package hibernate.service;

import hibernate.domain.Genre;
import hibernate.domain.Movie;

import java.time.LocalDate;

public interface MovieService {


    Movie findMovie(String title);
    Movie findOrCreateMovie(String title, Genre genre, LocalDate releaseDate);
    Movie createMovie(String title, Genre genre, LocalDate releaseDate, String description); //to będzie metoda która pozwoli nam utworzyć film z pełnymi danymi


    //napiszemy sobie kontrakt jak to ma działac
    /**
     * Lookups movie by given title in database and updates fields according to passed non-null fields from instance
     *
     * @param movie
     * @return
     */

    //czyli idea jest taka ze przekazujemy Movie, po title wyszukujemy to movie i wszystkie non null fields z tego movie robimy
    // aktualizacje na naszym obiekcie i to zapisujemy

    Movie updateMovie(Movie movie);  // przekazujemy instancję Movie już po wprowadzeniu jakichś zmian

    boolean deleteMovie(String title);  //będziemy usuwali po title


}
