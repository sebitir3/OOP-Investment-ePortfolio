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
public class Portfolio {
    private static ArrayList<Stock> listOfStocks = new ArrayList<>();
    private static ArrayList<MutualFund> listOfMutualFunds = new ArrayList<>();
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
        String buyInvestType = scanner.nextLine();
    
        // validate input until user correctly enters stock or mutual fund
        while (!buyInvestType.equalsIgnoreCase("stock") && !buyInvestType.equalsIgnoreCase("mutual fund") 
            && !buyInvestType.equalsIgnoreCase("s") && !buyInvestType.equalsIgnoreCase("m")){
            System.out.println("Enter a valid investment type (stock or mutual fund): ");
            buyInvestType = scanner.nextLine();
        }

        // BUY A STOCK

        if (buyInvestType.equalsIgnoreCase("stock") || buyInvestType.equalsIgnoreCase("s")) {
            stockMode = true;
            // enter the symbol of stock user wishes to purchase
            System.out.println("Enter the symbol of the stock you wish to purchase: ");
            String symbolToBuy = scanner.nextLine();

            // boolean to check if stock is found
            boolean foundStockToBuy = false;

            // check if stock already exists in array list
            for (int i = 0; i < listOfStocks.size(); i++) {
                if (listOfStocks.get(i).getSymbol().equals(symbolToBuy) && !foundStockToBuy) {
                    // set condition to true so the next iteration doesnt enter loop
                    foundStockToBuy = true;

                    // modify the stock found in the array list
                    System.out.println(symbolToBuy + " was found!");

                    // ask for quantity and price with validation
                    quantityToBuy = quanityValidation(scanner, quantityToBuy, true);
                    priceToBuy = priceValidation(scanner, priceToBuy, stockMode);
                    
                    // buy shares of a stock
                    purchaseBookValue = listOfStocks.get(i).buy(quantityToBuy, priceToBuy);
                    if (quantityToBuy != 1){
                        System.out.println(quantityToBuy + " shares of " + listOfStocks.get(i).getName() 
                            + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    } else {
                        System.out.println(quantityToBuy + " share of " + listOfStocks.get(i).getName() 
                            + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    }
                    break;

                }
            }

            // if stock not found create NEW STOCK
            if (!foundStockToBuy) {
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
                listOfStocks.add(inputStock);
            }

            // BUY A MUTUAL FUND    

        } else if (buyInvestType.equalsIgnoreCase("mutual fund") || buyInvestType.equalsIgnoreCase("m")) {
            stockMode = false;
            // enter the symbol of mutual fund user wishes to purchase
            System.out.println("Enter the symbol of the mutual fund you wish to purchase: ");
            String symbolToBuy = scanner.nextLine();

            // boolean to check if stock is found
            boolean foundFundToBuy = false;

            // check if stock already exists in array list
            for (int i = 0; i < listOfMutualFunds.size(); i++) {
                if (listOfMutualFunds.get(i).getSymbol().equals(symbolToBuy) && !foundFundToBuy) {
                    // set condition to true so the next iteration doesnt enter loop
                    foundFundToBuy = true;

                    // modify the mutual fund found in the array list
                    System.out.println(symbolToBuy + " was found!");

                    // ask for quantity and price with validation
                    quantityToBuy = quanityValidation(scanner, quantityToBuy, true);
                    priceToBuy = priceValidation(scanner, priceToBuy, stockMode);

                    // buy shares of a stock
                    purchaseBookValue = listOfMutualFunds.get(i).buy(quantityToBuy, priceToBuy);
                    if(quantityToBuy != 1){
                        System.out.println(quantityToBuy + " units of " + listOfMutualFunds.get(i).getName() 
                            + " (" + symbolToBuy + ") were bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    } else {
                        System.out.println(quantityToBuy + " unit of " + listOfMutualFunds.get(i).getName() 
                            + " (" + symbolToBuy + ") was bought at $" + df.format(priceToBuy) + " | Book Value of Purchase: $" + df.format(purchaseBookValue));
                    }                   
                    break;
                }
            }

            // if fund not found create NEW MUTUAL FUND
            if (!foundFundToBuy) {
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
                listOfMutualFunds.add(inputMutualFund);
            }
        }
        // clear new line character for next function call in switch case
        scanner.nextLine();
    }

    // SELL A STOCK OR MUTUAL FUND
    /**
     * This methods allows the user to sell a stock or a mutual fund
     *
     * @param scanner - for user inputs
     */
    private void sellInvestments(Scanner scanner){
        System.out.println("Enter the type of investment you want to sell: ");
        String sellInvestType = scanner.nextLine();
        int quantityToSell = 0;
        double priceToSell = 0;
        // if selling a stock or mutual fund
        boolean stockMode = false;

        // book value @ sale
        double saleBookValue;

        // SELL A STOCK

        while (!sellInvestType.equalsIgnoreCase("stock") && !sellInvestType.equalsIgnoreCase("mutual fund")
                && !sellInvestType.equalsIgnoreCase("s") && !sellInvestType.equalsIgnoreCase("m")){
            System.out.println("Enter the type of a valid investment type (stock or mutual fund): ");
            sellInvestType = scanner.nextLine();
        }

        if (sellInvestType.equalsIgnoreCase("stock") || sellInvestType.equalsIgnoreCase("s")) {
            stockMode = true;
            boolean stockListIsEmpty = false;

            // check if stock array list is empty and exit if true
            if(listOfStocks.isEmpty()){
                System.out.println("You have no stock investments. Please buy stock to be able to sell.");
                stockListIsEmpty = true;
            }

            if(!stockListIsEmpty) {
                // enter the symbol of stock user wishes to purchase
                System.out.println("Enter the symbol of the stock you wish to sell: ");
                String symbolToSell = scanner.nextLine();

                boolean foundStockToSell = false;

                for (int i = 0; i < listOfStocks.size(); i++) {
                    if (listOfStocks.get(i).getSymbol().equals(symbolToSell) && !foundStockToSell) {
                        // set condition to true so the next iteration doesnt enter loop
                        foundStockToSell = true;

                        // modify the stock found in the array list
                        System.out.println(symbolToSell + " was found!");

                        // ask for quantity and price with validation
                        quantityToSell = quanityValidation(scanner, quantityToSell, false);
                    
                        // check if quantity to sell is larger than owned stock
                        while(quantityToSell > listOfStocks.get(i).getQuantity()){
                            System.out.println("ERROR: Not enough shares owned to sell.");
                            quantityToSell = quanityValidation(scanner, quantityToSell, false);       
                        }

                        // validate input price to sell
                        priceToSell = priceValidation(scanner, priceToSell, stockMode);

                        // for full purchase, index is removed so we must store the name of the stock in a temp var
                        String tempName = listOfStocks.get(i).getName();
                        
                        // perform the sale    
                        saleBookValue = listOfStocks.get(i).sell(quantityToSell, priceToSell, listOfStocks, i);
                        // sell shares of a stock (if full sale, stock is removed from arraylist)
                        if(quantityToSell != 1){
                            System.out.println(quantityToSell + " shares of " + tempName + 
                                " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                        } else {
                            System.out.println(quantityToSell + " share of " + tempName + 
                                " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                        }             
                        break;

                    } else {
                        System.out.println("Error: the stock with symbol " + symbolToSell + " was not found.");
                        System.out.println("No stock was sold.");
                        break;
                    }
                }
                // consume new line character
                scanner.nextLine();
            }

            // SELL A MUTUAL FUND

        } else if (sellInvestType.equalsIgnoreCase("mutual fund") || sellInvestType.equalsIgnoreCase("m")) {
            stockMode = false;

            boolean fundListIsEmpty = false;

            // check if mutual fund array list is empty and exit if true
            if(listOfMutualFunds.isEmpty()){
                System.out.println("You have no mutual fund investments. Please buy mutual funds to be able to sell.");
                fundListIsEmpty = true;
            }

            if(!fundListIsEmpty){
                // enter the symbol of mutual fund user wishes to sell
                System.out.println("Enter the symbol of the mutual fund you wish to sell: ");
                String symbolToSell = scanner.nextLine();

                boolean foundFundToSell = false;

                for (int i = 0; i < listOfMutualFunds.size(); i++) {
                    if (listOfMutualFunds.get(i).getSymbol().equals(symbolToSell) && !foundFundToSell) {
                        // set condition to true so the next iteration doesnt enter loop
                        foundFundToSell = true;

                        // modify the mutual fund found in the array list
                        System.out.println(symbolToSell + " was found!");

                        // ask for quantity and price with validation
                        quantityToSell = quanityValidation(scanner, quantityToSell, false);

                        while(quantityToSell > listOfMutualFunds.get(i).getQuantity()){
                            System.out.println("ERROR: Not enough units owned to sell.");
                            quantityToSell = quanityValidation(scanner, quantityToSell, false);
                        }

                        // validate input price to sell
                        priceToSell = priceValidation(scanner, priceToSell, stockMode);

                        // for full purchase, index is removed so we must store the name of the stock in a temp var
                        String tempName = listOfMutualFunds.get(i).getName();

                        // perform mutual fund sale
                        saleBookValue = listOfMutualFunds.get(i).sell(quantityToSell, priceToSell, listOfMutualFunds, i);
                        // sell units of a mutual fund (if full sale, stock is removed from arraylist)
                        if (quantityToSell != 1) {
                            System.out.println(quantityToSell + " units of " + tempName 
                                + " (" + symbolToSell + ") were sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                        } else {
                            System.out.println(quantityToSell + " unit of " + tempName
                                + " (" + symbolToSell + ") was sold at $" + df.format(priceToSell) + " | Book Value of Sale: $" + df.format(saleBookValue));
                        }
                        break;
                    } else {
                        System.out.println("Error: the mutual fund with symbol " + symbolToSell + " was not found.");
                        System.out.println("No units were sold.");
                        break;
                    }
                }
                // consume new line character
                scanner.nextLine();
            }     
        }
        // clear new line character for next function call in switch case
        //scanner.nextLine();
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
        if (listOfStocks.isEmpty() && listOfMutualFunds.isEmpty()) {
            System.out.println("There are no investments to update. Please buy investments to update prices.");
        } else {
            for (int i = 0; i < listOfStocks.size(); i++) {
                System.out.println("The current price for " + listOfStocks.get(i).getSymbol() + " is: $" + listOfStocks.get(i).getPrice());

                double updateStockPrice = 0;
                updateStockPrice = priceValidationForUpdate(scanner, updateStockPrice, updatingAStock);

                listOfStocks.get(i).setPrice(updateStockPrice);
                System.out.println("The updated price for " + listOfStocks.get(i).getSymbol() + " is: $" + listOfStocks.get(i).getPrice());
                System.out.println(""); // spacer
            }
            // next update mutual funds
            updatingAStock = false;
            // iterate through all mutual funds and update price for each mutual fund
            for (int i = 0; i < listOfMutualFunds.size(); i++) {
                System.out.println("The current price for " + listOfMutualFunds.get(i).getSymbol() + " is: $" + listOfMutualFunds.get(i).getPrice());

                double updateFundPrice = 0;
                updateFundPrice = priceValidationForUpdate(scanner, updateFundPrice, updatingAStock);

                listOfMutualFunds.get(i).setPrice(updateFundPrice);
                System.out.println("The updated price for " + listOfMutualFunds.get(i).getSymbol() + " is: $" + listOfMutualFunds.get(i).getPrice());
                System.out.println(""); // spacer
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
        for (int i = 0; i < listOfStocks.size(); i++) {
            double stockGain = listOfStocks.get(i).getGain(listOfStocks.get(i).getQuantity(), listOfStocks.get(i).getPrice());
            gainSum += stockGain;
        }
        // iterate through all mutual funds and update price for each mutual fund
        for (int i = 0; i < listOfMutualFunds.size(); i++) {
            double fundGain = listOfMutualFunds.get(i).getGain(listOfMutualFunds.get(i).getQuantity(), listOfMutualFunds.get(i).getPrice());
            gainSum += fundGain;
        }
        // set decimal format rounding mode to round up
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

        // boolean if investment found variables
        boolean foundFund = true;
        boolean foundStock = true;

        // found counter
        int foundCount = 0;
        
        // search through all stocks
        for (int i = 0; i < listOfStocks.size(); i++) {
            // check if symbol is not empty
            if(!symbolSearch.isEmpty()){
                // check if symbol is not equal to current stock
                if(!listOfStocks.get(i).getSymbol().equalsIgnoreCase(symbolSearch.split(" ")[0])){
                    // set to false and don't print stock
                    foundStock = false;
                }
            }

            // check if keywords is not empty
            if(!keywordSearch.isEmpty()){
                // check if all keywords are not found
                String[] keywords = keywordSearch.split(" ");
                // check each keyword
                for (String keyword : keywords){
                    // if each keyword is not contained in current stock
                    if(!listOfStocks.get(i).getName().contains(keyword)){
                        // set to false and don't print stock
                        foundStock = false;
                    }
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
                        if (listOfStocks.get(i).getPrice() < lowerValue){
                            foundStock = false;
                        }
                    }

                    // check if upper bound is not empty
                    if (!upperBound.isEmpty()) {
                        double upperValue = Double.parseDouble(upperBound);
                        // see if stock price is above upper bound
                        // if so dont print stock
                        if (listOfStocks.get(i).getPrice() > upperValue) {
                            foundStock = false;
                        }
                    }
                } else {
                    double exactPrice = Double.parseDouble(rangeSearch);
                    // see if stock doesn't match exact price
                    // if so, dont print stock
                    if(listOfStocks.get(i).getPrice() != exactPrice) {
                        foundStock = false;
                    }
                }
            }

            // if condition maintains true (survived all filters), then print
            if(foundStock){
                System.out.println(listOfStocks.get(i).toString());
                foundCount ++;
            }

        }

        // iterate through all mutual funds and update price for each mutual fund
        for (int i = 0; i < listOfMutualFunds.size(); i++) {
            // check if symbol is not empty
            if (!symbolSearch.isEmpty()) {
                // check if symbol is not equal to current fund
                if (!listOfMutualFunds.get(i).getSymbol().equalsIgnoreCase(symbolSearch.split(" ")[0])) {
                    // set to false and don't print fund
                    foundFund = false;
                }
            }

            // check if keywords is not empty
            if (!keywordSearch.isEmpty()) {
                // check if all keywords are not found
                String[] keywords = keywordSearch.split(" ");
                // check each keyword
                for (String keyword : keywords) {
                    // if each keyword is not contained in current fund
                    if (!listOfMutualFunds.get(i).getName().contains(keyword)) {
                        // set to false and don't print fund
                        foundFund = false;
                    }
                }
            }

            // check if price range not empty
            if (!rangeSearch.isEmpty()) {
                // if the range has - in it, it means its not an exact value
                if (rangeSearch.contains("-")) {
                    // set upper and lower bounds
                    String[] bounds = rangeSearch.split("-");
                    String lowerBound = bounds[0].trim();
                    String upperBound;
                    if (bounds.length > 1) {
                        upperBound = bounds[1].trim();
                    } else {
                        upperBound = "";
                    }

                    // check if lower bound is not empty
                    if (!lowerBound.isEmpty()) {
                        double lowerValue = Double.parseDouble(lowerBound);
                        // see if fund price falls below lower bound
                        // if so dont print the fund
                        if (listOfMutualFunds.get(i).getPrice() < lowerValue) {
                            foundFund = false;
                        }
                    }

                    // check if upper bound is not empty
                    if (!upperBound.isEmpty()) {
                        double upperValue = Double.parseDouble(upperBound);
                        // see if fund price falls below lower bound
                        // if so dont print the fund
                        if (listOfMutualFunds.get(i).getPrice() > upperValue) {
                            foundFund = false;
                        }
                    }
                } else {
                    double exactPrice = Double.parseDouble(rangeSearch);
                    // see if fund price doesn't match exact price
                    // if so, dont print fund
                    if (listOfMutualFunds.get(i).getPrice() != exactPrice) {
                        foundFund = false;
                    }
                }
            }

            // if condition maintains true (survived all filters), then print
            if (foundFund) {
                System.out.println(listOfMutualFunds.get(i).toString());
                foundCount++;
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