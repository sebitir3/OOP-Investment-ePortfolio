1) DESCRIPTION:
This program allows users (in this case investors) to manage an
electronic portfolio of different investments (stocks, and mutual funds)

This program allows users to buy investments, sell investments, see their
gain from all their investments currently, update the prices of their investments
as well as search for certain investments that they have in the list of all investments.

2) ASSUMPTIONS AND LIMITATIONS:
We are assuming the user is inputing actual stocks and/or mutual funds into the e-portfolio
as they are not being validated with actual stock and mutual funds in the real world.

The program is limited by the fact that the user has to manually update the prices of the
investments where normally the prices themselves would automatically update periodically.

3) USER GUIDE:
Compilition and execution:
Run the following in a terminal from the main project folder (i.e. stiriba_a3)
$ javac ePortfolio/*.java
$ jar cvfe A3.jar ePortfolio.GUI <filename> ePortfolio/*.class
$ java -jar A3.jar

Here, filename is optional. If not specified it will write to "default.txt".

An initial window will pop up and explain to the user than they are within the ePortfolio and the different functions
are found within the menu. The menu options are ...

(1) Buy --> buys more investments (stock or mutual fund from the dropdown menu).

To buy, the user must enter the symbol, the name, along with
quantity being purchased, and the price at purchase time of the investment. 

Once all these are fields are full the user can press the buy button and the purchase
will be confirmed in the message console.

The reset button clears all fields.

*All fields must be full to complete a purchase. Price and quanitity must be positive, non zero numbers.

(2) Sell --> sell investments that you currently own

To sell, the user must enter the symbol, along with
quantity being sold, and the price at time of sale of the investment. 

Once all these are fields are full the user can press the sell button and the sale
will be confirmed in the message console.

The reset button clears all fields.

*All fields must be full to complete a sale. Price and quanitity must be positive, non zero numbers.

(3) Update Price --> allows you to change the price of all your investments

To update a price of an investment, the user must enter the 
new desired price of the investment within the price field of the invesment.
The symbol and name fields cannot be modified.

The next and previous buttons cycle through the list of investments for
the user to be able to modify them.

Once all these are fields are full the user can press the sell button and the sale
will be confirmed in the message console.

The reset button clears all fields.

*Price must be a positive, non zero number.

(4) Get Gain --> displays the current gain of all investments

No user input required. Automatically displays current
investment gain and the individual gains of each investment.

(5) Search --> return the investment depending on search filters for symbol, keyword, and price range

To search, the user must input search filters for symbol, keyword and a price range.
They DO NOT all have to be entered and if user wants to not use a filter, it can be left blank.

The symbol filter: the user can enter a symbol of an investment they want to search for (case insensitive).

The keyword filter: the user can enter keyword of the name of an investment to search for (case insensitive).
All keywords must be seperated by a space. (Ex: "Apple Inc" will search for an investment
that includes both keywords "Apple" and "Inc")

The price range filter: the user can input a price range of investments they want to search for.

A lower bound and upper bound can be specified to filter the prices of your investments.

The reset button clears all fields.

*The lower bound must be lower than the higher bound.

4) TEST PLAN:
INVESTMENT FUNCTION TESTING:

GUI:
Test: if all panels appear as they should and all buttons / interactable components do the tasks below
      if appropriate confirmation / error messages appear in the consoles of each panel


Buy
Test: if symbol is found or not --> if not enter name for new investment, then quantity and price; else enter the quantity and price of the found investment ✔
      validate numerical inputs for quanitity and price using helper validate methods making sure price and quantity are larger than 0 ✔

Sell
Test: if symbol is found or not --> exit function if not found, continue with sell inputs if symbol found
      validate numerical inputs for quanitity and price using helper validate methods making sure price and quantity are larger than 0 ✔
      if list of chosen investment is empty, don't sell ✔

Update
Test: if inputs for the updated prices are numbers or not: i.e. try strings/garbage values to enter try catch --> throws type mismatch execption ✔
      if list is empty --> message to buy investments should be display ✔

Gain
Test: do the math and see if correct output is printed when calculating the theoretical gain of all investments ✔
      if each investment's gain appears in the console

Search
Test: all combinations of symbol, keywords, and ranges --> i.e. if some left blank, it still searches for remaining filters ✔
      if only first string is taken as the symbol search filter ✔
      if lowercase string is accepted as a symbol search filter ✔
      if all keyword are found within the name of an investment (must contain all keywords entered) ✔
      if all types of ranges and edge cases work ✔
            i.e.(range above, range below, full range, and ranges that don't make sense such as 500-100) ✔
            --> return an error and exit the search loop ✔
      if no investment found --> message should be printed that none were found ✔

Hashmap
Test: that upon buy and sell function calls that the indices of all the keywords in investment names update accordingly (adding and removing) ✔
      that it is parsed correctly in the search function so all the indices that match the input search keywords are properly intersected ✔

Exit
Test: if program is terminated ✔
      if program closes only on quit selection option so file write occurs ✔

Read & Write to file
Test: if program accepts command line argument file name --> if so make sure to loop through array list of investments and see if all information was read in ✔
      if program doesn't accept a command line argument file name --> make sure to create a default text file and check array list for no investments read in since no file specified ✔
     
      if program successfully writes to the given read option above (no file specified or file name specified) ✔
      also test entering a nonexistant filename as a command line argument --> should catch as an exception ✔
            and set file to write to as default.txt ✔

5) IMPROVEMENTS TO DO:

Bigger picture: investment price information could be pulled from a database of prices to allow for automatic and
periodic price changes so the user can save time when calculating the gain on their investments.
