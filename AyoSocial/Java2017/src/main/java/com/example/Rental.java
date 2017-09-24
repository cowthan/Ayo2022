package com.example;

/**
 * Created by Administrator on 2017/7/23.
 */

public class Rental {

    private Movie _movie;
    private int _dayRented;

    public Rental(Movie _movie, int _dayRented) {
        this._movie = _movie;
        this._dayRented = _dayRented;
    }

    public int getDaysRented(){
        return _dayRented;
    }

    public Movie getMovie(){
        return _movie;
    }

    public double getCharge(){
        double result = 0;
        switch (getMovie().getPriceCode()){
            case Movie.REGULAR:
                //两天2块钱，多出一天多1块5
                result += 2;
                if(getDaysRented() > 2){
                    result += (getDaysRented() - 2) * 1.5;
                }
                break;
            case Movie.NEW_RELEASE:
                //一天3块
                result += getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                //3天1块5，多出一天多1.5
                result += 1.5;
                if(getDaysRented() > 3){
                    result += (getDaysRented() - 3) * 1.5;
                }
                break;
        }
        return result;
    }

    /**
     * 常客积分，根据租的类型和天数增加积分
     * @return
     */
    public int getFrequentRenterPoints(){
        if((getMovie().getPriceCode() == Movie.NEW_RELEASE) && getDaysRented() > 1){
            return 2;
        }else{
            return 1;
        }
    }
}
