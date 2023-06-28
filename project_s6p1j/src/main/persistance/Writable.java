package persistance;

import org.json.JSONObject;

//code is from JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
