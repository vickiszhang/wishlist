package ui.tools;

import model.Balance;
import model.PurchaseList;
import model.WishList;
import persistance.JsonReader;
import persistance.JsonWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

// action when save button is pressed
public class SaveDataHandler implements ActionListener {

    private static final String JSON_STORE = "./data/wishAppData.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private Balance balance;
    private WishList wishList;
    private PurchaseList purList;

    // EFFECTS: constructs a save data handler, setting fields with parameters
    public SaveDataHandler(Balance balance, WishList wishList, PurchaseList purList) {
        this.balance = balance;
        this.wishList = wishList;
        this.purList = purList;

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: writes data to file
    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            jsonWriter.open();

            jsonWriter.writeAll(balance, wishList, purList);
            jsonWriter.close();

            JOptionPane.showMessageDialog(null,
                    "Your data has been saved",
                    "Data saved to file", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            //
        }


    }
}
