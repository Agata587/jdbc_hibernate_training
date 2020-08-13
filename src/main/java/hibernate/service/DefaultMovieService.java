package hibernate.service;

import hibernate.domain.Genre;
import hibernate.domain.Movie;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;

public class DefaultMovieService implements MovieService {


    @Override
    public Movie findMovie(String title) {
        Query<Movie> query = DefaultSessionService.getSession().createQuery("from Movie m where m.title:=title", Movie.class);
        query.setParameter("title", title);  //musimy ustawiać parametr i zwrócić Query
        return query.uniqueResult();   //to nam zwróci encję lub null więc musimy zrobić logikę do tej drugiej metodzie
    }

    //nasze filmy sa tak reprezentowane ze tytuł jest unikalny
    //hibernate walidator umożliwiłby nam ustawienie że to pole ma byc unikalne i kontrolował tą drugą metodę findOrCreateMovie

    @Override
    public Movie findOrCreateMovie(String title, Genre genre, LocalDate releaseDate) {
        Movie m = findMovie(title);
        if( m == null){                     //jeśli jest null czyli brak encji to musimy utowrzyć ten movie
              m = Movie.builder()
                      .title(title)
                      .genre(genre)
                      .releaseDate(releaseDate)
                      .build();

            Session session = DefaultSessionService.getSession();//musimy pozyskac naszą sesję z metody findMovie
            Transaction tx = session.beginTransaction();   //możemy rozpocząć tranzakcje
            session.save(m);
            tx.commit();
        }
        return m;               // jeśli jest null to tworzymy film i pote go dodajemy w tej pierwszej częsci, jeśli nie
                                // to zwracamy film w części return
    }

    @Override
    public Movie createMovie(String title, Genre genre, LocalDate releaseDate, String description) {
        Movie m = Movie.builder()                           //w tej metodzie będziemy tylko tworzyć film
                .title(title)                               //i tutaj wykorzystamy juz wszystkie pola również description
                .genre(genre)
                .releaseDate(releaseDate)
                .description(description)
                .build();

        Session session = DefaultSessionService.getSession();//musimy pozyskac naszą sesję z metody findMovie
        Transaction tx = session.beginTransaction();   //możemy rozpocząć tranzakcje
        session.save(m);
        tx.commit();

        return m;
    }


}



// dzięki temu że w Movie odnullowaliśmy LocalDate i Genre to możemy ustawiać tutaj tylko titl
// zrobiliśmy to żeby było prościej, bo inaczej musielibyśmy wszystkie dane przekazywać (pozostałe pola też, nie tylko title
