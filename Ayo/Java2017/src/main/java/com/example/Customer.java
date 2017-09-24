package com.example;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by Administrator on 2017/7/23.
 */

public class Customer {

    private String _name;

    private Vector _rentals = new Vector();

    public Customer(String name){
        _name = name;
    }

    public void addRental(Rental r){
        _rentals.addElement(r);
    }

    public String getName(){
        return _name;
    }


    public String statement(){
        Enumeration rentals = _rentals.elements();
        String result = "Rental Record for " + getName() + "\n";
        while (rentals.hasMoreElements()){
            Rental each = (Rental)rentals.nextElement();
            double thisAmount = each.getCharge();
            result += "\t" + each.getMovie().getTitle() + "\t (rental " + each.getDaysRented() + " days), cost " + String.valueOf(thisAmount) + "\n";
        }
        result += "Amount owed is " + String.valueOf(getTotalCharge()) + "\n";
        result += "You earned " + String.valueOf(getTotalPoints()) + " Frequent renter points";
        return result;
    }

    public double getTotalCharge(){
        double totalAmount = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()){

            Rental each = (Rental)rentals.nextElement();
            totalAmount += each.getCharge();
        }
        return totalAmount;
    }

    public double getTotalPoints(){
        int frequentRenterPoints = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()){

            Rental each = (Rental)rentals.nextElement();
            frequentRenterPoints += each.getFrequentRenterPoints();
        }
        return frequentRenterPoints;
    }


}
