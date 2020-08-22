package hibernate.domain;


/*CREATE TABLE IF NOT EXISTS rents (
        rentId INT(7) AUTO_INCREMENT,
        copyId INT(7) NOT NULL,
        customerId INT(7) NOT NULL,
        status ENUM('In rent', 'Returned') NOT NULL DEFAULT 'In rent',              ENUM RentStatus
        rentPricePerDay DECIMAL(4,2) NOT NULL,
        borrowedDate DATE NOT NULL,
        returnedDate DATE,
        PRIMARY KEY(rentId),
        CONSTRAINT fk_rents_copies_copyId FOREIGN KEY(copyId) REFERENCES copies(copyId),
        CONSTRAINT fk_rents_customers_customerId FOREIGN KEY(customerId) REFERENCES customers(customerId)*/



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;



@Builder
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "RENTS")
public class Rent {

    @Id
    @GeneratedValue
    @Column(length = 7)
    Long id;    //rentId INT(7) AUTO_INCREMENT,

    @ManyToOne  //ustaliliśmy że taką mamy relację, czyli many RENT to one customer (pierwsze słowo odnosi sie do nazwy klasy w której piszemy)
    Customer customer;


    @OneToOne
    Copy copy;



    @ColumnDefault("0")  //default ordinal = 0 = IN_RENT - bo w Enum RentSerwis jest na pozycji pierwszej podany więc "0'
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    RentStatus status;              //   status ENUM('In rent', 'Returned') NOT NULL DEFAULT 'In rent',


    //musi być typ BigDecimal, dla double mapuje po prostu na double
   @Column(nullable = false, precision = 4, scale = 2)
   BigDecimal rentPriceDay;   // rentPricePerDay DECIMAL(4,2) NOT NULL,

    @Column(nullable = false)
    LocalDate borrowedDate;         //borrowedDate DATE NOT NULL,

    LocalDate returnedDate;         //returnedDate DATE,


    public Long getId() {
        return id;
    }

    public RentStatus getStatus() {
        return status;
    }

    public BigDecimal getRentPriceDay() {
        return rentPriceDay;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }
}
