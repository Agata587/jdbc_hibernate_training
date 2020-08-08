package pl.sda;

import pl.sda.domain.Payment;
import pl.sda.domain.ProductLine;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

       final String query = "select customerName, country from customers"; //musimy zdefiniować Query;
        //można ten select przekopiować z MySQL

        try(Connection connection = DriverManager.getConnection(   //tutaj mamy połączenie z bazą które wcześniej utworzyliśmy
                configuration.get("db.url").toString() + "?serverTimezone=" + TimeZone.getDefault().getID(),
                configuration.get("db.user").toString(),
                configuration.get("db.pswd").toString());

            /*Statement statement = connection.createStatement(); //Statement czyli to zapytanie w kontekście Connection
            ResultSet result = statement.executeQuery(query);  //ResultSet czyli wynik który nam zwróci executeQuery
                                                                // ładujemy Query które zdefiniowaliśmy wyżej w final String
*/
            //Te dane trzeba teraz jakoś obrobić w SOUT
        ) {
            System.out.println("Nawiazano polaczenie z baza danych.");
            /*System.out.println("Oto nasi klienci z podziałem na kraje");
            System.out.println("Client name | Country");*/

/*
            while(result.next()) {
                System.out.println(result.getString("customerName") + " | " + result.getString("country"));
            }*/

          /*  List<Payment> payments = retrievePaymentsForYearAndAmountAbove(2004, 10000.0, connection);
            System.out.println("10ta pozycja na liście to: " + payments.get(9));*/


           // printProductsWithinProductLineForReturnValue(1.0, ProductLine.CLASSIC_CARS, connection);

      /*      Payment payment = new Payment("114", "yumyum7", LocalDate.now(), 500.0);
            insert(payment, connection);*/

            deleteOrder(10100, connection);


        }
        catch(SQLException sex) {
            System.err.println("Blad nawiazywania polaczenia z baza danych: " + sex);
        }

    }




        //Zadanie 2 -> chcemy wyświetlić payments za dany rok, powyżej jakiejś kwoty.


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
                       resultSet.getDate(3),
                       resultSet.getDouble(4));
           }

       }
       catch(SQLException sex) {
           System.err.println("Blad odczytu z bazy danych:" + sex);
       }


    }


        public static List<Payment> retrievePaymentsForYearAndAmountAbove(final int year, final double minAmount, final Connection connection){

            String parametrizedQuery = "select * from payments WHERE paymentDate BETWEEN ? AND ? AND amount > ?";
            // wklejamy komende z SQL'a i zamiast kontretnych parametrow dajemy "?"

            List<Payment> payments = new ArrayList<>();

            try ( PreparedStatement preparedStatement = connection.prepareStatement(parametrizedQuery)){
                preparedStatement.setDate(1, Date.valueOf(LocalDate.of(year,1,1)));  //pierwszy znak zapytania
                preparedStatement.setDate(2, Date.valueOf(LocalDate.of(year,12,31)));  //drugi znak zapytania
                preparedStatement.setDouble(3, minAmount);  // trzeci znak zapytania

                ResultSet resultSet = preparedStatement.executeQuery();


                while(resultSet.next()) {
                   payments.add(new Payment( resultSet.getString(1),
                                            resultSet.getString(2),
                                            resultSet.getDate(3),
                                            resultSet.getDouble(4)));
                }
            }
            catch(SQLException sex) {
                System.err.println("Blad odczytu z bazy danych:" + sex);
            }
            return payments;
        }




        /*
    Cwiczenie #1: Bazujac na metodzie printPaymentsForYearAndAmountAbove napisz wlasna metode, ktora z tabeli 'products'
    wypisze produkty, ktore generuja zarobek okreslony jako (msrp-buyPrice)/buyprice>0.75
    i przynaleza do żądanej kategorii productLine = Motorcycles, Classic Cars itd.)
     */

    // select * from products;

    // sprawdzamy najpierw w SQL czy to działa
    // mając zapytanie SQL wrzucamy je do naszego Query
    // odpowiednio uzupełniamy parametry w Try
    // zwracamy ReslutSet'a



    public static void printProductsWithinProductLineForReturnValue (final double returnValue, final ProductLine productLine, final Connection connection) {

        //zwracamy w metodzie zarobek -> returnValue, oraz productLine

        String parametrizedQuery = "select * from products where (MSRP-buyPrice)/buyPrice > ? and productLine = ?";
        // wklejamy komende z SQL'a i zamiast kontretnych parametrow dajemy "?"


        try(PreparedStatement preparedStatement = connection.prepareStatement(parametrizedQuery)){
            preparedStatement.setDouble(1, returnValue);  //pierwszy znak zapytania
            preparedStatement.setString(2, productLine.toString());  //drugi znak zapytania


            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.printf("Oto produkt który generuje zarobek w wysokości %f i przynależy do grupy produktów %s\n",
                    returnValue, productLine);


            while(resultSet.next()) {
                System.out.println("Product name | Product Vendor | MSRP");
                System.out.printf("%s | %s | %f\n",               // printf to jest w formacie!!!!
                        resultSet.getString("productName"),
                        resultSet.getString("productVendor"),
                        resultSet.getDouble("MSRP"));
            }

        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych:" + sex);
        }


    }

    // przykład kiedy chcemy dodać daną do bazy danych. Bedziemy dodawać płatność do tabeli Payment
    //inserting into database example

    public static void insert (final Payment payment, final Connection connection) {

        // Kod z MySQL:
       // insert into payments values(103, 'DUPA1', '2020-08-01', 5000.0);
        //select * from payments where checkNumber ='DUPA1';

        final String Query = "insert into payments values(?,?,?,?)"; //nalezy rozdzielić przecinkami i napisać łącznie

        //(String customerName, String checkNo, java.sql.Date date, double amount)  -> to jest nam potrzebne do ustawienia parametrów

        try(PreparedStatement preparedStatement = connection.prepareStatement(Query)){  //bierzemy te parametry które są w klasie Payment w konstruktorze
            preparedStatement.setString(1,payment.getCustomerName());
            preparedStatement.setString(2, payment.getCheckNo());
            preparedStatement.setDate(3, java.sql.Date.valueOf(payment.getDate()));
            preparedStatement.setDouble(4, payment.getAmount());

          final int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " rows.");

        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych:" + sex);
        }
    }



    //  teraz bedziemy usuwac
    public static void deleteOrder (final int orderNumber, final Connection connection) {


        final String Query = "delete from orders where orderNumber = ?";


        try(PreparedStatement preparedStatement = connection.prepareStatement(Query)){  //bierzemy te parametry które są w klasie Payment w konstruktorze
            preparedStatement.setInt(1, orderNumber);

            final int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " rows.");

        }
        catch(SQLException sex) {
            System.err.println("Blad odczytu z bazy danych:" + sex);
        }
    }


    }








