package ePortfolio;

public interface MessageListener {
    void appendMessage(String message);
    void setMessage(String message);
    void setFields(String symbol, String name, String price);
}
