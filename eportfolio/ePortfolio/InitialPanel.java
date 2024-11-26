package ePortfolio;

import javax.swing.*;
import java.awt.*;

public class InitialPanel extends JPanel{
    public InitialPanel() {
        setSize(600, 400);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));   
        
        JTextField welcome = new JTextField();
        JTextArea welcomeInfo = new JTextArea(3,20);

        welcome.setText("Welcome to ePortfolio.");
        welcome.setEditable(false);
        welcome.setFont(new Font("Helvetica", Font.PLAIN, 13));
        welcome.setBackground(Color.WHITE);
        welcome.setBorder(null);
        
        welcomeInfo.setText("Choose a command from the “Commands” menu to buy or sell an investment, update prices for all investments, get gain for the portfolio, search for relevant investments, or quit the program.");
        welcomeInfo.setWrapStyleWord(true);
        welcomeInfo.setLineWrap(true);
        welcomeInfo.setEditable(false);
        welcomeInfo.setSize(560, 300);
        welcomeInfo.setFont(new Font("Helvetica", Font.PLAIN, 13));

        add(welcome); 
        add(welcomeInfo);
    }
}
