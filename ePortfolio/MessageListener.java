package ePortfolio;

 /**
 * listener interface for ePortfolio panels and functions
 *
 */
public interface MessageListener {
    /**
     * Appends a message to the messages area.
     *
     * @param message the message to append
     */
    void appendMessage(String message);

    /**
     * Replaces the current content of the messages area with a new message.
     *
     * @param message the message to display
     */
    void setMessage(String message);

    /**
     * Sets the input fields with specified values.
     * 
     * This method is a placeholder to allow integration with other panels
     * or components that might pre-fill fields with specific data.
     *
     * @param symbol the symbol to set in the symbol field
     * @param name the name to set in the name field
     * @param price the price to set in the price field
     */
    void setFields(String symbol, String name, String price);
}
