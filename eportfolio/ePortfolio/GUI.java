package ePortfolio;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener{

    private CardLayout cardLayout;
    private JPanel containerPanel;

    public GUI () {
        setTitle("Investment Portfolio");
        setSize(600, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up CardLayout
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        // Add panels
        containerPanel.add(new InitialPanel(), "Initial");
        containerPanel.add(new BuyPanel(), "Buy");
        // containerPanel.add(new SellPanel(), "Sell");
        // containerPanel.add(new UpdatePanel(), "Update");
        // containerPanel.add(new GainPanel(), "Gain");
        // containerPanel.add(new SearchPanel(), "Search");

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
            case "Gain":
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
}
