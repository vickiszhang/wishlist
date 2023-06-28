package ui.tools;

import model.Purchase;
import model.PurchaseList;
import model.Wish;
import model.WishList;
import ui.exceptions.NoWishesException;
import ui.exceptions.SufficientAmountException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

// action when purchase (wish) button is pressed
public class PurchaseWishHandler implements ActionListener {

    private static final int ARRAY_MAX = 100;
    private DefaultTableModel wishTable;
    private WishList wishList;
    private DefaultTableModel purchaseTable;
    private PurchaseList purList;

    // EFFECTS: constructs a wish handler, setting fields with parameters
    public PurchaseWishHandler(DefaultTableModel wishTable, WishList wishList,
                               DefaultTableModel purchaseTable, PurchaseList purList) {
        this.wishTable = wishTable;
        this.wishList = wishList;
        this.purchaseTable = purchaseTable;
        this.purList = purList;
    }

    // EFFECTS: displays pop up window prompting user input
    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            displayBuyOptions();
        } catch (NoWishesException ex) {
            JOptionPane.showMessageDialog(null,
                    "You have no wishes or have not saved enough for any wishes",
                    "No wishes to buy", JOptionPane.ERROR_MESSAGE);
        }

    }

    // EFFECTS: displays all wishes available to purchase
    private void displayBuyOptions() throws NoWishesException {
        if (wishList.size() == 0 || noneSufficient()) {
            throw new NoWishesException();
        } else {

            String option;
            ButtonGroup buyOptionsGroup = new ButtonGroup();
            Object[] buyOptions = new Object[ARRAY_MAX];

            for (int i = 0; i < wishList.size(); i++) {
                if (wishList.getWish(i).getAllocated() == wishList.getWish(i).getPrice()) {

                    option = (String) wishTable.getValueAt(i, 0); //name

                    JRadioButton buyOptionButton = new JRadioButton(option);
                    buyOptionButton.setSelected(true);
                    buyOptionsGroup.add(buyOptionButton);
                    buyOptions[i + 1] = buyOptionButton;

                }
            }

            int selection = JOptionPane.showConfirmDialog(null,
                    buyOptions,
                    "Select a wish to buy",
                    JOptionPane.OK_CANCEL_OPTION);

            if (selection == JOptionPane.OK_OPTION) {

                transferWishToPurchase((Integer) getSelectionNumber(buyOptionsGroup));
            }
        }
    }

    // EFFECTS: returns true if no wishes have enough saved
    private boolean noneSufficient() {

        boolean noWishAvail = true;

        for (int i = 0; i < wishList.size(); i++) {
            if (wishList.getWish(i).getAllocated() == wishList.getWish(i).getPrice()) {
                noWishAvail = false;
            }
        }
        return noWishAvail;
    }

    // EFFECTS: returns the number of the option the user selected
    private Comparable<Integer> getSelectionNumber(ButtonGroup buyOptionsGroup) {

        int num = 0;
        for (Enumeration<AbstractButton> buttons = buyOptionsGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return num;
            } else {
                num++;
            }
        }
        return null;
        // cite: Rendicahya
        // https://stackoverflow.com/questions/201287/how-do-i-get-which-jradiobutton-is-selected-from-a-buttongroup

    }

    // MODIFIES: this
    // EFFECTS: removes selected wish from table and wishlist, inputs it into the purchase list
    private void transferWishToPurchase(int buyOptionNum) {

        wishTable.removeRow(buyOptionNum);
        Wish wishToRemove = wishList.getWish(buyOptionNum);
        wishToRemove.setBought();
        wishList.removeWish(wishToRemove);

        Purchase newPurchase = new Purchase(wishToRemove.getName(),
                wishToRemove.getPrice(), wishToRemove.getCategory());

        List<String> wishToPurchaseInput = new ArrayList<>();
        wishToPurchaseInput.add(newPurchase.getName());
        wishToPurchaseInput.add("- $" + newPurchase.getPrice());
        wishToPurchaseInput.add(newPurchase.getCategory().toString());
        purchaseTable.insertRow(0, wishToPurchaseInput.toArray());

        purList.addPurchase(newPurchase);

        JOptionPane.showMessageDialog(null,
                "You have successfully purchased " + newPurchase.getName(),
                "Purchase successful", JOptionPane.INFORMATION_MESSAGE);


    }
}
