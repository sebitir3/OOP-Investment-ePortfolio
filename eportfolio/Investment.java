package eportfolio;

public class Investment {
    private String symbol;
    private String name;
    private int quantity;
    private double price;
    private double bookValue;

    /**
     * This constructor is the default constructor and does not initialize any
     * data for the investment
     */
    public Investment() {};


    /**
     * This constructor accepts data for the symbol, name
     * quantity, price and book value of a an investment and sets these
     * to the Investments object's symbol, name quantity, price and book value
     * 
     * @param symbol - string value for the symbol
     * @param name - string value for the name
     * @param quantity - int value for the quantity
     * @param price - double value for the price
     * @param bookValue - double for the book value
     */
    public Investment(String symbol, String name, int quantity, double price, double bookValue) {
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
}
