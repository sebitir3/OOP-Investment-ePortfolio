package ePortfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to implement all panels through a menu and handle file reading and writing.
 */
public class GUI extends JFrame implements ActionListener{

    private CardLayout cardLayout;
    private JPanel containerPanel;

    private static Portfolio portfolio;
    private static String filePath;
    
    private BuyPanel buyPanel;
    private SellPanel sellPanel;
    private UpdatePanel updatePanel;
    private GainPanel gainPanel;
    private SearchPanel searchPanel;

     /**
     * Constructs the GUI for the ePortfolio
     */
    public GUI () {
        setTitle("ePortfolio");
        setSize(600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up CardLayout
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        portfolio = new Portfolio();
        portfolio.setGUI(this);

        buyPanel = new BuyPanel(portfolio);
        sellPanel = new SellPanel(portfolio);
        updatePanel = new UpdatePanel(portfolio);
        gainPanel = new GainPanel(portfolio);
        searchPanel = new SearchPanel(portfolio);

        // Add panels
        containerPanel.add(new InitialPanel(), "Initial");
        containerPanel.add(buyPanel, "Buy");
        containerPanel.add(sellPanel, "Sell");
        containerPanel.add(updatePanel, "Update");
        containerPanel.add(gainPanel, "Gain");
        containerPanel.add(searchPanel, "Search");

        add(containerPanel, BorderLayout.CENTER);

        // Set up the menu bar
        setupMenuBar();
    }

    /**
     * Creates the menu bar for the GUI
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu commandsMenu = new JMenu("Commands");

        menuBar.setBackground(Color.WHITE);

        commandsMenu.setBackground(Color.WHITE);
        commandsMenu.setFont(new Font("Helvetica", Font.PLAIN, 13));
        

        // Add menu items
        addMenuItem(commandsMenu, "Buy");
        addMenuItem(commandsMenu, "Sell");
        addMenuItem(commandsMenu, "Update");
        addMenuItem(commandsMenu, "Gain");
        addMenuItem(commandsMenu, "Search");
        addMenuItem(commandsMenu, "Quit");

        // Add "Commands" menu to the menu bar
        menuBar.add(commandsMenu);

        // Set the menu bar
        setJMenuBar(menuBar);
    }

    /**
     * Adds options to the menu
     * 
     * @param menu instance of the menu
     * @param panelName name of each option that leads to a panel when clicked
     */
    private void addMenuItem(JMenu menu, String panelName) {
        JMenuItem menuItem = new JMenuItem(panelName);
        menuItem.addActionListener(this);

        menuItem.setBackground(Color.WHITE);
        menuItem.setFont(new Font("Helvetica", Font.PLAIN, 13));

        menu.add(menuItem);
    }
    
    /**
     * Main method that creates GUI window and reads from file
     * 
     * @param args command line arguments used for filename to read from
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);

            // FILE PATH FOR READING AND WRITING

            // set the current working directory
            String currentDir = new File(".").getAbsolutePath();
 
            // for incorrectly specified .txt filename
            // write to default
            boolean isDefaultCase;

            // no file specified in command line
            if (args.length < 1) {
                filePath = currentDir + "/ePortfolio/default.txt";
                File file = new File(filePath);
                if (!file.exists()) {
                    try {
                        // Create the file if it doesn't exist
                        file.getParentFile().mkdirs();  // Ensure the directory exists
                        file.createNewFile();
                        //System.out.println("New file created at: " + file.getAbsolutePath());
                    } catch (IOException e) {
                        System.out.println("Error creating file.");
                        System.exit(1);
                    }
                }
            } else {
                filePath = currentDir + "/ePortfolio/" + args[0] + ".txt";
            }

            // load in investments from file
            // loadInvestments returns false when error reading file occurs
            isDefaultCase = portfolio.loadInvestments(filePath);
            if(!isDefaultCase){
                filePath = currentDir + "/ePortfolio/default.txt";
            }
        });
    }

    /**
     * Reacts to menu button events
     *
     * @param e event captured from buttons
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionString = e.getActionCommand();
        switch(actionString) {
            case "Buy":
            case "Sell":
            case "Update":
                portfolio.initialInvestmentShown();
            case "Gain":
                portfolio.getInvestmentGain();
            case "Search":
                // Switch to the corresponding panel
                cardLayout.show(containerPanel, actionString);
                break;
            case "Quit":
                // Exit the program
                portfolio.writeInvestments(filePath);
                System.exit(0);
                break;
        }
    }

    // BUY COMMUNICATION

    /**
     * Sends messages to buyPanel text area
     *
     * @param message to be sent
     */
    public void handleBuyAction(String message) {
        buyPanel.setMessage(message);
    }
    
    // SELL COMMUNICATION
    /**
     * Sends messages to sellPanel text area
     *
     * @param message to be sent
     */
    public void handleSellAction(String message) {
        sellPanel.setMessage(message);
    }

    // UPDATE COMMUNICATION
    /**
     * Sends messages to updatePanel text area
     *
     * @param message to be sent
     */
    public void handleUpdateMessage(String message) {
        updatePanel.setMessage(message);
    }

    /**
     * Updates the symbol, name and price fields in updatePanel
     *
     * @param symbol string to be sent to field
     * @param name string to be sent to field
     * @param price string to be sent to field
     */
    public void handleUpdateFields(String symbol, String name, String price) {
        updatePanel.setFields(symbol, name, price);;
    }

    // GAIN COMMUNICATION
    /**
     * Appends messages to gainPanel text area
     *
     * @param message to be sent
     */
    public void handleGainMessage(String message) {
        gainPanel.appendMessage(message);
    }

    /**
     * Sets messages to gainPanel text area
     *
     * @param message to be sent
     */
    public void clearGainMessage(String message) {
        gainPanel.setMessage(message);
    }

    /**
     * Updates the symbol, name and price fields in gainPanel
     *
     * @param symbol string to be sent to field
     * @param name string to be sent to field
     * @param price string to be sent to field
     */
    public void handleGainFields(String symbol, String name, String price) {
        gainPanel.setFields(symbol, name, price);;
    }

    // SEARCH COMMUNICATION
    /**
     * Sets messages to searchPanel text area
     *
     * @param message to be sent
     */
    public void setSearchMessage(String message) {
        searchPanel.setMessage(message);
    }

    /**
     * Apppends messages to searchPanel text area
     *
     * @param message to be sent
     */
    public void handleSearchMessage(String message) {
        searchPanel.appendMessage(message);
    }
}
