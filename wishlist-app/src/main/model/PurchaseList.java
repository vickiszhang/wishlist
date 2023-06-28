package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;

// represents a list of Purchases
public class PurchaseList implements Writable {

    ArrayList<Purchase> purchaseList;

    // EFFECTS: constructs an empty PurchaseList
    public PurchaseList() {
        purchaseList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds purchase to purchaseList and returns true
    public boolean addPurchase(Purchase purchase) {
        EventLog.getInstance().logEvent(new Event(purchase.getName()
                + " of price " + purchase.getPrice() + " added to purchase list"));
        return purchaseList.add(purchase);
    }

    // REQUIRES: purchaseList.size() > 0
    // MODIFIES: this
    // EFFECTS: removes purchase from purchaseList and returns
    //          true if purchase was removed, false otherwise
    public boolean removePurchase(Purchase purchase) {
        EventLog.getInstance().logEvent(new Event("purchase removed"));
        return purchaseList.remove(purchase);
    }

    // REQUIRES: purchaseList.size() > 0 && 0 <= purchaseNum < purchaseList.size
    // EFFECTS: gets the Purchase from purchaseList with index purchaseNum and returns it
    public Purchase getPurchase(int purchaseNum) {
        return purchaseList.get(purchaseNum);
    }

    // EFFECTS: returns size of wishList
    public int size() {
        return purchaseList.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("purchases", purchasesToJson());
        return json;

    }

    // EFFECTS: returns purchases in purchaseList as a JSON array
    public JSONArray purchasesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Purchase p : purchaseList) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }
}



