package ui.tools;

import model.Balance;
import ui.exceptions.EmptyInputException;
import ui.exceptions.NotNumericException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// action when deposit button is pressed
public class DepositHandler implements ActionListener {

    private Balance balance;
    private JLabel balanceNum;
    private DefaultTableModel depositTable;

    // EFFECTS: constructs a deposit handler, setting fields with parameters
    public DepositHandler(DefaultTableModel depositTable, JLabel balanceNum, Balance balance) {
        this.depositTable = depositTable;
        this.balanceNum = balanceNum;
        this.balance = balance;
    }

    // EFFECTS: displays pop up window prompting user input
    @Override
    public void actionPerformed(ActionEvent e) {

        JTextField depositInput = new JTextField();

        Object[] depositField = {
                "Enter amount to deposit", depositInput
        };

        int selection = JOptionPane.showConfirmDialog(null,
                depositField,
                "Deposit amount",
                JOptionPane.OK_CANCEL_OPTION);

        if (selection == JOptionPane.OK_OPTION) {

            try {
                handleDeposit(depositInput);
            } catch (EmptyInputException ex) {
                JOptionPane.showMessageDialog(null, "Please input a value",
                        "Empty input", JOptionPane.ERROR_MESSAGE);
            } catch (NotNumericException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a numeric value",
                        "Invalid number", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds user input to table, deducts the balance
    public void handleDeposit(JTextField depositInput) throws EmptyInputException, NotNumericException {

        if (depositInput.getText().equals("")) {
            throw new EmptyInputException();
        } else if (!isNumeric(depositInput.getText())) {
            throw new NotNumericException();
        } else {

            double depositNum = Double.parseDouble(depositInput.getText());
            List<String> deposits = new ArrayList<>();
            deposits.add("+ $" + depositNum);
            deposits.add("DEPOSIT");
            depositTable.insertRow(0, deposits.toArray());

            depositNum = Math.round(depositNum * 100);
            depositNum = depositNum / 100;

            balance.deposit(depositNum);
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
