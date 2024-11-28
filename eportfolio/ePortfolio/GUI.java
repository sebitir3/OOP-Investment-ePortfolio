package ePortfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener{

    private CardLayout cardLayout;
    private JPanel containerPanel;

    private Portfolio portfolio;

    private BuyPanel buyPanel;
    private SellPanel sellPanel;
    private UpdatePanel updatePanel;
    private GainPanel gainPanel;
    private SearchPanel searchPanel;


    public GUI () {
        setTitle("Investment Portfolio");
        setSize(600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    private void addMenuItem(JMenu menu, String panelName) {
        JMenuItem menuItem = new JMenuItem(panelName);
        menuItem.addActionListener(this);

        menuItem.setBackground(Color.WHITE);
        menuItem.setFont(new Font("Helvetica", Font.PLAIN, 13));

        menu.add(menuItem);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }

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
                System.exit(0);
                break;
        }
    }

    // buy communication
    public void handleBuyAction(String message) {
        buyPanel.setMessage(message);
    }
    
    // sell communication
    public void handleSellAction(String message) {
        sellPanel.setMessage(message);
    }

    // update communication
    public void handleUpdateMessage(String message) {
        updatePanel.setMessage(message);
    }

    public void handleUpdateFields(String symbol, String name, String price) {
        updatePanel.setFields(symbol, name, price);;
    }

    // gain communication
    public void handleGainMessage(String message) {
        gainPanel.appendMessage(message);
    }

    public void clearGainMessage(String message) {
        gainPanel.setMessage(message);
    }

    public void handleGainFields(String symbol, String name, String price) {
        gainPanel.setFields(symbol, name, price);;
    }
}
