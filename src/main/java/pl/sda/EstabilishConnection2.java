package pl.sda;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.TimeZone;

public class EstabilishConnection2 {


    public static void main(String[] args) {


        Properties configuration = new Properties();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream resourceFileInputStream = classloader.getResourceAsStream("config.properties");

            configuration.load(resourceFileInputStream);
        }
        catch(IOException ioe) {
            System.out.println("Nie można otworzyć pliku konfigurującego:" + ioe);
        }

        // od tej lini obiekt klasy Properties zawiera odczytaną konfigurację






       final String query = "select customerName, country from customers"; //musimy zdefiniować Query;
        //można ten select przekopiować z MySQL

        try(Connection connection = DriverManager.getConnection(   //tutaj mamy połączenie z bazą które wcześniej utworzyliśmy
                configuration.get("db.url").toString() + "?serverTimezone=" + TimeZone.getDefault().getID(),
                configuration.get("db.user").toString(),
                configuration.get("db.pswd").toString());

            Statement statement = connection.createStatement(); //Statement czyli to zapytanie w kontekście Connection
            ResultSet result = statement.executeQuery(query);  //ResultSet czyli wynik który nam zwróci executeQuery
                                                                // ładujemy Query które zdefiniowaliśmy wyżej w final String

            //Te dane trzeba teraz jakoś obrobić w SOUT
        ) {
            System.out.println("Nawiazano polaczenie z baza danych.");
            System.out.println("Oto nasi klienci z podziałem na kraje");
            System.out.println("Client name | Country");


            while(result.next()) {
                System.out.println(result.getString("customerName") + " | " + result.getString("country"));
            }
        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }


    }



}

