package ePortfolio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePanel extends JPanel implements ActionListener, MessageListener{
    private Portfolio portfolio;

    private JPanel upperPanel;

    private JPanel leftOfUpperPanel;
    private JLabel panelTitle;

    private JPanel fieldListPanel;

    private JTextField symbolField;
    private JTextField nameField;
    private JTextField priceField;
    
    private JPanel rightOfUpperPanel;

    private JButton prevButton;
    private JButton nextButton;
    private JButton saveButton;

    private JPanel lowerPanel;
    private JTextArea messagesArea;

    public UpdatePanel(Portfolio portfolio) {
        this.portfolio = portfolio;
        portfolio.setMessageListener(this);

        setLayout(new GridLayout(2,1));
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5,5,5));   

        // UPPER PANEL

        upperPanel = new JPanel(new GridLayout(1,2));
        upperPanel.setBackground(Color.WHITE);

        // LEFT UPPER PANEL

        leftOfUpperPanel = new JPanel(new BorderLayout());
        leftOfUpperPanel.setBackground(Color.WHITE);

        panelTitle = new JLabel(" Updating Investments");
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
        symbolField.setEditable(false);

        JLabel nameFieldLabel = new JLabel("Name");
        nameFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 25));
        nameField.setFont(new Font("Helvetica", Font.PLAIN, 13));
        nameField.setEditable(false);

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
        fieldListPanel.add(nameFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldListPanel.add(priceFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(priceField, gbc);

        leftOfUpperPanel.add(panelTitle, BorderLayout.NORTH);
        leftOfUpperPanel.add(fieldListPanel, BorderLayout.CENTER);

        upperPanel.add(leftOfUpperPanel);

        // RIGHT UPPER PANEL

        rightOfUpperPanel = new JPanel(new GridLayout(3,1));
        rightOfUpperPanel.setBackground(Color.WHITE);

        // Create panels to wrap the buttons
        JPanel prevButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        prevButtonPanel.setBackground(Color.WHITE);
        JPanel nextButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        nextButtonPanel.setBackground(Color.WHITE);
        JPanel saveButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        saveButtonPanel.setBackground(Color.WHITE);

        prevButton = new JButton("Prev");
        prevButton.setFont(new Font("Helvetica", Font.PLAIN, 13));
        prevButton.setBackground(Color.WHITE);
        prevButton.addActionListener(this);

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Helvetica", Font.PLAIN, 13));
        nextButton.setBackground(Color.WHITE);
        nextButton.addActionListener(this);

        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Helvetica", Font.PLAIN, 13));
        saveButton.setBackground(Color.WHITE);
        saveButton.addActionListener(this);

        Dimension buttonSize = new Dimension(100, 35); // Width: 80, Height: 30
        prevButton.setPreferredSize(buttonSize);
        nextButton.setPreferredSize(buttonSize);
        saveButton.setPreferredSize(buttonSize);

        prevButtonPanel.add(prevButton);
        nextButtonPanel.add(nextButton);
        saveButtonPanel.add(saveButton);

        // Add the panels to the rightOfUpperPanel
        rightOfUpperPanel.add(prevButtonPanel);
        rightOfUpperPanel.add(nextButtonPanel);
        rightOfUpperPanel.add(saveButtonPanel);

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
        if(buttonCommand.equals("Prev")){
            portfolio.updateInvesments(0, false, false);
            
        } else if (buttonCommand.equals("Next")){
            portfolio.updateInvesments(0, true, false);

        } else if (buttonCommand.equals("Save")){
            double updatePrice = Double.parseDouble(priceField.getText().trim());
            portfolio.updateInvesments(updatePrice, true, true);
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
    public void setFields(String symbol, String name, String price) {
        symbolField.setText(symbol);
        nameField.setText(name);
        priceField.setText(price);
    }
}

