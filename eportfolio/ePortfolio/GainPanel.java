package ePortfolio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;

/**
 * The GainPanel class represents the panel in the GUI where users can see the gain on they're investments.
 */
public class GainPanel extends JPanel implements MessageListener{
    /**
     * The portfolio that this panel interacts with to perform gain calculations
     */
    private Portfolio portfolio;

    /**
     * The upper section of the panel containing the title and gain field
     */
    private JPanel upperPanel;

    /**
     * The title label displayed at the top of the upper panel
     */
    private JLabel panelTitle;

    /**
     * The panel containing the input or display fields for portfolio gain
     */
    private JPanel fieldListPanel;

    /**
     * The text field for displaying the total gain of the portfolio
     */
    private JTextField gainField;

    /**
     * The lower section of the panel containing the messages area
     */
    private JPanel lowerPanel;

    /**
     * The text area for displaying messages or feedback to the user
     */
    private JTextArea messagesArea;

    /**
     * Constructs GainPanel for the GUI.
     *
     * @param portfolio instance to call functions
     */
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
        messagesArea.setText("");
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
    public void setFields(String symbol, String name, String price) {
        gainField.setText(price);
    }
}

