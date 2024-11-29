package ePortfolio;

import java.io.*;
import java.math.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * This class is used to perform different operations on
 * an ePortfolio of stocks and mutual funds
 *
 * @author Sebastian Tiriba
 */
public class Portfolio{
    // vars for investments
    public ArrayList<Investment> listOfInvestments = new ArrayList<>();
    private static double gainSum = 0;

    // keyword hashmap
    private static HashMap<String, ArrayList<Integer>> index = new HashMap<String,ArrayList<Integer>>();
    private static ArrayList<Integer> keywordIndices = new ArrayList<>();

    // decimal format for getGain
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private MessageListener messageListener;

    private int investmentPos = 0;

    private GUI gui;
    
    /**
     * This methods creates an instance of a GUI to send of messages to the GUI class
     * 
     * @param gui instance of a GUI
     */
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * This methods itializes the message listener
     * 
     * @param listener the message listener
     */
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    // BUY A STOCK OR MUTUAL FUND

    /**
     * This methods allows the user to buy a stock or a mutual fund
     * 
     * @param buyInvestType string from buyPanel combo box
     * @param symbolToBuy string from buyPanel textfield
     * @param nameToBuy string from buyPanel textfield
     * @param quantityToBuy int from buyPanel textfield
     * @param priceToBuy double from buyPanel textfield
     */
    public void buyInvestments(String buyInvestType, String symbolToBuy, String nameToBuy, int quantityToBuy, double priceToBuy){
        try{     
            // condition if investment symbol is a different investment type
            boolean duplicateSymbol = false;

            // condition if an investment is found
            boolean investmentFound = false;
            // book value when purchased
            double purchaseBookValue = 0;

            // iterate through all investments
            for (int i = 0; i < listOfInvestments.size(); i++) {
                if(!investmentFound && !duplicateSymbol){
                    // IF STOCK
                    if ((buyInvestType.equalsIgnoreCase("stock"))) {
                        investmentFound = false;

                        // check if the symbol is a mutual fund and print error if true
                        if (symbolToBuy.equals(listOfInvestments.get(i).getSymbol()) && listOfInvestments.get(i) instanceof MutualFund){
                            gui.handleBuyAction("Error. This symbol already exists as a mutual fund.");
                            duplicateSymbol = true;
                        } 
                        // find stock in investment list and buy stock
                        else if (listOfInvestments.get(i) instanceof Stock && listOfInvestments.get(i).getSymbol().equals(symbolToBuy)) {
                            Stock stockInList = (Stock)listOfInvestments.get(i);
                            investmentFound = true;      
                            // buy shares of a stock
                            purchaseBookValue = stockInList.buy(quantityToBuy, priceToBuy);
                            if (quantityToBuy != 1){
                                gui.handleBuyAction(quantityToBuy + " shares of " + listOfInvestments.get(i).getName() 
                                    + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                            } else {
                                gui.handleBuyAction(quantityToBuy + " share of " + listOfInvestments.get(i).getName() 
                                    + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                            }
                        }    
                    // IF MUTUAL FUND          
                    } else if (buyInvestType.equalsIgnoreCase("mutual fund")) {
                        investmentFound = false;
                
                        // check if the symbol is a stock and print error if true
                        if (listOfInvestments.get(i) instanceof Stock && symbolToBuy.equals(listOfInvestments.get(i).getSymbol())){
                            System.out.println("Error. This symbol already exists as a stock.");
                            duplicateSymbol = true;
                        } 
                        // check if mutual fund already exists in array list
                        else if (listOfInvestments.get(i) instanceof MutualFund && listOfInvestments.get(i).getSymbol().equals(symbolToBuy)) {
                            investmentFound = true;
                            MutualFund fundInList = (MutualFund)listOfInvestments.get(i);

                            // buy shares of a stock
                            purchaseBookValue = fundInList.buy(quantityToBuy, priceToBuy);
                            if(quantityToBuy != 1){
                                gui.handleBuyAction(quantityToBuy + " units of " + fundInList.getName() 
                                    + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                            } else {
                                gui.handleBuyAction(quantityToBuy + " unit of " + fundInList.getName() 
                                    + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                            }                                   
                        }
                    }
                }
            }

            // buy NEW investment
            if(!investmentFound && !duplicateSymbol) {
                // NEW STOCK
                if(buyInvestType.equalsIgnoreCase("stock")){    
                    Stock inputStock = new Stock();
                    inputStock.setSymbol(symbolToBuy);
                    inputStock.setName(nameToBuy);
            
                    // buy shares of the stock (sets quantity, price, and bookvalue)
                    purchaseBookValue = inputStock.buy(quantityToBuy, priceToBuy);
                    if(quantityToBuy != 1){
                        gui.handleBuyAction(quantityToBuy + " shares of " + nameToBuy + " (" + symbolToBuy 
                            + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    } else {
                        gui.handleBuyAction(quantityToBuy + " share of " + nameToBuy + " (" + symbolToBuy 
                            + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    }
                    // add stock to array list
                    listOfInvestments.add(inputStock);
                } 
                // NEW MUTUAL FUND
                else if (buyInvestType.equalsIgnoreCase("mutual fund")){
                    MutualFund inputMutualFund = new MutualFund();
                    inputMutualFund.setSymbol(symbolToBuy);
                    inputMutualFund.setName(nameToBuy);
                
                    // buy shares of the stock (sets quantity, price, and bookvalue)
                    purchaseBookValue = inputMutualFund.buy(quantityToBuy, priceToBuy);
                    if (quantityToBuy != 1){
                        gui.handleBuyAction(quantityToBuy + " units of " + nameToBuy + " (" 
                            + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    } else {
                        gui.handleBuyAction(quantityToBuy + " unit of " + nameToBuy + " (" 
                            + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    }
                    listOfInvestments.add(inputMutualFund);
                }
            }
            // update hashmap after buying
            updateHash(listOfInvestments, index, keywordIndices);
        } catch (IllegalArgumentException e) {
            gui.handleBuyAction("Error: " + e.getMessage() + "\n");
        }
        
    }
       
    // SELL AN INVESTMENT
    /**
     * This methods allows the user to sell a stock or a mutual fund
     *
     * @param symbolToSell string from sellPanel textfield
     * @param quantityToSell int from sellPanel textfield
     * @param priceToSell double from sellPanel textfield
     */
    public void sellInvestments(String symbolToSell, int quantityToSell, double priceToSell){
        try {
            boolean sellFound = false;

            // book value @ sale
            double saleBookValue;

            if(!listOfInvestments.isEmpty()){
                // iterate through all investments
                for (int i = 0; i < listOfInvestments.size(); i++){
                    if(!sellFound){
                        if (listOfInvestments.get(i) instanceof Stock && listOfInvestments.get(i).getSymbol().equals(symbolToSell)) {
                            // a sale is found
                            sellFound = true;

                            // create stock object
                            Stock stockSold = (Stock) listOfInvestments.get(i);
                        
                            // check if quantity to sell is larger than owned stock
                            if(quantityToSell > stockSold.getQuantity()){
                                gui.handleSellAction("Error: Not enough shares owned to sell.");
                            } else {
                                // for full purchase, index is removed so we must store the name of the stock in a temp var
                                String tempName = stockSold.getName();
                                
                                // perform the sale    
                                saleBookValue = stockSold.sell(quantityToSell, priceToSell, listOfInvestments, i);

                                // get the payment on the stock sold
                                double stockPayment = stockSold.getPayment(quantityToSell, priceToSell);

                                // sell shares of a stock (if full sale, stock is removed from arraylist)
                                if(quantityToSell != 1){
                                    gui.handleSellAction(quantityToSell + " shares of " + tempName + 
                                        " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(stockPayment));
                                } else {
                                    gui.handleSellAction(quantityToSell + " share of " + tempName + 
                                        " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(stockPayment));
                                }         
                            }
                        }    
                        else if (listOfInvestments.get(i) instanceof MutualFund && listOfInvestments.get(i).getSymbol().equals(symbolToSell)) {
                            // a sale is found
                            sellFound = true;

                            // new mutual fund object
                            MutualFund fundSold = (MutualFund) listOfInvestments.get(i);

                            // modify the mutual fund found in the array list
                            gui.handleSellAction(symbolToSell + " was found!");

                            if(quantityToSell > fundSold.getQuantity()){
                                gui.handleSellAction("Error: Not enough units owned to sell.");
                            } else {
                                // for full purchase, index is removed so we must store the name of the stock in a temp var
                                String tempName = fundSold.getName();

                                // perform mutual fund sale
                                saleBookValue = fundSold.sell(quantityToSell, priceToSell, listOfInvestments, i);

                                // get the payment on the stock sold
                                double fundPayment = fundSold.getPayment(quantityToSell, priceToSell);

                                // sell units of a mutual fund (if full sale, stock is removed from arraylist)
                                if (quantityToSell != 1) {
                                    gui.handleSellAction(quantityToSell + " units of " + tempName 
                                        + " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(fundPayment));
                                } else {
                                    gui.handleSellAction(quantityToSell + " unit of " + tempName
                                        + " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(fundPayment));
                                }
                            }
                        }        
                    }    
                } 
                if(!sellFound){
                    gui.handleSellAction("Error: Investment (" + symbolToSell + ") was not found.");
                }
            } else {
                gui.handleSellAction("Error: You have no investments to sell.");
            }
            // update the hashtable
            updateHash(listOfInvestments, index, keywordIndices);
        } catch (IllegalArgumentException e) {
            gui.handleSellAction("Error: " + e.getMessage() + "\n");
        }
    }
      
    // UPDATE ALL PRICES OF STOCKS AND MUTUAL FUNDS
    /**
     * This methods allows the user to update all prices
     * of stocks and mutual funds
     * 
     * @param updatedPrice double from updatePanel textfield 
     * @param increasedPos if we are moving forward or backwards in investment list
     * @param saved if we are saving the update price or not
     */
    public void updateInvesments(double updatedPrice, boolean increasedPos, boolean saved){
        try{
            // UPDATE PRICES FOR ALL INVESTMENTS
            if(!saved){ // don't change the position if saving
                if(increasedPos){
                    investmentPos++;
                    if(investmentPos >= listOfInvestments.size()){
                        investmentPos = 0;
                    }
                } else if (!increasedPos){
                    investmentPos--;
                    if(investmentPos < 0){
                        investmentPos = listOfInvestments.size() - 1;
                    }
                }
                }

                if(!listOfInvestments.isEmpty()){
                // if the current investment is a stock object
                if(listOfInvestments.get(investmentPos) instanceof Stock){
                    // create new stock object by downcasting current investment
                    Stock updatedStock = (Stock)listOfInvestments.get(investmentPos);

                    // print current price
                    gui.handleUpdateFields(updatedStock.getSymbol(), updatedStock.getName(), df.format(updatedStock.getPrice()));

                    if(saved){
                        updatedStock.setPrice(updatedPrice);
                        gui.handleUpdateFields(updatedStock.getSymbol(), updatedStock.getName(), df.format(updatedStock.getPrice()));
                        gui.handleUpdateMessage("The updated price for " + updatedStock.getSymbol() + " is: $" + df.format(updatedStock.getPrice()));
                    }
                } 
                // if the current investment is a mutual fund object
                else if(listOfInvestments.get(investmentPos) instanceof MutualFund){
                    // create new mutual fund object by downcasting current investment
                    MutualFund updatedFund = (MutualFund)listOfInvestments.get(investmentPos);

                    // print current price
                    gui.handleUpdateFields(updatedFund.getSymbol(), updatedFund.getName(), df.format(updatedFund.getPrice()));

                    if(saved){
                        updatedFund.setPrice(updatedPrice);
                        gui.handleUpdateFields(updatedFund.getSymbol(), updatedFund.getName(), df.format(updatedFund.getPrice()));
                        gui.handleUpdateMessage("The updated price for " + updatedFund.getSymbol() + " is: $" + df.format(updatedFund.getPrice()));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            gui.handleUpdateMessage("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * This methods prints the first investment in the list to the update panel,
     * if no investments owned it will print an error message in the messageArea
     * 
     */
    public void initialInvestmentShown(){
        if(!listOfInvestments.isEmpty()){
            gui.handleUpdateMessage("");
            gui.handleUpdateFields(listOfInvestments.get(0).getSymbol(), listOfInvestments.get(0).getName(), df.format(listOfInvestments.get(0).getPrice()));
        } else {
            gui.handleUpdateMessage("There are no investments to update. Please buy investments to update prices.");
        }
    }

    // GET GAIN ON INVESTMENTS FROM STOCK AND MUTUAL FUNDS
    /**
     * This methods allows user to check there current
     * gain of all their stocks and mutual funds
     */
    public void getInvestmentGain(){
        // clear the messageArea
        gui.clearGainMessage("");
        gainSum = 0;
        // PRINT GET GAIN TO USER
        for (int i = 0; i < listOfInvestments.size(); i++) {
            if(listOfInvestments.get(i) instanceof Stock){
                // create new stock object by downcasting current investment
                Stock aStock = (Stock)listOfInvestments.get(i);
                // add to the gain sum
                double stockGain = aStock.getGain(aStock.getQuantity(), aStock.getPrice());
                gui.handleGainMessage("The gain on " + aStock.getName() + " (" + aStock.getSymbol() +") is: $" + df.format(stockGain));
                gainSum += stockGain;
            } else if (listOfInvestments.get(i) instanceof MutualFund){
                // create new mutual fund object by downcasting current investment
                MutualFund aFund = (MutualFund)listOfInvestments.get(i);
                // add to the gain sum
                double fundGain = aFund.getGain(aFund.getQuantity(), aFund.getPrice());
                gui.handleGainMessage("The gain on " + aFund.getName() + " (" + aFund.getSymbol() +") is: $" + df.format(fundGain));
                gainSum += fundGain;
            }        
        }
        df.setRoundingMode(RoundingMode.UP);
        // set gain field with 2 decimal places
        gui.handleGainFields("","","$" + df.format(gainSum));
    }

    // SEARCH INVESTMENTS
    /**
     * This method allows users to search for
     * stocks and mutual funds with 3 filters:
     * symbol, keywords and range
     * 
     * @param symbolSearch symbol to search for in list of investments
     * @param keywordSearch keywords to search for in list of investments
     * @param lowerBound search for investments greater than this price
     * @param upperBound search for investments lower than this price
     */
    public void searchInvestments(String symbolSearch, String keywordSearch, String lowerBound, String upperBound){
        try{
            // clear the message area
            gui.setSearchMessage("");

            boolean foundInvestment = true;
            boolean isValidRange = true;

            ArrayList<Integer> intersectedIndices = new ArrayList<>();

            // found counter
            int foundCount = 0;

            // tokenize keywords into an array
            String[] keywords = keywordSearch.split(" ");
            
            // search through keywords in hashmap
            
            if (!keywordSearch.isEmpty()) {
                // ITERSECT ALL SIMILAR INDICIES FOR EACH KEYWORD
                for (int j = 0; j < keywords.length; j++) {
                    String keyword = keywords[j];

                    // put the first keyowrd in the intersectedIndicies array list
                    if (index.containsKey(keyword)) {
                        ArrayList<Integer> keywordIndices = index.get(keyword);
                        // add all indices to intersectedIndices for the first keyword
                        if (j == 0){
                            intersectedIndices.addAll(keywordIndices);
                        } else {
                            // retain all indices after the first
                            intersectedIndices.retainAll(keywordIndices);
                        }
                    } else {
                        intersectedIndices.clear();
                        break;
                    }
                }    
            }
            // if no keywords are provided, include all indices in the intersectedIndices array list
            else {
                for (int i = 0; i < listOfInvestments.size(); i++) {
                    intersectedIndices.add(i);
                }     
            }

            //System.out.println(intersectedIndices);        
                    
            // SYMBOL AND RANGE CHECK
            // iterate through intersectedIndices array list to check for symbol and range
            for (Integer j : intersectedIndices) {
                // set true for every investment in the list, then check set false each loop if criteria not met
                foundInvestment = true;
                
                // check if symbol is not empty
                if(!symbolSearch.isEmpty()){
                    // check if symbol is not equal to current stock
                    if(!listOfInvestments.get(j).getSymbol().equalsIgnoreCase(symbolSearch)){
                        // set to false and don't print stock
                        //System.out.println(listOfInvestments.get(j).getSymbol());
                        foundInvestment = false;
                    }
                }

                try {
                    // check if lower bound is not empty
                    if(!lowerBound.isEmpty()){
                        double lowerValue = Double.parseDouble(lowerBound);
                        // see if stock price falls below lower bound
                        // if so dont print the stock
                        if (listOfInvestments.get(j).getPrice() < lowerValue){
                            foundInvestment = false;
                        }
                    }
                } catch (NumberFormatException notNum) {
                    throw new IllegalArgumentException("Low price must be a valid integer.");
                }
                
                try {
                    // check if upper bound is not empty
                    if (!upperBound.isEmpty()) {
                        double upperValue = Double.parseDouble(upperBound);
                        // see if stock price is above upper bound
                        // if so dont print stock
                        if (listOfInvestments.get(j).getPrice() > upperValue) {
                            foundInvestment = false;
                        }
                    }
                } catch (NumberFormatException notNum) {
                    throw new IllegalArgumentException("High price must be a valid integer.");
                }
                

                if(!upperBound.isEmpty() && !lowerBound.isEmpty()){
                    double lowerValue = Double.parseDouble(lowerBound);
                    double upperValue = Double.parseDouble(upperBound);
                    if (lowerValue > upperValue){
                        throw new IllegalArgumentException("Low price cannot be larger than high price.");
                    }
                }

                // if condition maintains true (survived all filters), then print
                if(foundInvestment && isValidRange){
                    gui.handleSearchMessage(listOfInvestments.get(j).toString());
                    foundCount ++;
                }
            }
            // no investments found
            if (foundCount == 0 && isValidRange){
                gui.handleSearchMessage("No investments found.");
            }
        } catch (IllegalArgumentException e) {
            gui.setSearchMessage("Error: " + e.getMessage() + "\n");
        }
       
    }     

    /**
     * This methods loads all investments into the investment array list from a file
     * 
     * @param filename - the file name to load from
     * 
     * @return true if no errors opening file, false if errors --> set file to default.txt
     */
    public boolean loadInvestments(String filename){
        // file scanner
        Scanner inputStream = null;
        try {
            // set file scanner to the correct file stream
            inputStream = new Scanner(new FileInputStream(filename));
            Investment readInvestment = null;
            // loop until file ends
            while(inputStream.hasNextLine()){
                // scan a line
                String line = inputStream.nextLine().trim();
                // stock or mutual fund
                if (line.startsWith("type")) {
                    String type = line.split("=")[1].trim().replace("\"", "");

                    if (type.equalsIgnoreCase("stock")) {
                        readInvestment = new Stock();
                    } else if (type.equalsIgnoreCase("mutualfund")) {
                        readInvestment = new MutualFund();
                    } else {
                        System.out.println("Error: Unrecognized investment type '" + type + "'");
                    }
                // read the rest of the investment parameteres
                } else if (line.startsWith("symbol")) {
                    readInvestment.setSymbol(line.split("=")[1].trim().replace("\"", ""));
                } else if (line.startsWith("name")) {
                    readInvestment.setName(line.split("=")[1].trim().replace("\"", ""));
                } else if (line.startsWith("quantity")) {
                    readInvestment.setQuantity(Integer.parseInt(line.split("=")[1].trim().replace("\"", "")));
                } else if (line.startsWith("price")) {
                    readInvestment.setPrice(Double.parseDouble(line.split("=")[1].trim().replace("\"", "")));
                } else if (line.startsWith("bookValue")) {
                    readInvestment.setBookValue(Double.parseDouble(line.split("=")[1].trim().replace("\"", "")));
                    listOfInvestments.add(readInvestment); // Add investment when all details are set
                }
            }
            System.out.println("Investments loaded from file.");
            // update hash map after reading from file
            updateHash(listOfInvestments, index, keywordIndices);
            return true;
        } catch (Exception e) {
            // error reading file exception
            System.out.println("Error reading file.");
            return false;
        }
    }

    /**
     * This methods writes all investments from the investment array list into a file
     * 
     * @param filename - the file name to load from
     */
    public void writeInvestments(String filename) {
        // create print writer to write to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // iterate through all investments and write to destination file
            for (Investment investment : listOfInvestments) {
                writer.println("type = \"" + (investment instanceof Stock ? "stock" : "mutualfund") + "\"");
                writer.println("symbol = \"" + investment.getSymbol() + "\"");
                writer.println("name = \"" + investment.getName() + "\"");
                writer.println("quantity = \"" + investment.getQuantity() + "\"");
                writer.println("price = \"" + investment.getPrice() + "\"");
                writer.println("bookValue = \"" + investment.getBookValue() + "\"");
                writer.println(); // Blank line between investments
            }
            // success message
            System.out.println("Investments saved to file.");

        // error writing to file catch
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    /**
     * This methods writes all investments from the investment array list into a file
     * 
     * @param inputList the array list we are iterating through
     * @param index the hashmap we are updating
     * @param keywordIndices the second array list we are storing intersected keywords in
     */
    private void updateHash(ArrayList<Investment> inputList, HashMap<String, ArrayList<Integer>> index, ArrayList<Integer> keywordIndices){
        // clear the hash table before updating the hash table (specifically in the case of sell to ensure fully sold investments are removed from hashmap)
        index.clear();
        
        for (int i = 0; i < inputList.size(); i++) {
            // store the name of the investment in a string
            String investmentName = inputList.get(i).getName().toLowerCase();

            // tokenize keywords into an array
            String[] keywords = investmentName.split(" ");

             // Check each keyword
            for (String keyword : keywords) {
                // If the keyword is contained in the current investment name
                if (investmentName.contains(keyword)) {
                    // Check if the keyword is already in the map
                    keywordIndices = index.get(keyword);
                    if (keywordIndices == null) {
                        // If not present, create a new ArrayList and add it to the map
                        keywordIndices = new ArrayList<>();
                        index.put(keyword, keywordIndices);
                    }
                    // Add the current index if not already in the list
                    if (!keywordIndices.contains(i)) {
                        keywordIndices.add(i);
                    }
                    
                }
            }
        }
    }
}
