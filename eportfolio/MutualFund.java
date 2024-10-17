package eportfolio;

import java.util.*;

/**
 * This class is used for the mutual fund data, containing
 * mutual fund information for symbol, name, quantity, price, and bookvalue
 * 
 * @author Sebastian Tiriba
 */
public class MutualFund {
    private String symbol;
    private String name;
    private int quantity;
    private double price;
    private double bookValue;

    /**
     * This constructor is the default constructor and does not initialize any
     * data for the mutual fund
     */
    public MutualFund() {};

    /**
     * This constructor accepts data for the symbol, name
     * quantity, price and book value of a mutual fund and sets these
     * to the Mutual Fund object's symbol, name quantity, price and book value
     * 
     * @param symbol - string value for the symbol
     * @param name - string value for the name
     * @param quantity - int value for the quantity
     * @param price - double value for the price
     * @param bookValue - double for the book value
     */
    public MutualFund(String symbol, String name, int quantity, double price, double bookValue) {
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = bookValue;
    }

    /**
     * This method will return the current value of the mutual fund's symbol
     *
     * @return string value for the current symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * This method will set the current of value of the symbol to the string
     * value passed in
     *
     * @param symbol - value to set mutual fund's symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * This method will return the current value 
     * of the mutual fund's name
     *
     * @return string value for the current name
     */
    public String getName() {
        return name;
    }

    /**
     * This method will set the current of value of the name to the string value
     * passed in
     *
     * @param name - value to set mutual fund's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method will return the current value of the mutual fund's quanity
     *
     * @return int value for the current quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * This method will set the current of value of the quantity to the int
     * value passed in
     *
     * @param quantity - value to set mutual fund's quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * This method will return the current value of the mutual fund's price
     *
     * @return double value for the current price
     */
    public double getPrice() {
        return price;
    }

    /**
     * This method will set the current of value of the price to the double
     * value passed in
     *
     * @param price - value to set mutual fund's price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * This method will return the current value of the mutual fund's book value
     *
     * @return double value for the current bookValue
     */
    public double getBookValue() {
        return bookValue;
    }

    /**
     * This method will set the current of value of the bookValue to the double
     * value passed in
     *
     * @param bookValue - value to set mutual fund's bookValue
     */
    public void setBookValue(double bookValue) {
        this.bookValue = bookValue;
    }

    /**
     * This method performs a mutual fund buy, adding the quantity bought to the
     * previous quantity of the mutual fund, updating price at the time of purchase,
     * and calculates the bookvalue for the mutual with no fee, adding it to
     * the previous bookValue
     *
     * @param addedQuantity - the quantity of stock bought
     * @param newPrice - the price at which stock was bought
     * @return the bookvalue of the purchase (no fee)
     */
    public double buy(int addedQuantity, double newPrice){
        this.quantity += addedQuantity;
        double buyBookValue = addedQuantity * newPrice;
        this.bookValue += buyBookValue;
        this.price = newPrice;

        return buyBookValue;
    }

    /**
     * This method performs a mutual fund sell, either partial or full if the
     * quanitity parameter is not larger than the quantity of mutual fund owned
     *
     * FULL SALE: sets quantity and bookValue to zero and removes mutual fund
     *
     * PARTIAL SALE: sets remaining quanitity after sale, updates price to the
     * price at which mutual fund was sold, and calculates the book value of the mutual fund
     * remaining
     *
     * @param sellQuantity - the quantity of stock bought
     * @param salePrice - the price at which stock was bought
     * @param inputList - for removal of stock at full sale
     * @param i - index of arrayList of stocks to remove at full sale
     * @return the gain of the partial or full sale of the mutual fund
     */
    public double sell(int sellQuantity, double salePrice, ArrayList<MutualFund> inputList, int i){
        // vars for calculating book value 
        double payment = sellQuantity * salePrice - 9.99;
        double gain;

        // FULL SALE
        if(sellQuantity == this.quantity){
            gain = payment - this.bookValue;

            // reset mutual fund quantity and bookvalue after FULL SALE
            this.quantity = 0;
            this.bookValue = 0;

            // when full sale occurs, remove the mutual fund from array list
            inputList.remove(i);

            return gain;
        }
        // PARTIAL SALE
        else {
            // remaining quanitity after partial sale
            int remainingQuantity = this.quantity - sellQuantity;

            // calculate the bookValue for the remaining quantity, bookValueSold and gain for sale
            double bookValueRemaining = this.bookValue * (double)remainingQuantity / this.quantity;
            double bookValueSold = this.bookValue - bookValueRemaining;
            gain = payment - bookValueSold;

            // update quanitity and bookvalue after PARTIAL SALE
            this.quantity = remainingQuantity;
            this.bookValue = bookValueRemaining;
            this.price = salePrice;

            return gain;
        }
    }

    /**
     * This method calculates the theoretical gain of a mutual fund (as if it were a
     * full sale) and returns it to be printed in main (sale fee of 45.00
     * included)
     *
     * @param sellQuantity - the theoretical quantity being sold
     * @param salePrice - the theoretical sale price
     * @return theoretical gain of the mutual fund
     */
    public double getGain(int sellQuantity, double salePrice) {
        double payment = sellQuantity * salePrice - 45.00;

        // ASSUME FULL SALE FOR GET GAIN
        double gain = payment - this.bookValue;

        return gain;
    }

    /**
     * Displays all information of the stock neatly
     *
     * @return all information of the stock
     */
    @Override
    public String toString() {
        return name + " (" + symbol + ") | Owned: " + quantity + " | Price: $"
            + String.format("%.2f", price) + " | Book value: $" + String.format("%.2f", bookValue);
    }

    /**
     * Checks if two mutual funds are equal in terms
     * of all attributes
     * 
     * @param mutualFund is the other mutual fund in the comparison (fund1.equals(fund2))
     * @return true or falls depending if equal or not
     */
    public boolean equals(MutualFund mutualFund){
        // if empty cannot be equal
        if (mutualFund == null){
            return false;
        }
        // create an instance of the mutual fund argument by casting
        MutualFund fund = (MutualFund) mutualFund;
        // return true if all mutual fund paramters are equal
        return(fund.bookValue == this.bookValue && fund.name.equals(this.name) && fund.symbol.equals(this.symbol)
            && fund.price == this.price && fund.quantity == this.quantity);
    }
}