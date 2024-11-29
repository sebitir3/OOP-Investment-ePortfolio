package ePortfolio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The SellPanel class represents the panel in the GUI where users can sell investments.
 */
public class SellPanel extends JPanel implements ActionListener, MessageListener{
    /**
     * The portfolio that this panel interacts with to perform sell operations
     */
    private Portfolio portfolio;

    /**
     * The upper section of the panel containing input fields and buttons
     */
    private JPanel upperPanel;

    /**
     * The left portion of the upper panel containing the title and input fields
     */
    private JPanel leftOfUpperPanel;

    /**
     * The title label displayed at the top of the left upper panel
     */
    private JLabel panelTitle;

    /**
     * The panel containing the input fields for symbol, quantity, and price
     */
    private JPanel fieldListPanel;

    /**
     * The text field for the user to input the symbol of the investment they want to sell
     */
    private JTextField symbolField;

    /**
     * The text field for the user to input the quantity of the investment they want to sell
     */
    private JTextField quantityField;

    /**
     * The text field for the user to input the price of the investment they want to sell
     */
    private JTextField priceField;

    /**
     * The right portion of the upper panel containing the reset and sell buttons
     */
    private JPanel rightOfUpperPanel;

    /**
     * The button to reset all input fields to their default (empty) state
     */
    private JButton resetButton;

    /**
     * The button to initiate the "sell" operation for an investment
     */
    private JButton sellButton;

    /**
     * The lower section of the panel containing the messages area
     */
    private JPanel lowerPanel;

    /**
     * The text area for displaying messages or feedback to the user
     */
    private JTextArea messagesArea;

    /**
     * Constructs SellPanel for the GUI
     *
     * @param portfolio instance to call functions
     */
    public SellPanel(Portfolio portfolio) {
        this.portfolio = portfolio;
        portfolio.setMessageListener(this);

        setLayout(new GridLayout(2,1));
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5,5,5));   

        // UPPER PANEL

        upperPanel = new JPanel(new GridLayout(1,2));
        upperPanel.setBackground(Color.WHITE);

        // LEFT OF UPPER

        leftOfUpperPanel = new JPanel(new BorderLayout());
        leftOfUpperPanel.setBackground(Color.WHITE);

        panelTitle = new JLabel(" Selling an Investment");
        panelTitle.setFont(new Font("Helvetica", Font.PLAIN, 13));
        
        fieldListPanel = new JPanel();
        fieldListPanel.setBackground(Color.WHITE);
        fieldListPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for form fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 15, 5); // Add space between components

        // Create labels and text fields for input
        JLabel symbolFieldLabel = new JLabel("Symbol");
        symbolFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        symbolField = new JTextField();
        symbolField.setPreferredSize(new Dimension(200, 25));
        symbolField.setFont(new Font("Helvetica", Font.PLAIN, 13));

        JLabel quantityFieldLabel = new JLabel("Quantity");
        quantityFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        quantityField = new JTextField();
        quantityField.setPreferredSize(new Dimension(200, 25));
        quantityField.setFont(new Font("Helvetica", Font.PLAIN, 13));

        JLabel priceFieldLabel = new JLabel("Price");
        priceFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(200, 25));
        priceField.setFont(new Font("Helvetica", Font.PLAIN, 13));

        // Add labels and fields to fieldListPanel with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldListPanel.add(symbolFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(symbolField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldListPanel.add(quantityFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldListPanel.add(priceFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(priceField, gbc);

        leftOfUpperPanel.add(panelTitle, BorderLayout.NORTH);
        leftOfUpperPanel.add(fieldListPanel, BorderLayout.CENTER);

        upperPanel.add(leftOfUpperPanel);

        // LEFT UPPER PANEL

        rightOfUpperPanel = new JPanel(new GridLayout(2,1));
        rightOfUpperPanel.setBackground(Color.WHITE);

        // Create panels to wrap the buttons
        JPanel resetButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        resetButtonPanel.setBackground(Color.WHITE);
        JPanel sellButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        sellButtonPanel.setBackground(Color.WHITE);

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Helvetica", Font.PLAIN, 13));
        resetButton.setBackground(Color.WHITE);
        resetButton.addActionListener(this);

        sellButton = new JButton("Sell");
        sellButton.setFont(new Font("Helvetica", Font.PLAIN, 13));
        sellButton.setBackground(Color.WHITE);
        sellButton.addActionListener(this);

        Dimension buttonSize = new Dimension(100, 35); // Width: 80, Height: 30
        resetButton.setPreferredSize(buttonSize);
        sellButton.setPreferredSize(buttonSize);

        resetButtonPanel.add(resetButton);
        sellButtonPanel.add(sellButton);

        // Add the panels to the rightOfUpperPanel
        rightOfUpperPanel.add(resetButtonPanel);
        rightOfUpperPanel.add(sellButtonPanel);

        upperPanel.add(rightOfUpperPanel);
        
        // LOWER PANEL

        lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBackground(Color.WHITE);

        JLabel messagesLabel = new JLabel("Messages");
        messagesLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        messagesLabel.setBackground(Color.WHITE);
        
        messagesArea = new JTextArea(6,50);
        messagesArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(5,5,5,5)
        ));
        messagesArea.setFont(new Font("Helvetica", Font.PLAIN, 13));
        messagesArea.setEditable(false);


        // scroll pane
        JScrollPane scrollBars = new JScrollPane(messagesArea);
        scrollBars.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollBars.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        lowerPanel.add(messagesLabel, BorderLayout.NORTH);
        lowerPanel.add(scrollBars, BorderLayout.CENTER);

        add(upperPanel); 
        add(lowerPanel);
    }

     /**
     * Reacts to button events
     *
     * @param e event captured from buttons
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonCommand = e.getActionCommand();
        if(buttonCommand.equals("Reset")){
            symbolField.setText("");
            quantityField.setText("");
            priceField.setText("");
        } else if (buttonCommand.equals("Sell")){
            try {
                String symbol = symbolField.getText().trim();
                String quantityText = quantityField.getText().trim();
                String priceText = priceField.getText().trim();

                if(symbol.isEmpty()){
                    throw new IllegalArgumentException("Symbol cannot be left blank.");
                }
                if(quantityText.isEmpty()){
                    throw new IllegalArgumentException("Quantity cannot be left blank.");
                }
                if(priceText.isEmpty()){
                    throw new IllegalArgumentException("Price cannot be left blank.");
                }

                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                
                // Call the Portfolio's sellInvestments method
                portfolio.sellInvestments(symbol, quantity, price);

                symbolField.setText("");
                quantityField.setText("");;
                priceField.setText("");;
        
                // Provide feedback to the user
            } catch (IllegalArgumentException em) {
                messagesArea.setText("Error: " + em.getMessage() + "\n");
            }
        }
    }

    /**
     * Appends a message to the messages area.
     *
     * @param message the message to append
     */
    @Override
    public void appendMessage(String message) {
        messagesArea.append(message + "\n");
    }

    /**
     * Replaces the current content of the messages area with a new message.
     *
     * @param message the message to display
     */
    @Override
    public void setMessage(String message) {
        messagesArea.setText(message + "\n");
    }
    
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
    @Override
    public void setFields(String symbol, String name, String price) {}
}

