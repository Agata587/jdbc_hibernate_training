package hibernate.domain;


import javax.persistence.Column;
import javax.persistence.Entity;  //javax.persistence to oznacze że jest to adnotacja która wchodzi w skład standardów JPA
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity // przykład jak działa to domyślnie
public class SimpleMovie {

    @Id  // to jest wskazówka że to jest nasz private key
    @GeneratedValue  //to wskazuje że Hibernate ma zarządzać numeracją i sam ją pokolei narzucać
    Long id;

    @Column(nullable = false)     //ma odpowiadać kolumnie i w SQL: NOT NULL
    String title;

    public SimpleMovie() {    // konstruktor bezargumentowy/domyslny - jest obowiązkowy przy Hibernate

    }


    public SimpleMovie(String title) {
        this.title = title;
    }


    public Long getId() {
        return id;
    }

  /*  public void setId(Long id) {
        this.id = id;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
