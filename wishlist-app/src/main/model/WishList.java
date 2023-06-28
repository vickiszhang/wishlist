package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;

// represents a list of Wishes
public class WishList implements Writable {

    ArrayList<Wish> wishList;

    // EFFECTS: construct an empty WishList
    public WishList() {
        wishList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds wish to wishList and returns true
    public boolean addWish(Wish wish) {

        EventLog.getInstance().logEvent(new Event(wish.getName()
                + " of price " + wish.getPrice() + " added to wish list"));
        return wishList.add(wish);
    }

    // REQUIRES: wishList.size() > 0
    // MODIFIES: this
    // EFFECTS: removes wish from wishList and returns
    //          true if wish was removed, false otherwise
    public boolean removeWish(Wish wish) {

        EventLog.getInstance().logEvent(new Event(wish.getName() + " removed from wish list"));
        return wishList.remove(wish);
    }

    // REQUIRES: wishList.size() > 0
    // EFFECTS: gets the Wish from wishList with index wishNum and returns it
    public Wish getWish(int wishNum) {
        return wishList.get(wishNum);
    }

    // EFFECTS: returns size of wishList
    public int size() {
        return wishList.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("wishes", wishesToJson());
        return json;
    }

    // EFFECTS: returns wishes in wishList as a JSON array
    public JSONArray wishesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Wish w : wishList) {
            jsonArray.put(w.toJson());
        }
        return jsonArray;
    }

}

