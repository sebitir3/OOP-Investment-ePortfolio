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

For the menu loop, accepted options for all investment functions is either the first word
of the function name (b, s, u, g, se) or the whole word (buy, sell, update, gain, search). These
inputs are also case insensitive. These input options were chosen as they are the most logical
options that a user would input.

The same goes for inputing a stock or mutual fund, being that the user can input the entire
word (stock, mutual fund) or the first letter of each (s, m) all case insensitive as well.

3) USER GUIDE:
Compilition and execution:
Run the following in a terminal from the main project folder (i.e. stiriba_a1) 
$ javac ePortfolio/*.java
$ jar cvfe A2.jar ePortfolio.Portfolio ePortfolio/*.class
$ java -jar A2.jar

A menu of options will then be presented to the user.
First, (1) Buy option should be selected first to buy your investment

From there, you have the option to ...
(1) Buy --> buys more investments (new or currently owned investments).

The user will get prompted to enter the symbol of the investment. If its a new
investment being entered the user will be asked to input the name, along with
quantity being purchased, and the price at purchase time.

If the investment already exists, the name is not required to be entered,
and the user will be asked to enter quantity and price as usual.

(2) Sell --> sell investments that you currently own

The user will be prompted to enter a symbol of an investment they own.
If a symbol is not owned, the operation will stop and you must select the (2) option again.

When a symbol is found, the user will be prompted to input the quantity of investment being
sold as well as the price at time of sale.

(3) Update Price --> allows you to change the price of all your investments

The program will view every investment and the user will be asked to input a new price
for each investment.

(4) Get Gain --> displays the current gain of all investments

No user input required besides selecting option (4). Automatically displays current
investment gain.

(5) Search --> return the investment depending on search filters for symbol, keyword, and price range

The user is asked to input search filters for symbol, keyword and a price range.
They DO NOT all have to be entered and if user wants to not use a filter, it can be left blank.

The symbol filter: the user can enter a symbol of an investment they want to search for (case insensitive).

The keyword filter: the user can enter keyword of the name of an investment to search for (case insensitive).
All keywords must be seperated by a space. (Ex: "Apple Inc" will search for an investment
that includes both keywords "Apple" and "Inc")

The price range filter: the user can input a price range of investments they want to search for.

A full range should be inputed as "0-100". This searches for investments between $0 and $100 inclusive.
A lower bound range should be inputed as "0-". This searches for investments above $0 inclusive.
An upper bound range should be inputed as "-100". This searches for investments below $100 inclusive.
An exact price should be inputed as "100". This searches for investments priced at exactly $100.


4) TEST PLAN:

MENU LOOP TESTING
Test: if all inputs (buy or b case insensitive, sell or s case insensitive etc.) enter each function ✔
      enter values outside of the cases; test if loop asks for another option to be entered ✔

INVESTMENT FUNCTION TESTING:

Buy
Test: if input for buying stock is accepted as 'stock' or 's' case insensitive and input for buying mutual fund is accepted as 'mutual fund' or 'm' ✔
      if symbol is found or not --> if not enter name for new investment, then quantity and price; else enter the quantity and price of the found investment ✔
      validate numerical inputs for quanitity and price using helper validate methods ✔

Sell
Test: if input for selling stock is accepted as 'stock' or 's' case insensitive and input for selling mutual fund is accepted as 'mutual fund' or 'm' ✔
      if symbol is found or not --> exit function if not found, continue with sell inputs if symbol found 
      validate numerical inputs for quanitity and price using helper validate methods ✔
      if list of chosen investment is empty, don't sell ✔

Update
Test: if inputs for the updated prices are numbers or not: i.e. try strings/garbage values to enter try catch --> throws type mismatch execption ✔
      if list is empty --> message to buy investments should be display ✔

Gain
Test: do the math and see if correct output is printed when calculating the theoretical gain of all investments ✔

Search
Test: all combinations of symbol, keywords, and ranges --> i.e. if some left blank, it still searches for remaining filters ✔
      if only first string is taken as the symbol search filter ✔
      if lowercase string is accepted as a symbol search filter ✔
      if all keyword are found within the name of an investment (must contain all keywords entered) ✔
      if all types of ranges and edge cases work ✔
            i.e.(exact value, range above, range below, full range, and ranges that don't make sense such as 500-100) ✔
      if no investment found --> message should be printed that none were found ✔

Exit
Test: if program is terminated ✔

5) IMPROVEMENTS TO DO:

First of all, a parent class for both Stock.java and MutualFund.java should be created to
allow for less duplication of code, as well as only managing 1 array list of investments,
and not 1 array list of each type of investment (MutualFund.java and Stock.java are almost
identical classes that can easily extend an investment.java class in the future).

For selling, if no symbol is found, the function in the future should ask for another
investment symbol within the function rather than exiting the function (lessens the user
inputs having to call the function again).

For later versions, simplify the validate helper functions --> at least have 1 price validation and
not 2 seperate functions for validating price when buying/selling and another for updating price.

Bigger picture: investment price information could be pulled from a database of prices
to allow for automatic and periodic price changes so the user can save time when
calculating the gain on their investments.
