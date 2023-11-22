package persistence;

import org.json.JSONObject;

/**
 * inspired by Dr. Carter's example
 * Interface that forces implementors to implement a toJson method.
 */
public interface Writeable {
    // EFFECTS: Converts the class into a JSON Object
    JSONObject toJson();
}
