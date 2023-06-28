package model;

import org.json.JSONObject;
import persistance.Writable;

// represents a wish with a name and price
public class Wish implements Writable {

    private String name;
    private double price;
    private Category category;
    private boolean isBought;
    private double allocated;

    // EFFECTS: constructs a wish with a name given a string, and price given a double.
    //          sets isBought to false and allocated to 0
    public Wish(String wishName, double wishPrice, Category wishCategory) {
        name = wishName;
        price = wishPrice;
        category = wishCategory;
        isBought = false;
        allocated = 0;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: adds amount to allocated, sets allocated equal to
    //          price if allocated + amount exceeds price, returns allocated
    public double allocate(double amount) {


        if (allocated + amount >= price) {
            EventLog.getInstance().logEvent(new Event(price - allocated + " allocated to "
                    + getName()));
            return allocated = price;
        } else {
            EventLog.getInstance().logEvent(new Event(amount + " allocated to "
                    + getName()));

            return allocated += amount;
        }
    }

    // MODIFIES: this
    // EFFECTS: isBought set to true
    public void setBought() {
        isBought = true;
        EventLog.getInstance().logEvent(new Event(getName() + " has been purchased off wish list"));
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns price
    public double getPrice() {
        return price;
    }

    // EFFECTS: returns category
    public Category getCategory() {
        return category;
    }

    // EFFECTS: returns isBought
    public boolean isBought() {
        return isBought;
    }

    // EFFECTS: returns allocated
    public double getAllocated() {
        return allocated;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("price", price);
        json.put("category", category);
        json.put("status", isBought);
        json.put("allocated", allocated);
        return json;
    }

}
