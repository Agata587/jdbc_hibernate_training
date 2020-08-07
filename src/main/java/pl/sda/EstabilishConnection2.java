package pl.sda;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
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



        //Zadanie 1 -> przykład z pierwszym pobraniem danych

       /*final String query = "select customerName, country from customers"; //musimy zdefiniować Query;
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

    }*/




        //Zadanie 2 -> chcemy wyświetlić payments za dany rok, powyżej jakiejś kwoty.

        try(Connection connection = DriverManager.getConnection(   //tutaj mamy połączenie z bazą które wcześniej utworzyliśmy
                configuration.get("db.url").toString() + "?serverTimezone=" + TimeZone.getDefault().getID(),
                configuration.get("db.user").toString(),
                configuration.get("db.pswd").toString());


        ) {
            System.out.println("Nawiazano polaczenie z baza danych.");

            printPaymentsForYearAndAmountAbove(2004, 10000.0, connection);

        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }

    }




    public static void printPaymentsForYearAndAmountAbove (final int year, final double minAmount, final Connection connection) {

        String parametrizedQuery = "select * from payments where paymentDate between ? and ? and amount > ?";
                                            // wklejamy komende z SQL'a i zamiast kontretnych parametrow dajemy "?"


       try(PreparedStatement preparedStatement = connection.prepareStatement(parametrizedQuery)){
           preparedStatement.setDate(1, Date.valueOf(LocalDate.of(year,1,1)));  //pierwszy znak zapytania
           preparedStatement.setDate(2, Date.valueOf(LocalDate.of(year,12,31)));  //drugi znak zapytania
           preparedStatement.setDouble(3, minAmount);  // trzeci znak zapytania


           ResultSet resultSet = preparedStatement.executeQuery();

           System.out.printf("Oto palatnosci za rok %s w kwocie przekraczającej %f\n", year, minAmount); //PrintStream w formacie


           while(resultSet.next()) {
               System.out.println("Customer number | Check number | Payment Date | Amount");
               System.out.printf("%s | %s | %s | %f\n",               // printf to jest w formacie!!!!
                       resultSet.getString(1),
                       resultSet.getString(2),
                       resultSet.getString(3),
                       resultSet.getDouble(4));
           }

       }
       catch(SQLException sex) {
           System.err.println("Blad odczytu z bazy danych:" + sex);
       }


    }






}

