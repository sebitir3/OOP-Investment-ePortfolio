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
    private static ArrayList<Investment> listOfInvestments = new ArrayList<>();
    private static double gainSum = 0;

    // keyword hashmap
    private static HashMap<String, ArrayList<Integer>> index = new HashMap<String,ArrayList<Integer>>();
    private static ArrayList<Integer> keywordIndices = new ArrayList<>();

    // decimal format for getGain
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private MessageListener messageListener;

    /**
     * Main methods for implementing all operations
     * 
     * @param args - command line arguments array
     */
    public static void main(String[] args) {
        // create scanner object      
        Scanner scanner = new Scanner(System.in);
        // initialize switch statement var
        String input;
        // create new portfolio object
        Portfolio portfolio = new Portfolio();

        // FILE PATH FOR READING AND WRITING

        // set the current working directory
        String currentDir = new File(".").getAbsolutePath();
        // construct the absolute path for the file
        String filePath;

        // for incorrectly specified .txt filename
        // write to default
        boolean isDefaultCase;

        // no file specified in command line
        if (args.length < 1) {
            filePath = currentDir + "/ePortfolio/default.txt";
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    // Create the file if it doesn't exist
                    file.getParentFile().mkdirs();  // Ensure the directory exists
                    file.createNewFile();
                    //System.out.println("New file created at: " + file.getAbsolutePath());
                } catch (IOException e) {
                    System.out.println("Error creating file.");
                    System.exit(1);
                }
            }
        } else {
            filePath = currentDir + "/ePortfolio/" + args[0] + ".txt";
        }

        // load in investments from file
        // loadInvestments returns false when error reading file occurs
        isDefaultCase = portfolio.loadInvestments(filePath);
        if(!isDefaultCase){
            filePath = currentDir + "/ePortfolio/default.txt";
        }

        // update hash map after reading from file
        portfolio.updateHash(listOfInvestments, index, keywordIndices);

        // MENU
        do {
            System.out.println("""
                           Options:
                           - Buy    (enter 'b' or 'buy' case insensitive)
                           - Sell   (enter 's' or 'sell' case insensitive)
                           - Update (enter 'u' or 'update' case insensitive)
                           - Gain   (enter 'g' or 'gain' case insensitive)
                           - Search (enter 'se' or 'search' case insensitive)
                           - Quit   (enter 'q' or 'quit' case insensitive)
                           """);

            System.out.println("Enter your choice: ");

            // read input as string
            input = scanner.nextLine().toLowerCase();

            switch (input) {
                case "b":
                case "buy":
                    //portfolio.buyInvestments(scanner);
                    break;

                case "s":
                case "sell":
                    portfolio.sellInvestments(scanner);
                    break;

                case "u":
                case "update":
                    portfolio.updateInvesments(scanner);
                    break;

                case "g":
                case "gain":
                    portfolio.getInvestmentGain();
                    break;

                case "se":
                case "search":
                    portfolio.searchInvestments(scanner);
                    break;

                case "q":
                case "quit":
                    portfolio.writeInvestments(filePath);
                    System.out.println("Exiting program...");
                    portfolio.updateHash(listOfInvestments, index, keywordIndices);
                    System.exit(0);

                default:
                    System.out.println("Please enter valid input.");
            }          
        } while ((!input.toLowerCase().equals("q")) && (!input.toLowerCase().equals("quit")));
    }

    // METHODS FOR MAIN

    // implement messageListener
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    // if there is something in the message listener, send it over
    private void notifyMessage(String message) {
        if (messageListener != null) {
            messageListener.appendMessage(message);
        }
    }

    // BUY A STOCK OR MUTUAL FUND

    /**
     * This methods allows the user to buy a stock or a mutual fund
     * 
     * @param scanner - for user inputs
     */
    public void buyInvestments(String buyInvestType, String symbolToBuy, String nameToBuy, int quantityToBuy, double priceToBuy){
        // if buying a stock or mutual fund
        boolean stockMode = false;
        
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
                if ((buyInvestType.equalsIgnoreCase("stock") || buyInvestType.equalsIgnoreCase("s"))) {
                    stockMode = true;
                    investmentFound = false;

                    // check if the symbol is a mutual fund and print error if true
                    if (symbolToBuy.equals(listOfInvestments.get(i).getSymbol()) && listOfInvestments.get(i) instanceof MutualFund){
                        notifyMessage("Error. This symbol already exists as a mutual fund.");
                        duplicateSymbol = true;
                    } 
                    // find stock in investment list and buy stock
                    else if (listOfInvestments.get(i) instanceof Stock && listOfInvestments.get(i).getSymbol().equals(symbolToBuy)) {
                        Stock stockInList = (Stock)listOfInvestments.get(i);
                        investmentFound = true;
                        // modify the stock found in the array list
                        //System.out.println(symbolToBuy + " was found!");

                        // ask for quantity and price with validation
                        quantityToBuy = quanityValidation(quantityToBuy, true);
                        priceToBuy = priceValidation(priceToBuy, stockMode);
                                
                        // buy shares of a stock
                        purchaseBookValue = stockInList.buy(quantityToBuy, priceToBuy);
                        if (quantityToBuy != 1){
                            notifyMessage(quantityToBuy + " shares of " + listOfInvestments.get(i).getName() 
                                + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                        } else {
                            notifyMessage(quantityToBuy + " share of " + listOfInvestments.get(i).getName() 
                                + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                        }
                        //break;
                    }    
                // buying a mutual fund           
                } else if (buyInvestType.equalsIgnoreCase("mutual fund") || buyInvestType.equalsIgnoreCase("m")) {
                    stockMode = false;
                    investmentFound = false;
            
                    // check if the symbol is a stock and print error if true
                    if (listOfInvestments.get(i) instanceof Stock && symbolToBuy.equals(listOfInvestments.get(i).getSymbol())){
                        System.out.println("Error. This symbol already exists as a stock.");
                        duplicateSymbol = true;
                    } 
                    // check if mutual fund already exists in array list
                    else if (listOfInvestments.get(i) instanceof MutualFund && listOfInvestments.get(i).getSymbol().equals(symbolToBuy)) {
                        // set condition to true so the next iteration doesnt enter loop
                        investmentFound = true;
        
                        // modify the mutual fund found in the array list
                        System.out.println(symbolToBuy + " was found!");

                        MutualFund fundInList = (MutualFund)listOfInvestments.get(i);
            
                        // ask for quantity and price with validation
                        quantityToBuy = quanityValidation(quantityToBuy, true);
                        priceToBuy = priceValidation(priceToBuy, stockMode);
            
                        // buy shares of a stock
                        purchaseBookValue = fundInList.buy(quantityToBuy, priceToBuy);
                        if(quantityToBuy != 1){
                            notifyMessage(quantityToBuy + " units of " + fundInList.getName() 
                                + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                        } else {
                            notifyMessage(quantityToBuy + " unit of " + fundInList.getName() 
                                + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                        }                                   
                    }
                }
            }
        }

        // buy NEW investment
        if(!investmentFound && !duplicateSymbol) {
            // NEW STOCK
            if(buyInvestType.equalsIgnoreCase("stock") || buyInvestType.equalsIgnoreCase("s")){
                System.out.println(symbolToBuy + " was found not found.");
        
                // ask for quantity and price with validation
                quantityToBuy = quanityValidation(quantityToBuy, true);
                priceToBuy = priceValidation(priceToBuy, stockMode);
        
                Stock inputStock = new Stock(symbolToBuy, nameToBuy, 0, 0, 0);
        
                // buy shares of the stock (sets quantity, price, and bookvalue)
                purchaseBookValue = inputStock.buy(quantityToBuy, priceToBuy);
                if(quantityToBuy != 1){
                    notifyMessage(quantityToBuy + " shares of " + nameToBuy + " (" + symbolToBuy 
                        + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                } else {
                    notifyMessage(quantityToBuy + " share of " + nameToBuy + " (" + symbolToBuy 
                        + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                }
                // add stock to array list
                listOfInvestments.add(inputStock);
            } 
            // NEW MUTUAL FUND
            else if (buyInvestType.equalsIgnoreCase("mutual fund") || buyInvestType.equalsIgnoreCase("m")){
                System.out.println(symbolToBuy + " was found not found.");
            
                // ask for quantity and price with validation
                quantityToBuy = quanityValidation(quantityToBuy, true);
                priceToBuy = priceValidation(priceToBuy, stockMode);
            
                MutualFund inputMutualFund = new MutualFund(symbolToBuy, nameToBuy, 0, 0, 0);
            
                // buy shares of the stock (sets quantity, price, and bookvalue)
                purchaseBookValue = inputMutualFund.buy(quantityToBuy, priceToBuy);
                if (quantityToBuy != 1){
                    notifyMessage(quantityToBuy + " units of " + nameToBuy + " (" 
                        + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                } else {
                    notifyMessage(quantityToBuy + " unit of " + nameToBuy + " (" 
                        + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                }
                // add mutual fund to array list
                notifyMessage(inputMutualFund.toString());
                listOfInvestments.add(inputMutualFund);
            }
        }
        // update hashmap after buying
        updateHash(listOfInvestments, index, keywordIndices);
    }
       
    // SELL AN INVESTMENT
    /**
     * This methods allows the user to sell a stock or a mutual fund
     *
     * @param scanner - for user inputs
     */
    private void sellInvestments(Scanner scanner){
        int quantityToSell = 0;
        double priceToSell = 0;
        // if selling a stock or mutual fund
        boolean stockMode = false;

        // condtions that check for no stocks, funds or investments in general
        boolean noStocks = false;
        boolean noMutualFunds = false;
        boolean noInvestments = false;

        boolean sellFound = false;

        // book value @ sale
        double saleBookValue;

        // stock and mutual fund counters
        int stockCounter = 0;
        int fundCounter = 0;

        // user input investment symbol
        String symbolToSell = "";

        // check if investment list is empty and exit if true
        if(listOfInvestments.isEmpty()){
            System.out.println("You have no investments. Please buy investments to be able to sell.");
            noInvestments = true;
        }

        // counters that check if stock or funds are present in investments
        for (int i = 0; i < listOfInvestments.size(); i++) {
            if (listOfInvestments.get(i) instanceof Stock) {
                stockCounter++;
            } else if (listOfInvestments.get(i) instanceof MutualFund) {
                fundCounter++;
            }
        }

        // no stock condtion set
        if (stockCounter == 0) {
            noStocks = true;
        }

        // no mutual fund condition set
        if (fundCounter == 0) {
            noMutualFunds = true;
        }

        // there are investments in the list ask user what they want to sell
        if(!noInvestments){
            System.out.println("Enter the type of investment you want to sell: ");
            String sellInvestType = scanner.nextLine().toLowerCase();
            
            // investment type validation
            while (!sellInvestType.equals("stock") && !sellInvestType.equals("mutual fund")
                    && !sellInvestType.equals("s") && !sellInvestType.equals("m")){
                System.out.println("Enter the type of a valid investment type (stock or mutual fund): ");
                sellInvestType = scanner.nextLine();
            }
           
            // enter the symbol of investment user wishes to sell
            if(sellInvestType.equalsIgnoreCase("stock") || sellInvestType.equalsIgnoreCase("s")){
                if(noStocks){
                    System.out.println("No stocks are owned. Please buy stocks to sell stocks.");
                } else {
                    System.out.println("Enter the symbol of the stock you wish to sell: "); 
                    // enter the symbol of stock user wishes to sell
                    symbolToSell = scanner.nextLine();
                }   
            } else if (sellInvestType.equalsIgnoreCase("mutual fund") || sellInvestType.equalsIgnoreCase("m")){
                if(noMutualFunds){
                    System.out.println("No mutual funds are owned. Please buy mutual funds to sell mutual funds.");
                } else {
                    System.out.println("Enter the symbol of the mutual fund you wish to sell: ");
                    // enter the symbol of mutual fund user wishes to sell
                    symbolToSell = scanner.nextLine();
                }
            }

            // iterate through all investments
            for (int i = 0; i < listOfInvestments.size(); i++){
                if(!noInvestments && !sellFound){
                    // SELL A STOCK
                    if (sellInvestType.equals("stock") || sellInvestType.equals("s")) {
                        stockMode = true;
                        
                        // if there are stocks
                        if(!noStocks) {       
                            // is the investmen is a stock and symbol matches       
                            if (listOfInvestments.get(i) instanceof Stock && listOfInvestments.get(i).getSymbol().equals(symbolToSell)) {
                                // a sale is found
                                sellFound = true;

                                // create stock object
                                Stock stockSold = (Stock) listOfInvestments.get(i);

                                // modify the stock found in the array list
                                System.out.println(symbolToSell + " was found!");

                                // ask for quantity and price with validation
                                //quantityToSell = quanityValidation(scanner, quantityToSell, false);
                            
                                // check if quantity to sell is larger than owned stock
                                while(quantityToSell > stockSold.getQuantity()){
                                    System.out.println("ERROR: Not enough shares owned to sell.");
                                    //quantityToSell = quanityValidation(scanner, quantityToSell, false);       
                                }

                                // validate input price to sell
                                //priceToSell = priceValidation(scanner, priceToSell, stockMode);

                                // for full purchase, index is removed so we must store the name of the stock in a temp var
                                String tempName = stockSold.getName();
                                
                                // perform the sale    
                                saleBookValue = stockSold.sell(quantityToSell, priceToSell, listOfInvestments, i);

                                // get the payment on the stock sold
                                double stockPayment = stockSold.getPayment(quantityToSell, priceToSell);

                                // sell shares of a stock (if full sale, stock is removed from arraylist)
                                if(quantityToSell != 1){
                                    System.out.println(quantityToSell + " shares of " + tempName + 
                                        " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(stockPayment));
                                } else {
                                    System.out.println(quantityToSell + " share of " + tempName + 
                                        " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(stockPayment));
                                } 
                                    
                                // consume new line character
                                scanner.nextLine();            
                            }
                        }
                    } 
                    // SELL A MUTUAL FUND
                    else if (sellInvestType.equalsIgnoreCase("mutual fund") || sellInvestType.equalsIgnoreCase("m")) {
                        stockMode = false;

                        if(!noMutualFunds){                
                            if (listOfInvestments.get(i) instanceof MutualFund && listOfInvestments.get(i).getSymbol().equals(symbolToSell)) {
                                // a sale is found
                                sellFound = true;

                                // new mutual fund object
                                MutualFund fundSold = (MutualFund) listOfInvestments.get(i);

                                // modify the mutual fund found in the array list
                                System.out.println(symbolToSell + " was found!");

                                // ask for quantity and price with validation
                                //quantityToSell = quanityValidation(scanner, quantityToSell, false);

                                while(quantityToSell > fundSold.getQuantity()){
                                    System.out.println("ERROR: Not enough units owned to sell.");
                                    // = quanityValidation(scanner, quantityToSell, false);
                                }

                                // validate input price to sell
                                //priceToSell = priceValidation(scanner, priceToSell, stockMode);

                                // for full purchase, index is removed so we must store the name of the stock in a temp var
                                String tempName = fundSold.getName();

                                // perform mutual fund sale
                                saleBookValue = fundSold.sell(quantityToSell, priceToSell, listOfInvestments, i);

                                // get the payment on the stock sold
                                double fundPayment = fundSold.getPayment(quantityToSell, priceToSell);

                                // sell units of a mutual fund (if full sale, stock is removed from arraylist)
                                if (quantityToSell != 1) {
                                    System.out.println(quantityToSell + " units of " + tempName 
                                        + " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(fundPayment));
                                } else {
                                    System.out.println(quantityToSell + " unit of " + tempName
                                        + " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue) + " | Payment on Sale: $" + df.format(fundPayment));
                                }
                                // consume new line character
                                scanner.nextLine();
                            } 
                        }     
                    }
                } 
                // no investments owned
                else if (!sellFound && noInvestments) {
                    System.out.println("Error: no investments owned. Please buy investments to sell.");
                } 
            } 
            // couldnt find investments
            if(!sellFound && stockMode && !noStocks){
                System.out.println("Error: the stock with symbol " + symbolToSell + " was not found.");
                System.out.println("No shares were sold.");            
            } else if (!sellFound && !stockMode && !noMutualFunds){
                System.out.println("Error: the mutual fund with symbol " + symbolToSell + " was not found.");
                System.out.println("No units were sold.");            
            }
        }
        // update the hashtable
        updateHash(listOfInvestments, index, keywordIndices);
    }
      
    // UPDATE ALL PRICES OF STOCKS AND MUTUAL FUNDS
    /**
     * This methods allows the user to update all prices
     * of stocks and mutual funds
     * 
     * @param scanner - for user inputs
     */
    private void updateInvesments(Scanner scanner){
        // UPDATE PRICES FOR ALL INVESTMENTS
        // update stocks first
        boolean updatingAStock = true;
        // iterate through stocks and update price for each stock
        if (listOfInvestments.isEmpty()) {
            System.out.println("There are no investments to update. Please buy investments to update prices.");
        } else {
            for (int i = 0; i < listOfInvestments.size(); i++) {
                // if the current investment is a stock object
                if(listOfInvestments.get(i) instanceof Stock){
                    // for price validation
                    updatingAStock = true;

                    // create new stock object by downcasting current investment
                    Stock updatedStock = (Stock)listOfInvestments.get(i);

                    // print current price
                    System.out.println("The current price for " + updatedStock.getSymbol() + " is: $" + df.format(updatedStock.getPrice()));

                    // ask user for the updated price and validate
                    double updatedStockPrice = 0;
                    updatedStockPrice = priceValidationForUpdate(scanner, updatedStockPrice, updatingAStock);

                    updatedStock.setPrice(updatedStockPrice);
                    System.out.println("The updated price for " + updatedStock.getSymbol() + " is: $" + df.format(updatedStock.getPrice()));
                    System.out.println(""); // spacer

                } 
                // if the current investment is a mutual fund object
                else if(listOfInvestments.get(i) instanceof MutualFund){
                    // for price validation
                    updatingAStock = false;

                    // create new mutual fund object by downcasting current investment
                    MutualFund updatedFund = (MutualFund)listOfInvestments.get(i);

                    // print current price
                    System.out.println("The current price for " + updatedFund.getSymbol() + " is: $" + df.format(updatedFund.getPrice()));

                    // ask user for updated price and validate
                    double updatedFundPrice = 0;
                    updatedFundPrice = priceValidationForUpdate(scanner, updatedFundPrice, updatingAStock);

                    updatedFund.setPrice(updatedFundPrice);
                    System.out.println("The updated price for " + updatedFund.getSymbol() + " is: $" + df.format(updatedFund.getPrice()));
                    System.out.println(""); // spacer
                }
                
            }
            scanner.nextLine(); // consume new line character
        }
    }

    // GET GAIN ON INVESTMENTS FROM STOCK AND MUTUAL FUNDS
    /**
     * This methods allows user to check there current
     * gain of all their stocks and mutual funds
     */
    private void getInvestmentGain(){
        gainSum = 0;
        // PRINT GET GAIN TO USER
        for (int i = 0; i < listOfInvestments.size(); i++) {
            if(listOfInvestments.get(i) instanceof Stock){
                // create new stock object by downcasting current investment
                Stock aStock = (Stock)listOfInvestments.get(i);
                // add to the gain sum
                double stockGain = aStock.getGain(aStock.getQuantity(), aStock.getPrice());
                gainSum += stockGain;
            } else if (listOfInvestments.get(i) instanceof MutualFund){
                // create new mutual fund object by downcasting current investment
                MutualFund aFund = (MutualFund)listOfInvestments.get(i);
                // add to the gain sum
                double fundGain = aFund.getGain(aFund.getQuantity(), aFund.getPrice());
                gainSum += fundGain;
            }        
        }
        df.setRoundingMode(RoundingMode.UP);
        // print gain with 2 decimal places
        System.out.println("So far, your potential gain from investestments is: $" + df.format(gainSum));
    }

    // SEARCH INVESTMENTS
    /**
     * This method allows users to search for
     * stocks and mutual funds with 3 filters:
     * symbol, keywords and range
     * 
     * @param scanner - for user input
     */
    private void searchInvestments(Scanner scanner){
        // ask user for symbol, keywords and range
        System.out.println("Enter the symbol to search for (optional: leave blank to not search for any symbol)");
        String symbolSearch = scanner.nextLine().trim();
        System.out.println("Enter keywords seperated by spaces to search for (optional: leave blank to not search for any keywords)");
        String keywordSearch = scanner.nextLine().trim().toLowerCase();
        System.out.println("Enter a price range seperated to search for (optional: leave blank to not search for any range)");
        String rangeSearch = scanner.nextLine().trim();

        boolean foundInvestment = true;
        boolean isValidRange = true;

        //HashMap<String, ArrayList<Integer>> index = new HashMap<String,ArrayList<Integer>>();
        //ArrayList<Integer> keywordIndices = new ArrayList<>();
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
                //System.out.println(keyword);

                // put the first keyowrd in the intersectedIndicies array list
                if (index.containsKey(keyword)) {
                    ArrayList<Integer> keywordIndices = index.get(keyword);
                    //System.out.println(keywordIndices);

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

            // check if price range not empty
            if(!rangeSearch.isEmpty() && isValidRange){
                // if the range has - in it, it means its not an exact value
                if(rangeSearch.contains("-")){
                    // set upper and lower bounds
                    String[] bounds = rangeSearch.split("-");
                    String lowerBound = bounds[0].trim();
                    String upperBound;
                    if (bounds.length > 1){
                        upperBound = bounds[1].trim();
                    } else {
                        upperBound = "";
                    }

                    // check if lower bound is not empty
                    if(!lowerBound.isEmpty()){
                        double lowerValue = Double.parseDouble(lowerBound);
                        // see if stock price falls below lower bound
                        // if so dont print the stock
                        if (listOfInvestments.get(j).getPrice() < lowerValue){
                            foundInvestment = false;
                        }
                    }

                    // check if upper bound is not empty
                    if (!upperBound.isEmpty()) {
                        double upperValue = Double.parseDouble(upperBound);
                        // see if stock price is above upper bound
                        // if so dont print stock
                        if (listOfInvestments.get(j).getPrice() > upperValue) {
                            foundInvestment = false;
                        }
                    }
                // check for invalid range
                } else if (rangeSearch.contains(" ")){
                    System.out.println("Error: invalid range! Please enter a valid range that contains '-'.");
                    foundInvestment = false;
                    isValidRange = false;
                    
                } else {
                    double exactPrice = Double.parseDouble(rangeSearch);
                    // see if stock doesn't match exact price
                    // if so, dont print stock
                    if(listOfInvestments.get(j).getPrice() != exactPrice) {
                        foundInvestment = false;
                    }
                }
            }

            // if condition maintains true (survived all filters), then print
            if(foundInvestment && isValidRange){
                System.out.println(listOfInvestments.get(j).toString());
                foundCount ++;
            }
        }
        
        // no investments found
        if (foundCount == 0 && isValidRange){
            System.out.println("No investments found.");
        }

    }     

    // INPUT VALIDATION HELPERS FOR BUY AND SELL
    /**
     * This methods validates correct input for quantity 
     * (must be an integer value greater than 0). The user 
     * is repeatedly asked to input a correct quantity value
     * and when recieved, the value is returned
     * 
     * @param scanner - for user inputs
     * @param validQuantity - the quantity value to validate
     * @param ifBuy - if buying or selling
     */
    private int quanityValidation(int validQuantity, boolean ifBuy){
        boolean validInput = false;

        // loop until input is valid
        while (!validInput){
            try {
                // only exit loop if value is positive integer
                if (validQuantity <= 0){
                    System.out.println("Enter a positive quantity.");
                } else {
                    validInput = true;
                }
            // throw input mismatch exception and ask again until valid
            } catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a whole number.");
            }   
        }
        return validQuantity;
    }

    /**
     * This methods validates correct input for price 
     * (must be an double greater than 0). The user is repeatedly
     * asked to input a correct price value and when recieved, 
     * the value is returned
     *
     * @param scanner - for user inputs
     * @param validPrice - the price value to validate
     * @param ifStock - if stock or mutual fund
     */
    private double priceValidation(double validPrice, boolean ifStock){
        boolean validInput = false;

        // loop until valid input   
        while (!validInput){
            try {
                // only exit the loop if price is a positive number
                if (validPrice <= 0){
                    notifyMessage("Enter a positive price.");
                } else {
                    validInput = true;
                }
            // throw input mismatch exception and ask again until valid
            } catch (InputMismatchException e){
                notifyMessage("Invalid input. Please enter a number.");
            }
        }
        return validPrice;
    }

    /**
     * This methods validates correct input for price FOR THE UPDATE FUNCTION
     * (must be an double greater than 0). The user is repeatedly
     * asked to input a correct price value and when recieved, 
     * the value is returned
     *
     * @param scanner - for user inputs
     * @param validPrice - the price value to validate
     * @param ifStock - if stock or mutual fund
     */
    private double priceValidationForUpdate(Scanner scanner, double validPrice, boolean ifStock){
        boolean validInput = false;

        // loop until valid input   
        while (!validInput){
            try {
                // print dependant on stock or mutual fund (i.e. shares or units)
                if(ifStock){
                    System.out.println("Enter the new price of each share: ");
                } else {
                    System.out.println("Enter the new price of each unit: ");
                }
                // get price from user
                validPrice = scanner.nextDouble();

                // only exit the loop if price is a positive number
                if (validPrice <= 0){
                    System.out.println("Enter a positive price.");
                } else {
                    validInput = true;
                }
            // throw input mismatch exception and ask again until valid
            } catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return validPrice;
    }

    /**
     * This methods loads all investments into the investment array list from a file
     * 
     * @param filename - the file name to load from
     * 
     * @return true if no errors opening file, false if errors --> set file to default.txt
     */
    private boolean loadInvestments(String filename){
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
    private void writeInvestments(String filename) {
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

    // update hash table for search
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
