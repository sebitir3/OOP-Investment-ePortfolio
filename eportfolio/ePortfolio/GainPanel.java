package ePortfolio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;

public class GainPanel extends JPanel implements MessageListener{
    private Portfolio portfolio;

    private JPanel upperPanel;
    private JLabel panelTitle;

    private JPanel fieldListPanel;
    private JTextField gainField;

    private JPanel lowerPanel;
    private JTextArea messagesArea;

    public GainPanel(Portfolio portfolio) {
        this.portfolio = portfolio;
        portfolio.setMessageListener(this);
        
        setLayout(new GridLayout(2,1));
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5,5,5));   

        // UPPER PANEL

        upperPanel = new JPanel(new BorderLayout());
        upperPanel.setBackground(Color.WHITE);

        panelTitle = new JLabel(" Getting Total Gain");
        panelTitle.setFont(new Font("Helvetica", Font.PLAIN, 13));
        
        fieldListPanel = new JPanel();
        fieldListPanel.setBackground(Color.WHITE);
        fieldListPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for form fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 15, 5); // Add space between components

        // Create labels and text fields for input
        JLabel gainFieldLabel = new JLabel("Total Gain");
        gainFieldLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        gainField = new JTextField();
        gainField.setPreferredSize(new Dimension(200, 25));
        gainField.setFont(new Font("Helvetica", Font.PLAIN, 13));
        gainField.setEditable(false);

        // Add labels and fields to fieldListPanel with GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldListPanel.add(gainFieldLabel, gbc);
        gbc.gridx = 1;
        fieldListPanel.add(gainField, gbc);

        upperPanel.add(panelTitle, BorderLayout.NORTH);
        upperPanel.add(fieldListPanel, BorderLayout.CENTER);
        
        // LOWER PANEL
        lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBackground(Color.WHITE);

        JLabel messagesLabel = new JLabel("Individual Gains");
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
    public void appendMessage(String message) {
        messagesArea.append(message + "\n");
    }

    @Override
    public void setMessage(String message) {
        messagesArea.setText("");
    }

    @Override
    public void setFields(String symbol, String name, String price) {
        gainField.setText(price);
    }
}

