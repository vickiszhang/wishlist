package ui;

import model.*;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// A WishApp class
public class WishApp {

    private Balance balance;
    private PurchaseList purList;
    private WishList wishList;
    private Scanner input;

    private static final String JSON_STORE = "./data/wish_app_data.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the WishApp
    public WishApp() throws FileNotFoundException {
        runWishApp();
    }

    // MODIFIES: this
    // EFFECTS: while app is running, display menu and get commands
    private void runWishApp() {
        boolean keepGoing = true;
        String command;

        initialize();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("x")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nQuitting app...");
    }

    // MODIFIES: this
    // EFFECTS: instantiates the fields
    private void initialize() {
        balance = new Balance();
        purList = new PurchaseList();
        wishList = new WishList();
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

    }

    ///// code above is inspired from TellerApp

    // EFFECTS: prints out menu in console
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tw: Wish for an item");
        System.out.println("\ta: Allocate money for a wish");
        System.out.println("\tp: Add a recent purchase");
        System.out.println("\tb: Check balance");
        System.out.println("\td: Deposit amount");
        System.out.println("\ts: Save data to file");
        System.out.println("\tl: Load data from file");
        System.out.println("\tx: Exit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        switch (command) {
            case "w": wishForItem();
                break;
            case "a": checkHaveWishes();
                break;
            case "p": enterPurchase();
                break;
            case "b": checkBalance();
                break;
            case "d": addDeposit();
                break;
            case "s": saveWishData();
                break;
            case "l": loadWishData();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: get the user's wish and stores it
    private void wishForItem() {

        System.out.println("Wish for an Item!");

        String wishName = getItemName();
        double wishPrice = getItemPrice();
        Category wishCategory = getItemCategory();

        Wish wishInput = new Wish(wishName, wishPrice, wishCategory);
        wishList.addWish(wishInput);
        System.out.println(wishName + " of price " + "$" + wishPrice
                + " has been added to wishlist.");

    }

    // MODIFIES: this
    // EFFECTS: checks if wishList is empty, displays appropriate statement
    private void checkHaveWishes() {
        if (wishList.size() == 0) {
            System.out.println("You do not have any wishes.");
        } else {
            chooseWish();
        }
    }

    // REQUIRES: wishList.size() > 0
    // MODIFIES: this
    // EFFECTS: displays all wishs, gets user input for selection of wish
    private void chooseWish() {
        System.out.println("Enter the number of the item you wish to save for.");

        String choice = "";
        int selection = 1;

        Wish nextWish;
        String nextWishName;
        double nextWishPrice;

        Wish chosenWish;

        for (int i = 0; i < wishList.size(); i++) {

            nextWish = wishList.getWish(i);

            nextWishName = nextWish.getName();
            nextWishPrice = nextWish.getPrice();

            System.out.println("[" + selection + "]" + " Item: " + nextWishName + " | Price: $" + nextWishPrice);
            selection++;
        }

        while (choice.isEmpty() || !isNumeric(choice) || !inRange(Integer.parseInt(choice), wishList)) {
            choice = input.nextLine();
        }

        chosenWish = wishList.getWish(Integer.parseInt(choice) - 1);
        if (chosenWish.isBought()) {
            System.out.println("You already have sufficient amount saved for this item.");
        } else {
            allocateMoney(chosenWish);
        }
    }

    // EFFECTS: return true if choiceNum is in range of 0 and wishList size
    private boolean inRange(Integer choiceNum, WishList currWishList) {
        return (0 < choiceNum) && (choiceNum <= currWishList.size());
    }

    // REQUIRES: savingAmount > 0
    // MODIFIES: this
    // EFFECTS: adds the user input amount to the chosenWish
    private void allocateMoney(Wish chosenWish) {
        System.out.print("Enter amount to save: ");
        String savingAmount = input.nextLine();

        while (savingAmount.isEmpty() || !isNumeric(savingAmount)) {
            savingAmount = input.nextLine();
        }

        double savingAmountNum = Double.parseDouble(savingAmount);
        if (savingAmountNum > balance.getCurrentBalance()) {
            System.out.println("You do not have sufficient funds to save $" + savingAmount + ".");

        } else if (savedSufficient(savingAmountNum, chosenWish)) {
            sufficientStatusPath(savingAmountNum, chosenWish);

        } else {
            System.out.println("$" + savingAmount + " has been saved for " + chosenWish.getName() + ".");
            System.out.println("Amount has been deducted from balance.");
            chosenWish.allocate(savingAmountNum);
            balance.deduct(savingAmountNum);
        }
    }

    // EFFECTS: return true if user input plus the amount already allocated is
    //          sufficient for the chosenWish
    private boolean savedSufficient(double savingAmountNum, Wish chosenWish) {
        return (savingAmountNum + chosenWish.getAllocated() >= chosenWish.getPrice());
    }

    // MODIFIES: this
    // EFFECTS: console prints out appropriate info, balance is deducted, Wish is allocated
    private void sufficientStatusPath(double savingAmountNum, Wish chosenWish) {

        System.out.println("You are able to purchase this Item!");

        if (savingAmountNum + chosenWish.getAllocated() > chosenWish.getPrice()) {
            System.out.println("You have funded an extra of $"
                    + (chosenWish.getAllocated() + savingAmountNum - chosenWish.getPrice()));
        }
        System.out.println("$" + chosenWish.getPrice() + " is available for this item");

        balance.deduct(chosenWish.getPrice() - chosenWish.getAllocated());
        chosenWish.allocate(savingAmountNum); //method deals with oversaving
    }

    // MODIFIES: this
    // EFFECTS: get the user's purchase from input and stores it, deducts balance
    private void enterPurchase() {
        System.out.println("Add a purchase!");

        String purchaseName = getItemName();
        double purchasePrice = getItemPrice();
        Category purchaseCategory = getItemCategory();

        System.out.println(purchaseName + " of price " + "$" + purchasePrice
                + " has been added to purchases.");
        System.out.println("$" + purchasePrice + " has been deducted from your balance.");

        Purchase purchaseInput = new Purchase(purchaseName, purchasePrice, purchaseCategory);
        purList.addPurchase(purchaseInput);
        boolean posBalance = balance.deduct(purchasePrice);

        if (!posBalance) {
            System.out.println("Warning: You have a negative balance of " + "$"
                    + balance.getCurrentBalance() + ".");
        }

    }

    // EFFECTS: gets user input and returns the string
    private String getItemName() {

        System.out.print("Enter item: ");
        String itemName = "";

        while (itemName.isEmpty()) {
            itemName = input.nextLine();
        }

        return itemName;
    }

    // EFFECTS: gets user input and returns the amount as a double
    private double getItemPrice() {

        System.out.print("Enter price: ");
        String itemPrice = "";

        while (itemPrice.isEmpty() || !isNumeric(itemPrice)) {
            itemPrice = input.nextLine();
        }

        return Double.parseDouble(itemPrice);
    }

    private Category getItemCategory() {
        System.out.println("Choose a category.");

        String choice = "";
        int menuLabel = 1;

        for (Category c : Category.values()) {
            System.out.println(menuLabel + ": " + c);
            menuLabel++;
        }

        while (choice.isEmpty() || !isNumeric(choice) || !(0 < Integer.parseInt(choice)
                && Integer.parseInt(choice) <= 7)) {
            choice = input.nextLine();
        }
        return Category.values()[Integer.parseInt(choice) - 1];
    }

    // EFFECTS: shows the current amount in balance
    private void checkBalance() {
        System.out.println("You currently have a balance of " + "$"
                + balance.getCurrentBalance() + ".");
    }

    // MODIFIES: this
    // EFFECTS: prompts user to input an amount, adds amount to balance
    private void addDeposit() {

        System.out.print("Enter the amount to deposit: ");
        String deposit = "";

        while (deposit.isEmpty() || !isNumeric(deposit)) {
            deposit = input.nextLine();

        }

        balance.deposit(Double.parseDouble(deposit));
        System.out.println("You have deposited " + "$" + deposit + ".");

    }

    // EFFECTS: returns true if string is a numeric value, false otherwise
    public boolean isNumeric(String inputVal) {
        return inputVal.matches("[+-]?\\d*(\\.\\d+)?");
        // user872985
        // https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java

    }

    /// code below is from JsonSerializationDemo

    // MODIFIES: this
    // EFFECTS: loads all wish app data from file
    private void loadWishData() {
        try {

            balance = jsonReader.readBalance();
            wishList = jsonReader.readWishlist();
            purList = jsonReader.readPurchaseList();
            System.out.println("Loaded file data.");

        } catch (IOException e) {
            System.out.println("lul");
        }

    }

    // EFFECTS: saves the wish app data to file
    private void saveWishData() {
        try {
            jsonWriter.open();

            jsonWriter.writeAll(balance, wishList, purList);
            jsonWriter.close();

            System.out.println("Data has been saved.");
        } catch (IOException e) {
            System.out.println("lul ");
        }
    }



}
