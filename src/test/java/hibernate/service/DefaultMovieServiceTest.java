package hibernate.service;

import org.assertj.core.api.Assertions;
import hibernate.domain.Genre;
import hibernate.domain.Movie;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class DefaultMovieServiceTest {

    MovieService movieService = new DefaultMovieService();

    @Test
    public void creates_and_finds_movie() {   //sprawdzamy czy utworzony film jest na pewno odnajdowany w bazie danych
        //utworzyc movie - createMovie
        //sprawdzic, czy jest na bazie - filmMovie

        //given

        //when
        movieService.createMovie("Ogniem i Mieczem",          //tutaj poajemy dane przykładowego filmu który utworzyliśmy
                Genre.HISTORICAL,
                LocalDate.of(1992, 2, 8),
                "Rzeczpospolita Obojga Narodow - poczatek upadku.");

        Movie m = movieService.findMovie("Ogniem i Mieczem");   //nastepnie próbujemy wywołać ten film

        //then   (podajemy dokładnie jakie dane powinien ten film posiadać, weryfikujemy poprawność
        Assertions.assertThat(m).hasFieldOrPropertyWithValue("title", "Ogniem i Mieczem")
                .hasFieldOrPropertyWithValue("genre", Genre.HISTORICAL)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1992, 2, 8))
                .hasFieldOrPropertyWithValue("description", "Rzeczpospolita Obojga Narodow - poczatek upadku.")
                .as("Film nie zostal poprawnie zapisany w bazie danych.");  //jeśli dane się nie zgadzają to zgłaszamy że nie został zapisany
    }





    @Test
    public void does_not_find_non_existing_movie() {

        //given

        //when
        Movie m = movieService.findMovie("Listonosz Pat");  //jeśli wyszukam movie które nie istnieje

        //then
        Assertions.assertThat(m).isNull();                      // to zwrotka będzie nullem

    }

    @Test
    public void finds_and_does_not_create_existing_movie() {  //tutaj znajduje movie więc nie tworzy movie jeszcze raz

        //given   (tworze movie)
        movieService.createMovie("Planet Earth II", Genre.DOCUMENTARY, LocalDate.of(2016, 11, 6), null);

        //when  (potem wywołuje wyszukanie movie i podaje fałszywe dan, title się zgadza ale dane już nie, to też sprawdzi czy nie nadpiszemy tego filmu )
        Movie m = movieService.findOrCreateMovie("Planet Earth II", Genre.COMEDY, LocalDate.of(1999, 9, 9));

        //then  (sprawdzam czy zgadzają się podane w given dane)
        Assertions.assertThat(m).hasFieldOrPropertyWithValue("title", "Planet Earth II")
                .hasFieldOrPropertyWithValue("genre", Genre.DOCUMENTARY)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2016, 11, 6))
                .hasFieldOrPropertyWithValue("description", null)
                .as("Film zostal ponownie dodany do tabeli w bazie danych, pomimo ze juz tam sie znajdowal.");
                            // jesli nie jest to zwracam info że film został ponownie zapisany mimo że już znajdował się w bazie co jest błędem
    }




    @Test
    public void does_not_find_and_creates_movie() {   //nie znajduje i tworzy w związku z tym
        //given

        //when (tworze sobie jakieś movie)
        movieService.findOrCreateMovie("Seven", Genre.THRILLER, LocalDate.of(1996, 2, 14));

        Movie m = movieService.findMovie("Seven");   //wołam wywołanie tego moviesa

        //then (sprawdzam czy rzeczywiście został on utworzony)
        Assertions.assertThat(m).hasFieldOrPropertyWithValue("title", "Seven")
                .hasFieldOrPropertyWithValue("genre", Genre.THRILLER)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1996, 2, 14))
                .hasFieldOrPropertyWithValue("description", null)
                .as("Film nie zostal dodany do tabeli w bazie danych, pomimo, ze nie istnial.");

    }


    //testujemy metode updateMovie z klasy DefaultMovieService

    @Test
    public void updates_existing_movie() {
        //given (tutaj będzie sobie istniał jakiś film)
        movieService.findOrCreateMovie("Random Movie", Genre.THRILLER, LocalDate.of(1996, 2, 14));

        //when (tworzymy film gdzie tytuł jest taki sam ale inne parametry się zminiły i sprawdzamy czy metoda updateMovie działa poprawnie
        Movie updated = Movie.builder()
                .title("Random Movie")
                .genre(Genre.COMEDY)
                .releaseDate(LocalDate.of(2020,8,2))
                .description("Our custom favourite movie! :")
                .build();

        movieService.updateMovie(updated);  //i tutaj podajmy już ten film po updatecie

        //na wszelki wypadek wyszukajmy go jeszcze żeby sprawdzić czy zadziałało prawidłowo
        Movie afterUpdate = movieService.findMovie("Random Movie");

        //then
        Assertions.assertThat(afterUpdate).isEqualToIgnoringNullFields(updated)  //sprawdza nam pole po polu czy jest update
                .as("Film nie został zaktualizowany");

    }



    //TO-DO
    @Ignore
    @Test
    public void does_not_update_non_existing_movie(){
    }



    @Test
    public void deletes_existing_movie() {

        //testy muszą być niezależne wiec najpierw musimy utworyć film i dopiero go usunąć

        //given
        movieService.createMovie("My movie", Genre.DOCUMENTARY, LocalDate.now(),null);
        Movie beforeDelete = movieService.findMovie("My movie");

        //when
        movieService.deleteMovie("My movie");
        Movie afterDelete = movieService.findMovie("My movie");


        //then
        Assertions.assertThat(beforeDelete).isNotNull();
        Assertions.assertThat(afterDelete).isNull();



    }


    @Ignore
    @Test
    public void does_not_delete_non_existing_movie() {

    }


}
