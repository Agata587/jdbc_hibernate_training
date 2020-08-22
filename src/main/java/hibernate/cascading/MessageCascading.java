package hibernate.cascading;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class MessageCascading {


    @Id
    @GeneratedValue
    Long id;
    String content;
    @OneToOne
    EmailCascading email;          //tutaj jest FK do email i że ten FK nazywa się "email"

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailCascading getEmail() {
        return email;
    }

    public void setEmail(EmailCascading email) {
        this.email = email;
    }
}
