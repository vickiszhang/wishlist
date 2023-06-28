package ui.tools;

import model.Category;
import model.Purchase;
import model.Wish;
import model.WishList;
import ui.exceptions.EmptyInputException;
import ui.exceptions.NotNumericException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// action when add (wish) button is pressed
public class AddWishHandler implements ActionListener {

    private String wishName;
    private double wishPrice;
    private Category wishCategory;

    private DefaultTableModel wishTable;
    private WishList wishList;

    // EFFECTS: construct an add wish handler, setting fields with parameters
    public AddWishHandler(DefaultTableModel wishTable, WishList wishList) {
        this.wishTable = wishTable;
        this.wishList = wishList;
    }

    // EFFECTS: displays pop up window prompting user input
    @Override
    public void actionPerformed(ActionEvent e) {

        JTextField nameInput = new JTextField();
        JTextField priceInput = new JTextField();
        JComboBox categoryInput = new JComboBox(Category.values());

        Object[] wishFields = {
                "Wish name", nameInput,
                "Price", priceInput,
                "Category", categoryInput
        };
        int selection = JOptionPane.showConfirmDialog(null,
                wishFields,
                "Enter a wish",
                JOptionPane.OK_CANCEL_OPTION);

        if (selection == JOptionPane.OK_OPTION) {
            try {
                inputWishToTable(nameInput, priceInput, categoryInput);
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
    // EFFECTS: adds user input to table, adds the wish to wishList
    private void inputWishToTable(JTextField nameInput, JTextField priceInput, JComboBox categoryInput)
            throws EmptyInputException, NotNumericException {
        if (nameInput.getText().equals("")) {
            throw new EmptyInputException();
        } else if (!isNumeric(priceInput.getText())) {
            throw new NotNumericException();
        } else {

            wishName = nameInput.getText();
            wishName = wishName.substring(0, 1).toUpperCase() + wishName.substring(1);

            double priceInputRounded = Double.parseDouble(priceInput.getText());
            priceInputRounded = Math.round(priceInputRounded * 100);
            priceInputRounded = priceInputRounded / 100;
            wishPrice = priceInputRounded;

            wishCategory = (Category) categoryInput.getSelectedItem();

            Wish wish = new Wish(wishName, wishPrice, wishCategory);
            wishList.addWish(wish);

            List<String> wishInputs = new ArrayList<>();
            wishInputs.add(wishName);
            wishInputs.add("$" + wishPrice);
            wishInputs.add(wishCategory.toString());
            wishInputs.add("$" + wish.getAllocated() + " / " + "$" + wish.getPrice());
            wishTable.addRow(wishInputs.toArray());

        }
    }

    // EFFECTS: returns true if string is a numeric value, false otherwise
    public boolean isNumeric(String inputVal) {
        return inputVal.matches("[+]?\\d*(\\.\\d+)?");
        // user872985
        // https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java

    }
}
