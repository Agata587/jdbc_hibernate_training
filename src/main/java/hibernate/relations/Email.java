package hibernate.relations;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Email {

    @Id
    @GeneratedValue
    Long id;

    String subject;

    @OneToOne //(mappedBy = "email") //IMPORTANT!
    Message message;




}
