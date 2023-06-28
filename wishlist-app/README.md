# Project Proposal

The app that I will be creating is a budget/expense tracker. The main functionality will include keeping track of the 
total amount of money that is available to spend, keeping track of purchases, and the ability to budget for items that 
the user wishes to save up for. 

Some practical examples for these functionalities are:
- If a user wishes to keep track of their total paychecks, the user can input the sum of money into the app.
- If a user makes a purchase, the app will automatically subtract the amount of the purchase from the total balance.
A user can go "in debt" and get a negative balance if they have made too many purchases. 
- If a user wishes to save up for an item such as a TV, the app will be able to list desired items, and user can 
choose to allocate a sum of their total balance to the item, the amount will be deducted as if a purchase was made,
unless there are insufficient funds to save. The
user will not be able to purchase an item off of the wishlist unless they have sufficient amount in their balance. 

It is important that the **history** of all "deposits" and purchases are kept in the case that the user wants to undo any 
actions, such as deleting a purchase. This app can be used by anyone who wishes to save up for any items and wants to 
keep track of the amount of money they have available to spend. It will also be useful to keep track of any recent 
purchases they have made. 

This project interests me because I want to design an app that is practical in day-to-day life to the average person. 
I personally am trying to save up for certain items and vacations, and it inspired the thought of how I can create an 
app that can organize my wishlist as well as being able to keep track of money flow in or out. I want to create this app 
specifically because I find that many budget tracker apps have the main functionality of keeping track of purchases, 
but lack the extra feature to create a wish list of items to save up for. 

### User Stories ###
- As a user, I want to be able to add an item with its price on my wishlist.
- As a user, I want to be able to add a recent purchase to the list of purchases.
- As a user, I want to be able to "deposit" money and keep track of the total amount acquired.
- As a user, I want to be able to allocate a sum of money to any wishlist item.

- As a user, I want to be able to save my app, and be reminded before quitting the application.
- As a user, I want to be able to load my previous saved data from the wish app

### Phase4: Task 2 ###
Thu Nov 25 14:20:07 PST 2021

10000.0 deposited to account

Thu Nov 25 14:20:16 PST 2021

Groceries of price 100.0 added to purchase list

Thu Nov 25 14:20:16 PST 2021

100.0 deducted from account

Thu Nov 25 14:20:26 PST 2021

Laptop of price 1000.0 added to wish list

Thu Nov 25 14:20:40 PST 2021

500.0 allocated to Laptop

Thu Nov 25 14:20:40 PST 2021

500.0 deducted from account

Thu Nov 25 14:20:48 PST 2021

500.0 deducted from account

Thu Nov 25 14:20:48 PST 2021

500.0 allocated to Laptop

Thu Nov 25 14:20:52 PST 2021

Laptop has been purchased off wish list

Thu Nov 25 14:20:52 PST 2021

Laptop removed from wish list

Thu Nov 25 14:20:52 PST 2021

Laptop of price 1000.0 added to purchase list

Thu Nov 25 14:20:55 PST 2021

data saved to file

### Phase 4: Task 3 ###
- change all the fields in the handlers to local variables so there does not need to be an association between the 
 handlers and the model classes
- make an abstract class called "Item" to capture the similar behaviour of the Purchase and Wish classes
- ^ same thing but abstract from WishList and PurchaseList