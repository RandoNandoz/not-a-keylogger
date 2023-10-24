package persistence;

import org.json.JSONObject;

// inspired by Dr. Carter's example
public interface Writeable {
    // EFFECTS: Converts the class into a JSON Object
    JSONObject toJson();
}
