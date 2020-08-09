package pl.sda.domain;

import java.time.LocalDate;

public class Payment {


    private final String customerNumber, checkNo;
    private final LocalDate date;
    private final double amount;

    public Payment (String customerNumber, String checkNo, java.sql.Date date, double amount) {
        this.customerNumber = customerNumber;
        this.checkNo = checkNo;
        this.date = date.toLocalDate();
        this.amount = amount;
    }

    public Payment (String customerNumber, String checkNo, LocalDate date, double amount) {
        this.customerNumber = customerNumber;
        this.checkNo = checkNo;
        this.date = date;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "customerName='" + customerNumber + '\'' +
                ", checkNo='" + checkNo + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getCheckNo () {
        return checkNo;
    }

    public LocalDate getDate (){
        return date;
    }

    public double getAmount() {
        return amount;
    }



}
