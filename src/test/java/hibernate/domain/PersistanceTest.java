package hibernate.domain;

import net.bytebuddy.asm.Advice;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.util.List;

public class PersistanceTest {


    SessionFactory sessionFactory;   // pole testu

                    // to odpowiednik BeforeClass z J Unit
    @BeforeSuite   // oznacza, że przed wykonaniem całego testu, to należy tą metodę najpierw wywołać
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

    }
     // do tego momentu w zasadzie trzeba się tego nauczyć bo to musi być wykonane zanim przejdziemy do testów

    @Test
    public void saveSimpleMovies() {                                          // ta metoda ma nam persistować 2 filmy
        SimpleMovie movie = new SimpleMovie("Ogniem i Mieczem");
        SimpleMovie movie2 = new SimpleMovie("Planet Earth II");
        try(Session session = sessionFactory.openSession()){           // pozyskujemy sesję
            Transaction tx = session.beginTransaction();             // otwieramy tranzakcje, to jest wymagane w sesji i to nie jest do końca to samo co tranzakcja w SQL
            session.persist(movie);
            session.persist(movie2);                                // mamy sesje i jak chcemy zrobić operacje na bazie to
            tx.commit();                                           // musimy otworzyć a potem zamknąć tranzakcje
        }                                                         // ona też zapewnia że nie ma poblemów z mieszaniem się danych

    }

    @Test(dependsOnMethods = "saveSimpleMovies")  //ta adnotacja mówi kiedy ten test ma się wywoływać, czyli kiedy saveMovie wykona się prawidłowo
    public void readSimpleMovies() {
        try(Session session = sessionFactory.openSession()) {    // pozyskuje sobie sesje
            List<SimpleMovie> movies = session.createQuery("from SimpleMovie", SimpleMovie.class).list();
            assertEquals(movies.size(), 2);   // sprawdzamy czy ma faktycznie 2 jednostki
            for(SimpleMovie movie : movies)
                System.out.println(movie.getTitle());
        }


    }



    @Test
    public void saveMovies(){
        Movie movie = Movie.builder()
                .title("Ogniem i Miecznem")
                .genre(Genre.HISTORICAL)
                .releaseDate(LocalDate.of(1999,2,7))
                .description("Kozacy się buntują")
                .build();


        try(Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(movie);
            tx.commit();
        }
    }

    /*@Test(dependsOnMethods = "saveMovies")
    public void readMovies() {
        try(Session session = sessionFactory.openSession()) {
            List<Movie> movies = session.createQuery("from Movie", Movie.class).list();
            assertEquals(movies.size(), 2);
            for(Movie movie : movies)
                System.out.println(movie);
        }

    }*/









}
