package ePortfolio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellPanel extends JPanel implements ActionListener, MessageListener{
    private Portfolio portfolio;

    private JPanel upperPanel;

    private JPanel leftOfUpperPanel;
    private JLabel panelTitle;

    private JPanel fieldListPanel;


    private JTextField symbolField;

    private JTextField quantityField;
    private JTextField priceField;
    
    private JPanel rightOfUpperPanel;

    private JButton resetButton;
    private JButton sellButton;

    private JPanel lowerPanel;

    private JTextArea messagesArea;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonCommand = e.getActionCommand();
        if(buttonCommand.equals("Reset")){
            symbolField.setText("");
            quantityField.setText("");
            priceField.setText("");
        } else if (buttonCommand.equals("Sell")){
            //try {
                String symbol = symbolField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                symbolField.setText("");
                quantityField.setText("");;
                priceField.setText("");;
        
                // Call the Portfolio's buyInvestments method
                portfolio.sellInvestments(symbol, quantity, price);
        
                // Provide feedback to the user
            //} catch (NumberFormatException ex) {
                //messagesArea.append("Invalid input. Please check your fields.\n");
            //}
        }
    }

    @Override
    public void appendMessage(String message) {
        messagesArea.append(message + "\n");
    }

    @Override
    public void setMessage(String message) {
        messagesArea.setText(message + "\n");
    }

    @Override
    public void setFields(String symbol, String name, String price) {}
}

