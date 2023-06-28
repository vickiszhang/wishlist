package ui.tools;

import model.Balance;
import model.Category;
import model.Purchase;
import model.PurchaseList;
import ui.exceptions.EmptyInputException;
import ui.exceptions.NotNumericException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// action when add (purchase) button is clicked
public class AddPurchaseHandler implements ActionListener {

    private String purchaseName;
    private double purchasePrice;
    private Category purchaseCategory;

    private PurchaseList purList;
    private DefaultTableModel purchaseTable;
    private JLabel balanceNum;
    private Balance balance;


    // EFFECTS: construct a purchase handler, setting fields with parameters
    public AddPurchaseHandler(DefaultTableModel purchaseTable, JLabel balanceNum,
                              Balance balance, PurchaseList purList) {
        this.purchaseTable = purchaseTable;
        this.purList = purList;
        this.balanceNum = balanceNum;
        this.balance = balance;

    }

    // EFFECTS: displays pop up window prompting user input
    @Override
    public void actionPerformed(ActionEvent e) {

        JTextField nameInput = new JTextField();
        JTextField priceInput = new JTextField();
        JComboBox categoryInput = new JComboBox(Category.values());

        Object[] purchaseFields = {
                "Purchase name", nameInput,
                "Price", priceInput,
                "Category", categoryInput
        };

        int selection = JOptionPane.showConfirmDialog(null,
                purchaseFields,
                "Enter recent purchase",
                JOptionPane.OK_CANCEL_OPTION);

        if (selection == JOptionPane.OK_OPTION) {
            try {
                inputPurchaseToTable(nameInput, priceInput, categoryInput);
            } catch (EmptyInputException ex) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields",
                        "Empty fields", JOptionPane.ERROR_MESSAGE);
            } catch (NotNumericException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a numeric value",
                        "Invalid number", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    // MODIFIES: this
    // EFFECTS: adds user input to table, adds the purchase to purList, deducts and displays balance
    public void inputPurchaseToTable(JTextField nameInput, JTextField priceInput, JComboBox categoryInput)
            throws EmptyInputException, NotNumericException {

        if (nameInput.getText().equals("")) {
            throw new EmptyInputException();
        } else if (!isNumeric(priceInput.getText())) {
            throw new NotNumericException();
        } else {

            purchaseName = nameInput.getText();
            purchaseName = purchaseName.substring(0, 1).toUpperCase() + purchaseName.substring(1);

            double priceInputRounded = Double.parseDouble(priceInput.getText());
            priceInputRounded = Math.round(priceInputRounded * 100);
            priceInputRounded = priceInputRounded / 100;
            purchasePrice = priceInputRounded;

            purchaseCategory = (Category) categoryInput.getSelectedItem();

            Purchase purchase = new Purchase(purchaseName, purchasePrice, purchaseCategory);
            purList.addPurchase(purchase);

            List<String> purchaseInputs = new ArrayList<>();
            purchaseInputs.add(purchaseName);
            purchaseInputs.add("- $" + purchasePrice);
            purchaseInputs.add(purchaseCategory.toString());
            purchaseTable.insertRow(0, purchaseInputs.toArray());

            balance.deduct(purchasePrice);
            balanceNum.setText("$" + String.format("%.2f", balance.getCurrentBalance()));

        }
    }

    // EFFECTS: returns true if string is a numeric value, false otherwise
    public boolean isNumeric(String inputVal) {
        return inputVal.matches("[+]?\\d*(\\.\\d+)?");
        // user872985
        // https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java

    }
}
