package model;

import org.json.JSONObject;
import persistance.Writable;

// represents a purchase with a name and a price
public class Purchase implements Writable {

    private String name;
    private double price;
    private Category category;

    // EFFECTS: constructs a Purchase with a name given a string, and a price given a double
    public Purchase(String purchaseName, double purchasePrice, Category purchaseCategory) {
        name = purchaseName;
        price = purchasePrice;
        category = purchaseCategory;
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns price
    public double getPrice() {
        return price;
    }

    //EFFECTS: returns category
    public Category getCategory() {
        return category;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("price", price);
        json.put("category", category);
        return json;
    }

}
