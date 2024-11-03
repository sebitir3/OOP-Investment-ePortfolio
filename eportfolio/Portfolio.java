package eportfolio;

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
    private static ArrayList<Investment> listOfInvestments = new ArrayList<>();
    //private static ArrayList<MutualFund> listOfMutualFunds = new ArrayList<>();
    //private static ArrayList<MutualFund> listOfStocks = new ArrayList<>();
    private static double gainSum = 0;
    // decimal format for getGain
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Main methods for implementing all operations
     * 
     * @param args - command line arguments array
     */
    public static void main(String[] args) {
        // create scanner object      
        Scanner scanner = new Scanner(System.in);
        // initialize switch statement var
        String input = null;
        // create new portfolio object
        Portfolio portfolio = new Portfolio();


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
                    portfolio.buyInvestments(scanner);
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
                    System.out.println("Exiting program...");
                    System.exit(0);

                default:
                    System.out.println("Please enter valid input.");
            }          
        } while ((!input.toLowerCase().equals("q")) && (!input.toLowerCase().equals("quit")));
    }

    // METHODS FOR MAIN

    // BUY A STOCK OR MUTUAL FUND

    /**
     * This methods allows the user to buy a stock or a mutual fund
     * 
     * @param scanner - for user inputs
     */
    private void buyInvestments(Scanner scanner){
        int quantityToBuy = 0;
        double priceToBuy = 0;
        // if buying a stock or mutual fund
        boolean stockMode = false;

        // book value when purchased
        double purchaseBookValue = 0;

        System.out.println("Enter the type of investment you want to buy: ");
        String buyInvestType = scanner.nextLine().toLowerCase();
    
        // validate input until user correctly enters stock or mutual fund
        while (!buyInvestType.equals("stock") && !buyInvestType.equals("mutual fund") && !buyInvestType.equals("s") && !buyInvestType.equals("m")){
            System.out.println("Enter a valid investment type (stock or mutual fund): ");
            buyInvestType = scanner.nextLine();
        }

        boolean investmentFound = false;
            
        if (buyInvestType.equalsIgnoreCase("stock") || buyInvestType.equalsIgnoreCase("s")) {
            stockMode = true;
            investmentFound = false;
            
            // enter the symbol of stock user wishes to purchase
            System.out.println("Enter the symbol of the stock you wish to purchase: ");
            String symbolToBuy = scanner.nextLine();

            // iterate through all investments
            for (int i = 0; i < listOfInvestments.size(); i++) {
                if (listOfInvestments.get(i) instanceof Stock && listOfInvestments.get(i).getSymbol().equals(symbolToBuy)) {
                    Stock stockInList = (Stock)listOfInvestments.get(i);
                    investmentFound = true;
                    // modify the stock found in the array list
                    System.out.println(symbolToBuy + " was found!");

                    // ask for quantity and price with validation
                    quantityToBuy = quanityValidation(scanner, quantityToBuy, true);
                    priceToBuy = priceValidation(scanner, priceToBuy, stockMode);
                            
                    // buy shares of a stock
                    purchaseBookValue = stockInList.buy(quantityToBuy, priceToBuy);
                    if (quantityToBuy != 1){
                        System.out.println(quantityToBuy + " shares of " + listOfInvestments.get(i).getName() 
                            + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    } else {
                        System.out.println(quantityToBuy + " share of " + listOfInvestments.get(i).getName() 
                            + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    }
                    break;
                }

            } 
                    
            if(!investmentFound) {
                System.out.println(symbolToBuy + " was found not found.");
        
                System.out.println("Enter the name of the stock you would like to purchase: ");
                String nameToBuy = scanner.nextLine();
        
                // ask for quantity and price with validation
                quantityToBuy = quanityValidation(scanner, quantityToBuy, true);
                priceToBuy = priceValidation(scanner, priceToBuy, stockMode);
        
                Stock inputStock = new Stock(symbolToBuy, nameToBuy, 0, 0, 0);
        
                // buy shares of the stock (sets quantity, price, and bookvalue)
                purchaseBookValue = inputStock.buy(quantityToBuy, priceToBuy);
                if(quantityToBuy != 1){
                    System.out.println(quantityToBuy + " shares of " + nameToBuy + " (" + symbolToBuy 
                        + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                } else {
                    System.out.println(quantityToBuy + " share of " + nameToBuy + " (" + symbolToBuy 
                        + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                }
                // add stock to array list
                listOfInvestments.add(inputStock);
            }

        } else if (buyInvestType.equalsIgnoreCase("mutual fund") || buyInvestType.equalsIgnoreCase("m")) {
            stockMode = false;
            investmentFound = false;
            // enter the symbol of mutual fund user wishes to purchase
            System.out.println("Enter the symbol of the mutual fund you wish to purchase: ");
            String symbolToBuy = scanner.nextLine();
        
            for (int i = 0; i < listOfInvestments.size(); i++) {
                // check if stock already exists in array list
                if (listOfInvestments.get(i) instanceof MutualFund && listOfInvestments.get(i).getSymbol().equals(symbolToBuy)) {
                    // set condition to true so the next iteration doesnt enter loop
                    investmentFound = true;
    
                    // modify the mutual fund found in the array list
                    System.out.println(symbolToBuy + " was found!");

                    MutualFund fundInList = (MutualFund)listOfInvestments.get(i);
        
                    // ask for quantity and price with validation
                    quantityToBuy = quanityValidation(scanner, quantityToBuy, true);
                    priceToBuy = priceValidation(scanner, priceToBuy, stockMode);
        
                    // buy shares of a stock
                    purchaseBookValue = fundInList.buy(quantityToBuy, priceToBuy);
                    if(quantityToBuy != 1){
                        System.out.println(quantityToBuy + " units of " + fundInList.getName() 
                            + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    } else {
                        System.out.println(quantityToBuy + " unit of " + fundInList.getName() 
                            + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    }     
                                    
                    break;
                }
            }

            if(!investmentFound){
                System.out.println(symbolToBuy + " was found not found.");
            
                System.out.println("Enter the name of the mutual fund you would like to purchase: ");
                String nameToBuy = scanner.nextLine();
            
                // ask for quantity and price with validation
                quantityToBuy = quanityValidation(scanner, quantityToBuy, true);
                priceToBuy = priceValidation(scanner, priceToBuy, stockMode);
            
                MutualFund inputMutualFund = new MutualFund(symbolToBuy, nameToBuy, 0, 0, 0);
            
                // buy shares of the stock (sets quantity, price, and bookvalue)
                purchaseBookValue = inputMutualFund.buy(quantityToBuy, priceToBuy);
                if (quantityToBuy != 1){
                    System.out.println(quantityToBuy + " units of " + nameToBuy + " (" 
                        + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                } else {
                    System.out.println(quantityToBuy + " unit of " + nameToBuy + " (" 
                        + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                }
                // add mutual fund to array list
                System.out.println(inputMutualFund);
                listOfInvestments.add(inputMutualFund);
            }
        }
            
        scanner.nextLine();  // clear new line character for next function call in switch case

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
        boolean noStocks = false;
        boolean noMutualFunds = false;
        boolean noInvestments = false;

        // book value @ sale
        double saleBookValue;

        // check if investment list is empty and exit if true
        if(listOfInvestments.isEmpty()){
            System.out.println("You have no investments. Please buy investments to be able to sell.");
            noInvestments = true;
            
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

            // SELL A STOCK
            if (sellInvestType.equals("stock") || sellInvestType.equals("s")) {
                stockMode = true;
                noStocks = false;

                int counter = 0;

                for (int i = 0; i < listOfInvestments.size(); i++) {
                    if(listOfInvestments.get(i) instanceof Stock){
                        counter++;
                    }
                }

                if(counter == 0){
                    noStocks = true;
                    System.out.println("No stocks are owned. Please buy stocks to sell stocks.");
                }

                // if there are investments
                if(!noStocks) {
                    // enter the symbol of stock user wishes to purchase
                    System.out.println("Enter the symbol of the stock you wish to sell: ");
                    String symbolToSell = scanner.nextLine();
                    
                    for (int i = 0; i < listOfInvestments.size(); i++) {                    
                        if (listOfInvestments.get(i) instanceof Stock && listOfInvestments.get(i).getSymbol().equals(symbolToSell)) {

                            Stock stockSold = (Stock) listOfInvestments.get(i);

                            // modify the stock found in the array list
                            System.out.println(symbolToSell + " was found!");

                            // ask for quantity and price with validation
                            quantityToSell = quanityValidation(scanner, quantityToSell, false);
                        
                            // check if quantity to sell is larger than owned stock
                            while(quantityToSell > stockSold.getQuantity()){
                                System.out.println("ERROR: Not enough shares owned to sell.");
                                quantityToSell = quanityValidation(scanner, quantityToSell, false);       
                            }

                            // validate input price to sell
                            priceToSell = priceValidation(scanner, priceToSell, stockMode);

                            // for full purchase, index is removed so we must store the name of the stock in a temp var
                            String tempName = stockSold.getName();
                            
                            // perform the sale    
                            saleBookValue = stockSold.sell(quantityToSell, priceToSell, listOfInvestments, i);
                            // sell shares of a stock (if full sale, stock is removed from arraylist)
                            if(quantityToSell != 1){
                                System.out.println(quantityToSell + " shares of " + tempName + 
                                    " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                            } else {
                                System.out.println(quantityToSell + " share of " + tempName + 
                                    " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                            } 
                             
                            // consume new line character
                            scanner.nextLine();            

                        } else {
                            System.out.println("Error: the stock with symbol " + symbolToSell + " was not found.");
                            System.out.println("No stock was sold.");
                            break;
                        }
                    }
                   
                }

            // SELL A MUTUAL FUND
            } else if (sellInvestType.equalsIgnoreCase("mutual fund") || sellInvestType.equalsIgnoreCase("m")) {
                stockMode = false;

                int counter = 0;

                for (int i = 0; i < listOfInvestments.size(); i++) {
                    if(listOfInvestments.get(i) instanceof MutualFund){
                        counter++;
                    }
                }

                if(counter == 0){
                    noMutualFunds = true;
                    System.out.println("No mutual funds are owned. Please buy mutual funds to sell stocks.");
                }

                if(!noMutualFunds){
                    // enter the symbol of mutual fund user wishes to sell
                    System.out.println("Enter the symbol of the mutual fund you wish to sell: ");
                    String symbolToSell = scanner.nextLine();

                    
                    for (int i = 0; i < listOfInvestments.size(); i++) {
                        if (listOfInvestments.get(i) instanceof MutualFund && listOfInvestments.get(i).getSymbol().equals(symbolToSell)) {
                            MutualFund fundSold = (MutualFund) listOfInvestments.get(i);

                            // modify the mutual fund found in the array list
                            System.out.println(symbolToSell + " was found!");

                            // ask for quantity and price with validation
                            quantityToSell = quanityValidation(scanner, quantityToSell, false);

                            while(quantityToSell > fundSold.getQuantity()){
                                System.out.println("ERROR: Not enough units owned to sell.");
                                quantityToSell = quanityValidation(scanner, quantityToSell, false);
                            }

                            // validate input price to sell
                            priceToSell = priceValidation(scanner, priceToSell, stockMode);

                            // for full purchase, index is removed so we must store the name of the stock in a temp var
                            String tempName = fundSold.getName();

                            // perform mutual fund sale
                            saleBookValue = fundSold.sell(quantityToSell, priceToSell, listOfInvestments, i);
                            // sell units of a mutual fund (if full sale, stock is removed from arraylist)
                            if (quantityToSell != 1) {
                                System.out.println(quantityToSell + " units of " + tempName 
                                    + " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                            } else {
                                System.out.println(quantityToSell + " unit of " + tempName
                                    + " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                            }
                            // consume new line character
                            scanner.nextLine();
                        } else {
                            System.out.println("Error: the mutual fund with symbol " + symbolToSell + " was not found.");
                            System.out.println("No units were sold.\n");
                            break;
                        }
                    }
                   
                }     
            }   
        }
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
                    System.out.println("The current price for " + updatedStock.getSymbol() + " is: $" + updatedStock.getPrice());

                    // ask user for the updated price and validate
                    double updatedStockPrice = 0;
                    updatedStockPrice = priceValidationForUpdate(scanner, updatedStockPrice, updatingAStock);

                    updatedStock.setPrice(updatedStockPrice);
                    System.out.println("The updated price for " + updatedStock.getSymbol() + " is: $" + updatedStock.getPrice());
                    System.out.println(""); // spacer

                } 
                // if the current investment is a mutual fund object
                else if(listOfInvestments.get(i) instanceof MutualFund){
                    // for price validation
                    updatingAStock = false;

                    // create new mutual fund object by downcasting current investment
                    MutualFund updatedFund = (MutualFund)listOfInvestments.get(i);

                    // print current price
                    System.out.println("The current price for " + updatedFund.getSymbol() + " is: $" + updatedFund.getPrice());

                    // ask user for updated price and validate
                    double updatedFundPrice = 0;
                    updatedFundPrice = priceValidationForUpdate(scanner, updatedFundPrice, updatingAStock);

                    updatedFund.setPrice(updatedFundPrice);
                    System.out.println("The updated price for " + updatedFund.getSymbol() + " is: $" + updatedFund.getPrice());
                    System.out.println(""); // spacer
                }
                
            }
            scanner.nextLine(); // consume \n
        }
        
    }

    // GET GAIN ON INVESTMENTS FROM STOCK AND MUTUAL FUNDS
    /**
     * This methods allows user to check there current
     * gain of all their stocks and mutual funds
     */
    private void getInvestmentGain(){
        // PRINT GET GAIN TO USER
        for (int i = 0; i < listOfInvestments.size(); i++) {
            if(listOfInvestments.get(i) instanceof Stock){
                // create new stock object by downcasting current investment
                Stock aStock = (Stock)listOfInvestments.get(i);

                double stockGain = aStock.getGain(aStock.getQuantity(), aStock.getPrice());
                System.out.println(stockGain);
                gainSum += stockGain;
            } else if (listOfInvestments.get(i) instanceof MutualFund){
                // create new mutual fund object by downcasting current investment
                MutualFund aFund = (MutualFund)listOfInvestments.get(i);

                double fundGain = aFund.getGain(aFund.getQuantity(), aFund.getPrice());
                System.out.println(fundGain);
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
        String keywordSearch = scanner.nextLine().trim();
        System.out.println("Enter a price range seperated to search for (optional: leave blank to not search for any range)");
        String rangeSearch = scanner.nextLine().trim();

        boolean foundStock = true;

        HashMap<String, ArrayList<Integer>> index = new HashMap<String,ArrayList<Integer>>();
        ArrayList<Integer> keyAt = new ArrayList<>();

        // found counter
        int foundCount = 0;
        
        // search through all stocks
        for (int i = 0; i < listOfInvestments.size(); i++) {
            
            // check if keywords is not empty
            if(!keywordSearch.isEmpty()){
                // check if all keywords are not found
                String[] keywords = keywordSearch.split(" ");
                // check each keyword
                for (int j = 0; j < keywords.length; j++){
                    String keyword = keywords[j];
                    // if each keyword is not contained in current stock
                    System.out.println(j + " " + keyword);
                    if(listOfInvestments.get(i).getName().contains(keyword)){
                        System.out.println(listOfInvestments.get(i).getName());
                        keyAt.add(i);
                        index.put(keyword, keyAt);
                    }
                }
            }

            System.out.println(index);
            
            
            // check if symbol is not empty
            if(!symbolSearch.isEmpty()){
                // check if symbol is not equal to current stock
                if(!listOfInvestments.get(i).getSymbol().equalsIgnoreCase(symbolSearch.split(" ")[0])){
                    // set to false and don't print stock
                    foundStock = false;
                }
            }

            // check if price range not empty
            if(!rangeSearch.isEmpty()){
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
                        if (listOfInvestments.get(i).getPrice() < lowerValue){
                            foundStock = false;
                        }
                    }

                    // check if upper bound is not empty
                    if (!upperBound.isEmpty()) {
                        double upperValue = Double.parseDouble(upperBound);
                        // see if stock price is above upper bound
                        // if so dont print stock
                        if (listOfInvestments.get(i).getPrice() > upperValue) {
                            foundStock = false;
                        }
                    }
                } else {
                    double exactPrice = Double.parseDouble(rangeSearch);
                    // see if stock doesn't match exact price
                    // if so, dont print stock
                    if(listOfInvestments.get(i).getPrice() != exactPrice) {
                        foundStock = false;
                    }
                }
            }

            // if condition maintains true (survived all filters), then print
            if(foundStock){
                System.out.println(listOfInvestments.get(i).toString());
                foundCount ++;
            }

        }

        if (foundCount == 0){
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
    private int quanityValidation(Scanner scanner, int validQuantity, boolean ifBuy){
        boolean validInput = false;

        // loop until input is valid
        while (!validInput){
            try {
                // print dependant on buy or sell
                if(ifBuy){
                    System.out.println("Enter the quantity you would like to purchase: ");
                } else {
                    System.out.println("Enter the quantity you would like to sell: ");
                }
                // get quantity from user
                validQuantity = scanner.nextInt();

                // only exit loop if value is positive integer
                if (validQuantity < 0){
                    System.out.println("Enter a positive quantity.");
                } else {
                    validInput = true;
                }
            // throw input mismatch exception and ask again until valid
            } catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a whole number.");
                scanner.next();
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
    private double priceValidation(Scanner scanner, double validPrice, boolean ifStock){
        boolean validInput = false;

        // loop until valid input   
        while (!validInput){
            try {
                // print dependant on stock or mutual fund (i.e. shares or units)
                if(ifStock){
                    System.out.println("Enter the price of each share: ");
                } else {
                    System.out.println("Enter the price of each unit: ");
                }
                // get price from user
                validPrice = scanner.nextDouble();

                // only exit the loop if price is a positive number
                if (validPrice < 0){
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
                if (validPrice < 0){
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
}
