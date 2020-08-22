package hibernate.domain;

import hibernate.service.DefaultSessionService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertNotNull;

public class OrphanRemovalTest {



    @Test
    public void orphan_copies_are_removed() {

        //given
        // (musim potworzyć sobie jakies encje)
        Movie movie = Movie.builder()
                .title("Ogniem i mieczem")
                .genre(Genre.HISTORICAL)
                .releaseDate(LocalDate.of(1990,2,8))
                .build();
        Long movieId;
        List<Long> copiesId = new ArrayList<>();

        //tworzymi sobie kopie
       /* Copy c1 = new Copy();
        c1.setMovie(movie);
        Copy c2 = new Copy();
        c2.setMovie(movie);
        Copy c3 = new Copy();
        c3.setMovie(movie);  */


       //druga wersja tworzenia copies w tablicy
        Copy[] copies = {new Copy(), new Copy(), new Copy()};  //tworze kopie
        movie.setCopies(Arrays.asList(copies));                 // potwem powiązuje je z movies

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();                                        //robimy tutaj tranzakcję bo zapisujemy do bazy danych
            session.persist(movie);     //should persist copies too due to CASCADE.PERSIST
            movieId = movie.getId();
            tx.commit();
        }

       //sprawdżmy teraz czy to wszystko się zapisało prawidłowo

        try(Session session = DefaultSessionService.getSession()) {
         movie = session.get(Movie.class, movieId);                 //tutaj jest tylko odczyt, nie ma zmiany na bazie danych więc nie robimy tranzakcji

            //pobierzmy ID zapisacnych kopii
            for(Copy c : movie.getCopies())
                copiesId.add(c.getId());
        }

        assertNotNull(movie);
        assertNotNull(movie.getCopies());




        //when
        //teraz przy usunięciu reszta copies równiez powinna być usunięta
        // otworzymy sobie nową sesję i przekażemy sobie movie, potem usuwamy

        try(Session session = DefaultSessionService.getSession()) {
           Transaction tx = session.beginTransaction();                     // tutaj jest usuwanie, zachodzi zmiana w bazie danych wiec znowu robię tranzakcje
            session.delete(movie);  //powinno usunąć movie oraz copies z nim powiązane
            tx.commit();
        }

        //then
        try(Session session = DefaultSessionService.getSession()) {
           movie = session.get(Movie.class, movieId);
           for(Long copyId : copiesId)
               assertNull(session.get(Copy.class, copyId)); //powinno zwrócić null, bo nie ma ich w bazie danych
        }



        assertNull(movie);

    }


}
