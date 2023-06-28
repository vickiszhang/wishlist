package ui.tools;

import model.*;
import persistance.JsonReader;
import persistance.JsonWriter;
import ui.WishAppGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// action when load button is pressed
public class LoadDataHandler implements ActionListener {

    private static final String JSON_STORE = "./data/wishAppData.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private Balance balance;
    private WishList wishList;
    private PurchaseList purList;

    private JLabel balanceNum;
    private DefaultTableModel depositTable;
    private DefaultTableModel purchaseTable;
    private DefaultTableModel wishTable;

    private WishAppGUI gui;

    // construct a load data handler, setting fields with parameters
    public LoadDataHandler(Balance balance, WishList wishList, PurchaseList purList,
                           JLabel balanceNum, DefaultTableModel depositTable,
                           DefaultTableModel purchaseTable, DefaultTableModel wishTable,
                           WishAppGUI gui) {

//        this.balance = balance;
//        this.wishList = wishList;
//        this.purList = purList;

        this.balanceNum = balanceNum;
        this.depositTable = depositTable;
        this.purchaseTable = purchaseTable;
        this.wishTable = wishTable;

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        this.gui = gui;
    }

    // MODIFIES: this
    // EFFECTS: reads the data from file, displays the graphics
    @Override
    public void actionPerformed(ActionEvent e) {

        try {

            balance = jsonReader.readBalance();
            wishList = jsonReader.readWishlist();
            purList = jsonReader.readPurchaseList();


            displayVisuals();

            gui.setBalance(balance);
            gui.setWishList(wishList);
            gui.setPurList(purList);

            JOptionPane.showMessageDialog(null,
                    "Previous data has been loaded",
                    "Data loaded from file", JOptionPane.INFORMATION_MESSAGE);


        } catch (IOException ex) {
            System.out.println("lul");
        }


    }

    // MODIFIES: this
    // EFFECTS: displays graphics for all components
    private void displayVisuals() {

        clearTables();

        balanceNum.setText("$" + String.format("%.2f", balance.getCurrentBalance()));
        displayTopLeft();
        displayBotLeft();
        displayTopRight();

    }

    private void clearTables() {
        depositTable.setRowCount(0);
        purchaseTable.setRowCount(0);
        wishTable.setRowCount(0);

    }

    // MODIFIES: this
    // EFFECTS: displays graphics for top left components (balance, deposits list)
    private void displayTopLeft() {
        for (double dep: balance.getDeposits()) {
            List<String> deposits = new ArrayList<>();
            deposits.add("+ $" + dep);
            deposits.add("DEPOSIT");
            depositTable.insertRow(0, deposits.toArray());

        }
    }

    // MODIFIES: this
    // EFFECTS: displays graphics for bot left components (purchase list)
    private void displayBotLeft() {
        for (int i = 0; i < purList.size(); i++) {

            Purchase currentPurchase = purList.getPurchase(i);

            List<String> purchases = new ArrayList<>();
            purchases.add(currentPurchase.getName());
            purchases.add("- $" + currentPurchase.getPrice());
            purchases.add(currentPurchase.getCategory().toString());
            purchaseTable.insertRow(0, purchases.toArray());

        }
    }

    // MODIFIES: this
    // EFFECTS: displays graphics for top right components (wish list)
    private void displayTopRight() {
        for (int i = 0; i < wishList.size(); i++) {

            Wish currentWish = wishList.getWish(i);

            List<String> wishes = new ArrayList<>();
            wishes.add(currentWish.getName());
            wishes.add("$" + currentWish.getPrice());
            wishes.add(currentWish.getCategory().toString());
            wishes.add("$" + currentWish.getAllocated() + " / " + "$" + currentWish.getPrice());
            wishTable.addRow(wishes.toArray());


        }
    }



}
