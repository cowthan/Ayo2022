package com.example;

/**
 * Created by Administrator on 2017/7/23.
 */

public class Movie {

    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;
    private int _priceCode;

    public Movie(String title, int priceCode){
        _title = title;
        _priceCode = priceCode;
    }

    public int getPriceCode(){
        return _priceCode;
    }

    public void setPriceCode(int priceCode) {
        this._priceCode = priceCode;
    }

    public String getTitle(){
        return _title;
    }


}
