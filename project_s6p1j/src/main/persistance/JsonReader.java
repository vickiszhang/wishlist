package persistance;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// Represents a reader that reads all wish app data from JSON data stored in file
// most code is from CPSC210 JsonSerializationDemo
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String currentSource) {
        source = currentSource;
    }

    // EFFECTS: reads elements (contains all elements of app) from file and returns it;
    // throws IOException if an error occurs reading data from file
    public JSONObject readFrom() throws IOException {
        String jsonData = readFile(source);
        return new JSONObject(jsonData);
    }

    // EFFECTS: reads balance from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Balance readBalance() throws IOException {
        EventLog.getInstance().logEvent(new Event("balance loaded from file"));
        return parseBalance(readFrom());

    }

    // EFFECTS: reads wishlist from file and returns it;
    // throws IOException if an error occurs reading data from file
    public WishList readWishlist() throws IOException {
        EventLog.getInstance().logEvent(new Event("wishes loaded from file"));
        return parseWishlist(readFrom());
    }

    // EFFECTS: reads purchaselist from file and returns it;
    // throws IOException if an error occurs reading data from file
    public PurchaseList readPurchaseList() throws IOException {
        EventLog.getInstance().logEvent(new Event("purchases loaded from file"));
        return parsePurchaseList(readFrom());
    }

    // EFFECTS: reads source file as string and returns it
    public String readFile(String course) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses balance from JSON object and returns it
    public Balance parseBalance(JSONObject jsonObject) {
        JSONArray elementsArray = jsonObject.getJSONArray("elements");
        JSONObject firstElement = elementsArray.getJSONObject(0);
        double prevBalance = firstElement.getDouble("balance");
        JSONArray prevDeposits = firstElement.getJSONArray("deposits");

        Balance b = new Balance();
        //b.deposit(prevBalance);
        b.setCurrentBalance(prevBalance);
        ArrayList<Double> deposits = new ArrayList<>();

        for (int i = 0; i < prevDeposits.length(); i++) {
            deposits.add(prevDeposits.getDouble(i));
        }
        b.setDeposits(deposits);
        return b;
    }

    // MODIFIES: wl
    // EFFECTS: parses wishlist from JSON object and returns it
    public WishList parseWishlist(JSONObject jsonObject) {

        JSONArray elementsArray = jsonObject.getJSONArray("elements");
        JSONObject secondElement = elementsArray.getJSONObject(1);
        JSONArray wishesArray = secondElement.getJSONArray("wishes");

        WishList wl = new WishList();
        for (Object json : wishesArray) {
            JSONObject nextWish = (JSONObject) json;
            Wish w = parseWish(nextWish);
            wl.addWish(w);
        }
        return wl;
    }

    // MODIFIES: wish
    // EFFECTS: parses wish from JSON object
    public Wish parseWish(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double price = jsonObject.getDouble("price");
        Category category = Category.valueOf(jsonObject.getString("category"));
        boolean isBought = jsonObject.getBoolean("status");
        double allocated = jsonObject.getDouble("allocated");
        Wish wish = new Wish(name, price, category);
        if (isBought) {
            wish.setBought();
        }
        wish.allocate(allocated);

        return wish;
    }

    // MODIFIES: pl
    // EFFECTS: parses purchaselist from JSON object and returns it
    public PurchaseList parsePurchaseList(JSONObject jsonObject) {

        JSONArray elementsArray = jsonObject.getJSONArray("elements");
        JSONObject thirdElement = elementsArray.getJSONObject(2);
        JSONArray purchasesArray = thirdElement.getJSONArray("purchases");

        PurchaseList pl = new PurchaseList();
        for (Object json : purchasesArray) {
            JSONObject nextPurchase = (JSONObject) json;
            Purchase p = parsePurchase(nextPurchase);
            pl.addPurchase(p);
        }
        return pl;
    }

    // EFFECTS: parses purchase from JSON object
    public Purchase parsePurchase(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        double price = jsonObject.getDouble("price");
        Category category = Category.valueOf(jsonObject.getString("category"));
        return new Purchase(name, price, category);
    }

}
