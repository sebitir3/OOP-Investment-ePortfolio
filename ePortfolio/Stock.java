package ePortfolio;

import java.util.*;

/**
 * This class is used for the stock data, containing
 * stock information for symbol, name, quantity, price, and bookvalue
 * 
 * @author Sebastian Tiriba
 */
public class Stock extends Investment{
    // fee constant
    private static final double FEE = 9.99;

    /**
     * This constructor is the default constructor and does not initialize
     * any data for the stock
    */
    public Stock() {};

    /**
     * This constructor accepts data for the symbol, name
     * quantity, price and book value of a stock and sets these
     * to the Stock object's symbol, name quantity, price and book value
     * 
     * @param symbol - string value for the symbol
     * @param name - string value for the name
     * @param quantity - int value for the quantity
     * @param price - double value for the price
     * @param bookValue - double for the book value
     */
    public Stock(String symbol, String name, int quantity, double price, double bookValue) {
        super(symbol, name, quantity, price, bookValue);
    }

    /**
     * This method performs a stock buy, 
     * adding the quantity bought to the previous quantity of the stock, 
     * updating price at the time of purchase, and calculates the bookvalue
     * for the stock with fee of 9.99, adding it to the previous bookValue
     * 
     * @param addedQuantity - the quantity of stock bought
     * @param newPrice - the price at which stock was bought
     * @return the bookvalue of the purchase (fee of $9.99)
     */
    public double buy(int addedQuantity, double newPrice) {
        if(addedQuantity <= 0){
            throw new IllegalArgumentException("Please enter a positive quantity.");
        }
        if(newPrice <= 0){
            throw new IllegalArgumentException("Please enter a positive price.");
        }
        this.quantity += addedQuantity;
        double buyBookValue = addedQuantity * newPrice + FEE;
        this.bookValue += buyBookValue;
        this.price = newPrice;

        return buyBookValue;
    }

    /**
     * This method performs a stock sell, either partial or full
     * if the quanitity parameter is not larger than the quantity of
     * stock owned
     * 
     * FULL SALE: sets quantity and bookValue to zero and removes stock
     * 
     * PARTIAL SALE: sets remaining quanitity after sale, updates
     * price to the price at which stock was sold, and calculates 
     * the remaining book value of the stock
     *
     * @param sellQuantity - the quantity of stock bought
     * @param salePrice - the price at which stock was bought
     * @param inputList - for removal of stock at full sale
     * @param i - index of arrayList of stocks to remove at full sale
     * @return the gain of the partial or full sale of the stock
     */
    public double sell(int sellQuantity, double salePrice, ArrayList<Investment> inputList, int i) {
        if(sellQuantity <= 0){
            throw new IllegalArgumentException("Please enter a positive quantity.");
        }
        if(salePrice <= 0){
            throw new IllegalArgumentException("Please enter a positive price.");
        }
        
        // vars for calculating book value 
        double payment = sellQuantity * salePrice - FEE;
        double gain;

        // FULL SALE
        if (sellQuantity == this.quantity) {
            gain = payment - this.bookValue;
            
            // reset mutual fund quantity and bookvalue after FULL SALE
            this.quantity = 0;
            this.bookValue = 0;

            // when full sale occurs, remove the stock from array list
            inputList.remove(i);

            return gain;
        } 
        // PARTIAL SALE
        else {
            // remaining quanitity after partial sale
            int remainingQuantity = this.quantity - sellQuantity;

            // calculate the bookValue for the remaining quantity, bookValueSold and gain for sale
            double bookValueRemaining = this.bookValue * (double) remainingQuantity / this.quantity;
            double bookValueSold = this.bookValue - bookValueRemaining;
            gain = payment - bookValueSold;

            // update quanitity, bookvalue, and price after PARTIAL SALE
            this.quantity = remainingQuantity;
            this.bookValue = bookValueRemaining;
            this.price = salePrice;

            return gain;
        }
    }

    /**
     * This method returns the payment of the sale of a stock
     * 
     * @param sellQuantity - the theoretical quantity being sold
     * @param salePrice - the theoretical sale price
     * @return theoretical gain of the stock
     */
    public double getPayment(int sellQuantity, double salePrice){
        double payment = sellQuantity * salePrice - FEE;
        return payment;
    }

    /**
     * This method calculates the theoretical gain of a
     * stock (as if it were a full sale) and returns it
     * to be printed in main (sale fee of 9.99 included)
     * 
     * @param sellQuantity - the theoretical quantity being sold
     * @param salePrice - the theoretical sale price
     * @return theoretical gain of the stock
     */
    public double getGain(int sellQuantity, double salePrice) {
        double payment = sellQuantity * salePrice - FEE;

        // ASSUME FULL SALE FOR GET GAINq
        double gain = payment - this.bookValue;
        return gain;
    }

    /**
     * Displays all information of the stock neatly
     * 
     * @return all information of the stock
     */
    @Override
    public String toString(){
        return getName() + " (" + getSymbol() + ") | Owned: " + quantity + " | Price: $" + 
            String.format("%.2f", price) + " | Book value: $" + String.format("%.2f", bookValue); 
    }

    /**
     * Checks if two stocks are equal in terms of all attributes
     *
     * @param stockCompare is the other stock in the comparison
     * (stock1.equals(compareStock))
     * @return true or falls depending if equal or not
     */
    public boolean equals(Stock stockCompare) {
        // if empty cannot be equal
        if (stockCompare == null) {
            return false;
        }
        // create an instance of the mutual fund argument by casting
        Stock stock = (Stock) stockCompare;
        
        // return true if all mutual fund paramters are equal
        return (stock.bookValue == this.bookValue && stock.name.equals(this.name) && stock.symbol.equals(this.symbol)
                && stock.price == this.price && stock.quantity == this.quantity);
    }

}
