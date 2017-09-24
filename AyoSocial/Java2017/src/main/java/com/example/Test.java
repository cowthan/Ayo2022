package com.example;

/**
 *  怎么在Android Studio里跑纯java
 * http://blog.csdn.net/xx326664162/article/details/51455313
 */
public class Test {
    public static void main(String[] args){
        System.out.println("dsdsdfasdf");

        Customer customer = new Customer("Tom");

        Movie m1 = new Movie("Movie-children", Movie.CHILDRENS);
        Movie m2 = new Movie("Movie-regular", Movie.REGULAR);
        Movie m3 = new Movie("Movie-new", Movie.NEW_RELEASE);

        Rental r1 = new Rental(m1, 2);
        Rental r2 = new Rental(m2, 4);
        Rental r3 = new Rental(m3, 6);

        customer.addRental(r1);
        customer.addRental(r2);
        customer.addRental(r3);

        System.out.println(customer.statement());
    }
}
