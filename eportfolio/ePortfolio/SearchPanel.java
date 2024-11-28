package ePortfolio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends JPanel implements ActionListener, MessageListener{
    private Portfolio portfolio;

    private JPanel upperPanel;

    private JPanel leftOfUpperPanel;
    private JLabel panelTitle;

    private JPanel fieldListPanel;

    private JTextField symbolField;
    private JTextField nameKeyField;
    private JTextField lowField;
    private JTextField highField;
    
    private JPanel rightOfUpperPanel;

    private JButton resetButton;
    private JButton searchButton;

    private JPanel lowerPanel;

    private JTextArea messagesArea;

    public SearchPanel(Portfolio portfolio) {
        this.portfolio = portfolio;
        //portfolio.setMessageListener(this);

        setLayout(new GridLayout(2,1));
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5,5,5));   

        // UPPER PANEL

        upperPanel = new JPanel(new GridLayout(1,2));
        upperPanel.setBackground(Color.WHITE);

        // LEFT OF UPPER

        leftOfUpperPanel = new JPanel(new BorderLayout());
        leftOfUpperPanel.setBackground(Color.WHITE);

        panelTitle = new JLabel(" Searching Investments");
        panelTitle.setFont(new Font("Helvetica", Font.PLAIN, 13));
        
        fieldListPanel = new JPanel();
        fieldListPanel.setBackground(Color.WHITE);
        fieldListPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for form fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add space between components
        gbc.weightx = 1.0;


        JLabel symbolFieldLabel = new JLabel("Symbol");
        symbolFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        symbolField = new JTextField();
        symbolField.setPreferredSize(new Dimension(200, 25));
        symbolField.setFont(new Font("Helvetica", Font.PLAIN, 13));

        JLabel nameKeyFieldLabel = new JLabel("Name /");
        nameKeyFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        JLabel nameKey2FieldLabel = new JLabel("Keywords");
        nameKey2FieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        nameKeyField = new JTextField();
        nameKeyField.setPreferredSize(new Dimension(200, 25));
        nameKeyField.setFont(new Font("Helvetica", Font.PLAIN, 13));

        JLabel lowFieldLabel = new JLabel("Low Price");
        lowFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        lowField = new JTextField();
        lowField.setPreferredSize(new Dimension(200, 25));
        lowField.setFont(new Font("Helvetica", Font.PLAIN, 13));

        JLabel highFieldLabel = new JLabel("High Price");
        highFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        highField = new JTextField();
        highField.setPreferredSize(new Dimension(200, 25));
        highField.setFont(new Font("Helvetica", Font.PLAIN, 13));

        // Add labels and fields to fieldListPanel with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldListPanel.add(symbolFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(symbolField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldListPanel.add(nameKeyFieldLabel, gbc);
        
        gbc.insets = new Insets(0, 5, 5, 5); // Reduce the top inset to bring labels closer vertically
        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldListPanel.add(nameKey2FieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(nameKeyField, gbc);

        gbc.insets = new Insets(5, 5, 5, 5); // Reset insets to default for other components
        gbc.gridx = 0;
        gbc.gridy = 3;
        fieldListPanel.add(lowFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(lowField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        fieldListPanel.add(highFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(highField, gbc);

        leftOfUpperPanel.add(panelTitle, BorderLayout.NORTH);
        leftOfUpperPanel.add(fieldListPanel, BorderLayout.CENTER);

        upperPanel.add(leftOfUpperPanel);

        // RIGHT UPPER PANEL

        rightOfUpperPanel = new JPanel(new GridLayout(2,1));
        rightOfUpperPanel.setBackground(Color.WHITE);

        // Create panels to wrap the buttons
        JPanel resetButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        resetButtonPanel.setBackground(Color.WHITE);
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        searchButtonPanel.setBackground(Color.WHITE);

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Helvetica", Font.PLAIN, 13));
        resetButton.setBackground(Color.WHITE);
        resetButton.addActionListener(this);

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Helvetica", Font.PLAIN, 13));
        searchButton.setBackground(Color.WHITE);
        searchButton.addActionListener(this);

        Dimension buttonSize = new Dimension(100, 35); // Width: 80, Height: 30
        resetButton.setPreferredSize(buttonSize);
        searchButton.setPreferredSize(buttonSize);

        resetButtonPanel.add(resetButton);
        searchButtonPanel.add(searchButton);

        // Add the panels to the rightOfUpperPanel
        rightOfUpperPanel.add(resetButtonPanel);
        rightOfUpperPanel.add(searchButtonPanel);

        upperPanel.add(rightOfUpperPanel);
        
        // LOWER PANEL

        lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBackground(Color.WHITE);

        JLabel messagesLabel = new JLabel("Search Results");
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
            nameKeyField.setText("");
            lowField.setText("");
            highField.setText("");
        } else if (buttonCommand.equals("Search")){
            //try {
                String symbol = symbolField.getText().trim();
                String searchKey = nameKeyField.getText().trim().toLowerCase();
                String low = lowField.getText().trim();
                String high = highField.getText().trim();

                symbolField.setText("");
                nameKeyField.setText("");
                lowField.setText("");
                highField.setText("");
        
                // Call the Portfolio's buyInvestments method
                portfolio.searchInvestments(symbol, searchKey, low, high);
        
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
        messagesArea.setText(message);
    }

    @Override
    public void setFields(String symbol, String name, String price) {}
}

