package hibernate.domain;


import javax.persistence.*;
import javax.print.attribute.standard.CopiesSupported;


@Entity
@Table(name = "COPIES")
public class Copy {


    @Id
    @GeneratedValue
    Long id;       //copyId INT(7) AUTO_INCREMENT


    @OneToOne(mappedBy = "copy")
    Rent rent;

    @ManyToOne   //many copies to one movie
    Movie movie;


    //@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")   //isRented BOOLEAN DEFAULT false,
    boolean isRented;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }
}
