package hibernate.domain;


/*
CREATE TABLE IF NOT EXISTS movies (
        movieId INT(7) AUTO_INCREMENT,
        title VARCHAR(255) NOT NULL,
        genre ENUM('Documentary', 'Thriller', 'Musical', 'Comedy', 'Horror', 'Sci-Fi', 'Action', 'Drama', 'Romance') NOT NULL,
        releaseDate DATE NOT NULL,
        description TEXT,
        PRIMARY KEY(movieId)
        );
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;


@Builder // dodaliśmy go żeby nam było łatwiej później
@ToString  //dodaliśmy żeby nam się ładnie wyświetlał opis i to jest zamiast generate ToSting
@NoArgsConstructor    // anotacja ustawiająca nam konstruktor bezargumentowy
@AllArgsConstructor  //anotacja ustawiająca nam konstruktor na wszystkie argumenty


@Entity //jeśli nie podamy Table name to tabela przyjmie nazwę Encji. Lepiej jej zostawić Entity domyślne (wówczas będzie to nazwa klasy) a Tabeli nadać nazwę jaką chcemy
@Table(name = "MOVIES")   // nadajemy nazwę naszej Tabeli
public class Movie {


    @Id
    @GeneratedValue
   Long id;    // to jest movieId INT(7) AUTO_INCREMENT,



    @Column(nullable = false)
    String title;               // to jest  title VARCHAR(255) NOT NULL,



    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)   //0 - Documentary, 1 -....
    Genre genre;            //genre ENUM('Documentary', 'Thriller', 'Musical', 'Comedy', 'Horror', 'Sci-Fi', 'Action', 'Drama', 'Romance'),NOT NULL



    @Column(nullable = false)
    LocalDate releaseDate;    //   releaseDate DATE NOT NULL,

    //domyślnie String jest mapowany na varchar [255] - to jest słabe
    //my chcemy żeby mapowało nam na TEXT
    @Type(type = "text")
    String description;





    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Genre getGenre() {
        return genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }

}
