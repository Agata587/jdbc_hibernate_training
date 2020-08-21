package hibernate.service;

import hibernate.domain.Genre;
import hibernate.domain.Movie;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Queue;

public class DefaultMovieService implements MovieService {


    @Override
    public Movie findMovie(String title) {
        Query<Movie> query = DefaultSessionService.getSession().createQuery("from Movie m where m.title=:title", Movie.class);
        query.setParameter("title", title);  //musimy ustawiać parametr i zwrócić Query
        return query.uniqueResult();   //to nam zwróci encję lub null więc musimy zrobić logikę do tej drugiej metodzie
    }

    //nasze filmy sa tak reprezentowane ze tytuł jest unikalny
    //hibernate walidator umożliwiłby nam ustawienie że to pole ma byc unikalne i kontrolował tą drugą metodę findOrCreateMovie




    @Override
    public Movie findOrCreateMovie(String title, Genre genre, LocalDate releaseDate) {
        Movie m = findMovie(title);
        if (m == null) {                     //jeśli jest null czyli brak encji to musimy utowrzyć ten movie
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


// dzięki temu że w Movie odnullowaliśmy LocalDate i Genre to możemy ustawiać tutaj tylko titl
// zrobiliśmy to żeby było prościej, bo inaczej musielibyśmy wszystkie dane przekazywać (pozostałe pola też, nie tylko title



// to jest 1 metoda gdzie mamy współdzielenie sessji wewnątrz metody
/*

    @Override
    public Movie updateMovie(Movie movie) {                 //aktualizacja odbywa się w ten sposób że przekazuje się jakiś tam movie
        Session session = DefaultSessionService.getSession();   //tutaj przekazujemy sobie sesje
        Movie dbMovie = findMovie(movie.getTitle(), session);      //wyszukujesz w bazie danych movie o zadanym title który przyszedł w linijce wyżej (obiekt wszedł w stan persistent - wyciągnelismy go sobie i Hibernate może sobie na nim działać)
        if (dbMovie != null) {
            Transaction tx = session.beginTransaction();
            dbMovie.overrideWithNonNullFields(movie);
            //session.save(dbMovie);               //nie używamy save przy update bo wrzuci nam kolejną instancję, taki sam film. Mamy tylko tranzakcję bez save session // musieliśmy to skomentować bo tobiła nam się druga sesja.
            tx.commit();                            //dopiero po commit zapiszemy zmiany w bazie danych
            DefaultColoredOutputService.print(DefaultColoredOutputService.ANSI_YELLOW, "DefaultMovieService: Zaktualizowano wpis w tabeli MOVIES dla rekordu " + movie.getTitle());
        }
            return dbMovie;                                      //wszystkie pola w tym movie które są w tym movie niewynullowane poprostu je ustawiam
            // na swoim movie i ponownie zapisuje do bazy dancych
        }
*/

// to jest 2 metoda na to samo działanie
//wersja z zastosowaniem Session#merge aby przywrócić obiekt w stan persistent i  to by było manage object
    @Override
    public Movie updateMovie(Movie movie) {
        Movie dbMovie = findMovie(movie.getTitle());  // po wykonaniu metody - detached (session zostaje zamknięta)
        if (dbMovie != null) {
            Session session = DefaultSessionService.getSession();
            Transaction tx = session.beginTransaction();
            dbMovie.overrideWithNonNullFields(movie);   //tutaj aktualizujemy tego movie'sa
            session.merge(dbMovie);           // potem robimy ponownie połączenie z bazą = dbMovie przechodzi w stan persistent po ID
            tx.commit();
            DefaultColoredOutputService.print(DefaultColoredOutputService.ANSI_YELLOW, "DefaultMovieService: Zaktualizowano wpis w tabeli MOVIES dla rekordu " + movie.getTitle());
        }
        return dbMovie;

    }


    // w klasie Movie mamy nową metodę overrideWithNonNullFields w której kontrolujemy że była jakaś zmiana

    // obiekt zarządzany przez daną sesję dlatego trzeba do tej metody przenieśc równięż sesję żeby nie było błędu że już gdzieś
    // ten obiekt powstał w poprzedniej sesji (findeMovie)
    // możemy się pokusić o zrobienie aby sesja była wymienialna

        private Movie findMovie (String title, final Session session ) {
            Query<Movie> query = session.createQuery("from Movie m where m.title=:title", Movie.class);  //tutaj zamiast tworzyć sesję jak w findMovie, wykorzystujemy jakąś już sesję
            query.setParameter("title", title);
            return query.uniqueResult();
        }


        // ważne są stany w których może się znajdować obiekt
        // obiekt wszedł nam w stan detached, przestał być obiektem który ma połączenie z bazą danych i Hibernate nie mógł nagrywać tych zmian
    // przez to chciał zapisać to jako nową encję w session.save(dbMovie), ale trafił że title jest unique i przez to nie mógł zrobić zapisu




    @Override
    public boolean deleteMovie(String title) {
        return false;
    }











    }