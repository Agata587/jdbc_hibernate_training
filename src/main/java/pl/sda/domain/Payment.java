package pl.sda.domain;

import java.time.LocalDate;

public class Payment {


    private final String customerName, checkNo;
    private final LocalDate date;
    private final double amount;

    public Payment (String customerName, String checkNo, java.sql.Date date, double amount) {
        this.customerName = customerName;
        this.checkNo = checkNo;
        this.date = date.toLocalDate();
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "customerName='" + customerName + '\'' +
                ", checkNo='" + checkNo + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }

    public String getCustomerName () {
        return customerName;
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
