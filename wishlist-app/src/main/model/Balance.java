package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;

// a Balance represents an amount of money in the current Account
public class Balance implements Writable {

    private double currentBalance;
    private ArrayList<Double> deposits;

    // EFFECTS: constructs a balance with 0 dollars
    public Balance() {
        currentBalance = 0;
        deposits = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: amount is added to currentBalance is returned
    public double deposit(double amount) {


        currentBalance += amount;
        deposits.add(amount);

        EventLog.getInstance().logEvent(new Event(amount + " deposited to account"));

        return currentBalance;


    }

    public void setDeposits(ArrayList<Double> deposits) {
        this.deposits = deposits;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: returns true if amount deducted is smaller or equal
    //          to the currentBalance, false otherwise
    public boolean deduct(double amount) {

        if (amount <= currentBalance) {
            currentBalance -= amount;
            EventLog.getInstance().logEvent(new Event(amount + " deducted from account"));
            return true;

        } else {
            currentBalance -= amount;
            return false;
        }
    }

    // EFFECTS: returns currentBalance
    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    // EFFECTS: returns the list of deposits
    public ArrayList<Double> getDeposits() {
        return deposits;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("balance", currentBalance);
        json.put("deposits", depositsToArray());
        return json;
    }

    private JSONArray depositsToArray() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < deposits.size(); i++) {
            jsonArray.put(deposits.get(i));
        }
        return jsonArray;
    }


}
