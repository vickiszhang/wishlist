package persistance;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes JSON representation of workroom to file
// most code is from CPSC210 JsonSerializationDemo
public class JsonWriter {

    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of balance, wishlist, and purchaselist to file
    public void writeAll(Balance b, WishList wl, PurchaseList pl) {
        JSONObject json = new JSONObject();

        JSONObject jsonBalance = b.toJson();
        JSONObject jsonWishlist = wl.toJson();
        JSONObject jsonPurchaseList = pl.toJson();

        JSONArray jsonCollection = new JSONArray();
        jsonCollection.put(jsonBalance);
        jsonCollection.put(jsonWishlist);
        jsonCollection.put(jsonPurchaseList);

        json.put("elements", jsonCollection);

        saveToFile(json.toString(TAB));
        EventLog.getInstance().logEvent(new Event("data saved to file"));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    public void saveToFile(String json) {
        writer.print(json);
    }

}
