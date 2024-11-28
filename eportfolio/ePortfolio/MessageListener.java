package ePortfolio;

public interface MessageListener {
    void appendMessage(String message);
    void setFields(String symbol, String name, String price);
}
