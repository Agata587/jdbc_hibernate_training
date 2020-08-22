package hibernate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.util.List;

/*
CREATE TABLE IF NOT EXISTS customers (
	customerId INT(7) AUTO_INCREMENT,
	fullName VARCHAR(255) NOT NULL,
	phone VARCHAR(11) NOT NULL, #xxx-xxx-xxx
	address VARCHAR(255) NOT NULL,
    PRIMARY KEY(customerId)
);
 */




@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "CUSTOMERS")
public class Customer {

    @Id
    @GeneratedValue
    Long id;            //customerId INT(7) AUTO_INCREMENT

    //mamy pole listę wypozyczeń
    @OneToMany (mappedBy = "customer")// mamy relację one customer to many rents (pierwsze słowo odnosi się do nazwy klasy w której piszemy)
    List<Rent> rents;


    @Column(nullable = false)
    String fullName;        //fullName VARCHAR(255) NOT NULL,


    //nalezy zadbac o to aby ten String mial rzeczywiscie tylko 11 znakow
    /*
    You can put the @Builder.ObtainVia annotation on the parameters (in case of a constructor or method)
    or fields (in case of @Builder on a type) to indicate alternative means by which the value
    for that field/parameter is obtained from this instance.
    For example, you can specify a method to be invoked: @Builder.ObtainVia(method = "calculateFoo").
     */


    //@Column(nullable = false, columnDefinition = "VARCHAR(11)")     //	phone VARCHAR(11) NOT NULL, #xxx-xxx-xxx
    @Column(nullable = false, length = 11)
    @Builder.ObtainVia(method = "setPhone")  //tutaj planowaliśmuy obsługę metody setPhone która miała nam pilnować długości znaków
                                            // do metod które wyliczają wartość
    //to-do: add hibernate validator; są takie walidatory, jeden z feature'ów hibernata
    String phone;


    @Column(nullable = false)
    String address;

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }


    //żeby nie wpadkowywać się w problemy z obsługą wyjątków itd. po prostu obcinamy do 11 znaków
    /*public void setPhone(String phone){
        if(phone.length() <= 11){
            this.phone = phone;
        } else {
            this.phone = phone.substring(0,10);
        }
    }
*/


    public String getAddress() {
        return address;
    }




}
