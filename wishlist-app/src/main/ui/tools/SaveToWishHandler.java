package ui.tools;

import model.Balance;
import model.Wish;
import model.WishList;
import ui.exceptions.EmptyInputException;
import ui.exceptions.NotNumericException;
import ui.exceptions.SufficientAmountException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

// action when save amount for item button is pressed
public class SaveToWishHandler implements ActionListener {

    private static final int ARRAY_MAX = 100;
    private DefaultTableModel wishTable;
    private WishList wishList;
    private Balance balance;
    private JLabel balanceNum;

    // construct a save to wish handler, setting fields with parameters
    public SaveToWishHandler(DefaultTableModel wishTable, WishList wishList,
                             Balance balance, JLabel balanceNum) {
        this.wishTable = wishTable;
        this.wishList = wishList;
        this.balance = balance;
        this.balanceNum = balanceNum;

    }

    // EFFECTS: displays pop up prompting user selection
    @Override
    public void actionPerformed(ActionEvent e) {

        if (wishTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "You have no wishes",
                    "No wishes", JOptionPane.ERROR_MESSAGE);
        } else {
            ButtonGroup optionsGroup = new ButtonGroup();

            Object[] wishOptions = new Object[ARRAY_MAX]; //max size

            ArrayList<Integer> iteration = new ArrayList<>();
            iteration.add(0);
            iteration.add(3);

            displayValidWishes(optionsGroup, wishOptions, iteration);

            int selection = JOptionPane.showConfirmDialog(null, wishOptions,
                    "Select a wish to save for", JOptionPane.OK_CANCEL_OPTION);

            if (selection == JOptionPane.OK_OPTION) {
                try {
                    handleSelection((Integer) getSelectionNumber(optionsGroup));
                } catch (SufficientAmountException ex) {
                    JOptionPane.showMessageDialog(null,
                            "You already have sufficient amount for this item",
                            "Sufficient amount", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // EFFECTS: displays all wishes that are available to buy, ie: have sufficient allocated
    private void displayValidWishes(ButtonGroup optionsGroup, Object[] wishOptions, ArrayList<Integer> iteration) {

        int col = 0;
        int row = 0;
        String option = "";

        for (int i = 0; i < wishTable.getRowCount(); i++) {
            for (int j: iteration) {
                if (j == 0) {
                    String name = (String) wishTable.getValueAt(row, col); //name
                    option = "Name: " + name;

                    col += 3;
                }
                if (j == 3) {
                    String saved = (String) wishTable.getValueAt(row, col); //allocated
                    option = option + " | Amount Saved: " + saved;
                    row++;
                    col -= 3;
                }
            }
            JRadioButton optionButton = new JRadioButton(option);
            optionButton.setSelected(true);
            optionsGroup.add(optionButton);
            wishOptions[i + 1] = optionButton;

        }
    }

    // EFFECTS: returns the number of the option the user selected
    private Comparable<Integer> getSelectionNumber(ButtonGroup optionsGroup) {

        int num = 0;
        for (Enumeration<AbstractButton> buttons = optionsGroup.getElements(); buttons.hasMoreElements();) {
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

    // EFFECTS: prompts user input for the wish that they selected
    private void handleSelection(int optionNum) throws SufficientAmountException {

        if (wishList.getWish(optionNum).getAllocated()
                == wishList.getWish(optionNum).getPrice()) {
            throw new SufficientAmountException();
        } else {

            JTextField saveInput = new JTextField();
            Object[] saveField = {
                    "Enter amount to save", saveInput
            };

            int selection = JOptionPane.showConfirmDialog(null,
                    saveField, "Save amount",
                    JOptionPane.OK_CANCEL_OPTION);

            if (selection == JOptionPane.OK_OPTION) {

                try {
                    handleInput(saveInput, optionNum);
                } catch (EmptyInputException ex) {
                    JOptionPane.showMessageDialog(null, "Please input a value",
                            "Empty input", JOptionPane.ERROR_MESSAGE);
                } catch (NotNumericException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a numeric value",
                            "Invalid number", JOptionPane.ERROR_MESSAGE);

                }
            }
        }

    }

    // EFFECTS: handles the user input
    private void handleInput(JTextField saveInput, int optionNum)
            throws EmptyInputException, NotNumericException {

        if (saveInput.getText().equals("")) {
            throw new EmptyInputException();
        } else if (!isNumeric(saveInput.getText())) {
            throw new NotNumericException();
        } else {
            handleAmountSaved(saveInput, optionNum);

        }
    }

    // MODIFIES: this
    // EFFECTS: saves the user amount to the wish according to balance and/or amount already saved
    private void handleAmountSaved(JTextField saveInput, int optionNum) {
        Wish selectedWish = wishList.getWish(optionNum);
        double saveNum = Double.parseDouble(saveInput.getText());

        if (saveNum > balance.getCurrentBalance()) {
            JOptionPane.showMessageDialog(null,
                    "You do not have sufficient funds to save $" + saveNum,
                    "Insufficient balance", JOptionPane.ERROR_MESSAGE);


        } else if (savedSufficient(saveNum, selectedWish)) {
            sufficientStatusPath(saveNum, selectedWish, optionNum);

        } else {

            selectedWish.allocate(saveNum);
            balance.deduct(saveNum);
            balanceNum.setText("$" + String.format("%.2f", balance.getCurrentBalance()));

            JOptionPane.showMessageDialog(null,
                    "$" + saveNum + " has been saved for " + selectedWish.getName()
                            + "\nAmount has been deducted from balance",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            String newCellVal = "$" + selectedWish.getAllocated() + " / " + "$" + selectedWish.getPrice();
            wishTable.setValueAt(newCellVal, optionNum, 3);

        }
    }

    // EFFECTS: returns true if input + amount already saved is greater than price
    private boolean savedSufficient(double saveNum, Wish selectedWish) {

        return (saveNum + selectedWish.getAllocated() >= selectedWish.getPrice());
    }

    // MODIFIES: this
    // EFFECTS: deducts balance while dealing with oversaving
    private void sufficientStatusPath(double saveNum, Wish selectedWish, int optionNum) {

        String dialog = "You are able to purchase this Item!";

        if (saveNum + selectedWish.getAllocated() > selectedWish.getPrice()) {
            dialog += "\nYou have funded an extra of $"
                    + (selectedWish.getAllocated() + saveNum - selectedWish.getPrice());
        }
        dialog += "\n$" + selectedWish.getPrice() + " is available for this item";

        balance.deduct(selectedWish.getPrice() - selectedWish.getAllocated());
        balanceNum.setText("$" + String.format("%.2f", balance.getCurrentBalance()));
        selectedWish.allocate(saveNum); //method deals with oversaving

        JOptionPane.showMessageDialog(null,
                dialog, "Success", JOptionPane.INFORMATION_MESSAGE);

        String newCellVal = "$" + selectedWish.getAllocated() + " / " + "$" + selectedWish.getPrice();
        wishTable.setValueAt(newCellVal, optionNum, 3);
    }

    // EFFECTS: returns true if string is a numeric value, false otherwise
    public boolean isNumeric(String inputVal) {
        return inputVal.matches("[+]?\\d*(\\.\\d+)?");
        // user872985
        // https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java

    }
}
