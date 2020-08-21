package hibernate.relations;

import hibernate.service.DefaultSessionService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

public class RelationsTest {

    @Test
    public void testNonManagedRelationship(){
        Long emailId, msgId;   //mamy podane gdzie są Id-ki
        Email email;      // mamy dwa obiekty emial i message
        Message msg;

        try(Session session = DefaultSessionService.getSession()) {     //otwieramy sobie nową sesję
            Transaction tx = session.beginTransaction();                //rozpoczynamy transakcję

            email = new Email();                                        //tworzę nowego maila
            msg = new Message();                                        //tworzę nowe message

            email.setMessage(msg);                                      // ustawiam w email pole message
            //msg.setEmail(email);

            session.save(email);                                        //zapisuję sobie w sesji email
            session.save(msg);                                          // zapisuję sobie w sesji message

            emailId = email.getId();                                    //wczytuję sobie emailId i messageId
            msgId = msg.getId();                                        //wraz z zapisaniem ich w sesji powyżej muszą być im nadane nowe Id-ki

            tx.commit();                                                //zatwierdzam
        }

        assertNotNull(email.getMessage());                              //sprawdzam czy rzeczywiście email.getMessage nie jest nullem
        assertNull(msg.getEmail());                                     // ale sprawdzam że msg.getEmail jest nullem

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(Email.class, emailId);              // po Id-kach wyciągam sobie te encje
            msg = session.get(Message.class, msgId);
        }

        assertNotNull(email.getMessage());                      // drugi raz sprawdzamy mimo nowej sesji
        assertNull(msg.getEmail());


    }

    @Test
    public void testManuallyManagedRelationship() {
        Long emailId, msgId;
        Email email;
        Message msg;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            email = new Email();
            msg = new Message();

            email.setMessage(msg);
            msg.setEmail(email);       //różni się tylko tym od górnego testu że ma to odkomentowane

            session.save(email);
            session.save(msg);

            emailId = email.getId();
            msgId = msg.getId();

            tx.commit();
        }

        //JVM - sprawdzenie obiektów Java'owych
        assertNotNull(email.getMessage());
        assertNotNull(msg.getEmail());

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(Email.class, emailId);
            msg = session.get(Message.class, msgId);
        }

        //DB - to jest widoczne w bazie (DB = data base)
        assertNotNull(email.getMessage());
        assertNotNull(msg.getEmail());
    }

// ale nie chcemy tego robić ręcznie, dlatego wprowadzamy mappedBy


    @Test  //robimy to niemal identycznie jak test powyżej z drobnymi zmianami
    public void testManagedRelationship() {
        Long emailId, msgId;
        EmailMapped email;   //podmieniamy tutaj nazwy
        MessageMapped msg;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            email = new EmailMapped();
            email.setSubject("JavaWWA13");   //ustawiamy subject
            msg = new MessageMapped();
            msg.setContent("See you @02.02.2019!");  //ustawiamy content

            //email.setMessage(msg);
            msg.setEmail(email);               //mappedBy jest po stronie email więc relacją zarządza message

            //powiedzieliśmy że oznaczamy jedną stronę jako "zarządzaną" przez tę drugą poprzez atrybut mappedBy
            //czyli strona która ma mappedBy jest zarządzana przez tą drugą
            //czyli w naszym przypadku mappedBy znajduje się w email , czyli zarządzany jest przez MessageMapped
            //stąd wynika to że zapisuje message msg.setEmail(email);


            session.save(email);
            session.save(msg);

            emailId = email.getId();
            msgId = msg.getId();

            tx.commit();
        }



        //JVM - to jest to co dzieje się w JVM
        assertEquals(email.getSubject(), "JavaWWA13");
        assertEquals(msg.getContent(), "See you @02.02.2019!");
        assertNull(email.getMessage());                                     //na ten moment email jest null
        assertNotNull(msg.getEmail());                                       // message jest ustawiony wyżej więc nie jest null'em

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(EmailMapped.class, emailId);                // po wyciągnięciu tego z bazy danych
            msg = session.get(MessageMapped.class, msgId);
        }

        // DB
        assertNotNull(email.getMessage());                                  // okazuje się że oba są not null
        assertNotNull(msg.getEmail());
    }

    @Test
    public void managed_relationship_called_from_wrong_side_ie_non_managed() {
        Long emailId, msgId;
        EmailMapped email;
        MessageMapped msg;

        try(Session session = DefaultSessionService.getSession()) {
            Transaction tx = session.beginTransaction();

            email = new EmailMapped();
            email.setSubject("JavaWWA13");
            msg = new MessageMapped();
            msg.setContent("See you @02.02.2019!");

            email.setMessage(msg);
            //msg.setEmail(email);    //mappedBy jest po stronie email czyli relacja zarzadza msg

            session.save(email);
            session.save(msg);

            emailId = email.getId();
            msgId = msg.getId();

            tx.commit();
        }

        //JVM
        assertEquals(email.getSubject(), "JavaWWA13");
        assertEquals(msg.getContent(), "See you @02.02.2019!");
        assertNotNull(email.getMessage());  //dla JVM jest ustawione pole msg dla email
        assertNull(msg.getEmail()); //ale nie jest ustawione pole email dla msg

        try(Session session = DefaultSessionService.getSession()) {
            email = session.get(EmailMapped.class, emailId);    //nadpisz ref do email obiektem z bazy danych
            msg = session.get(MessageMapped.class, msgId);  //jw
        }

        //DB
        assertNull(email.getMessage()); //poniewaz FK do email znajduje sie po stronie msg a nie zostalo ustawione to nadpisany obiekt email utracil ref do msg
        assertNull(msg.getEmail()); //email nigdy nie zostal powiazany z msg poprzez FK
    }


}


//opis tego jest na filmie - 2 dzień, ostatnia część od godz. 16:00