package pl.sda;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;

public class EstabilishingConnection {

// to jest pokaz jak można tworzyć połączenie z wykorzystaniem pliku PROPERTIES


    public static void main(String[] args) {

        //1. Najpierw musimy napisać kod dzięki któremu będziemy mogli odczytać plik properties
        // w którym mamy potrzebne dane do połączenie z bazą

        //najpierw tworzymy obiekt properties
        Properties configuration = new Properties();

        // chcemy tutaj otworzyć sobie połączenie "tunel"
        try {
            //odczytujemy plik resouces -> config.properties
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream resourceFileInputStream = classloader.getResourceAsStream("config.properties");


            //metoda load która pozwala odczytać z input stream'a)
            configuration.load(resourceFileInputStream);
        }
        catch(IOException ioe) {
            System.out.println("Nie można otworzyć pliku konfigurującego:" + ioe);
        }

        //2. Następnie mając już połączenie z plikiem Properties możemy nawiązać połączenie z bazą

        // od tej linii obiekt klasy Properties zawiera odczytana konfiguracje z config.properties

        System.out.println(configuration.get("db.url"));  // -> potwierdzamy sobie że mamy te dane

        try(Connection connection = DriverManager.getConnection(
                configuration.get("db.url").toString() + "?serverTimezone=" + TimeZone.getDefault().getID(),
                //configuration.get("db.url").toString(),
               configuration.get("db.user").toString(),
                configuration.get("db.pswd").toString()
        )) {
            System.out.println("Nawiazano polaczenie z baza danych.");
        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }


    }


}
