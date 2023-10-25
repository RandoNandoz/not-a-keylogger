package persistence;

import model.InputTime;
import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// From Dr. Carter's JSON Example https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private final String pathToJson;

    // EFFECTS: creates new JsonReader instance to write to given path
    public JsonReader(String pathToJson) {
        this.pathToJson = pathToJson;
    }

    // REQUIRES: kbJsonObject actually be a representation of KeyBoardInputTime
    // EFFECTS: reads a json representation of KeyboardInputTime
    private KeyboardInputTime readKbInput(JSONObject kbJsonObject) {
        return KeyboardInputTime.fromJson(kbJsonObject);
    }

    // REQUIRES: mouseJsonObject actually be a representation of MouseInputTime
    // EFFECTS: reads a json representation of MouseInputTime
    private MouseInputTime readMouseInput(JSONObject mouseJsonObject) {
        return MouseInputTime.fromJson(mouseJsonObject);
    }

    // REQUIRES: the json we're reading is actually a representation of a time series of kbinputtimes
    // EFFECTS: reads given json path as a TimeSeries<KeyboardInputTime>
    private TimeSeries<KeyboardInputTime> readKeyboardTimeSeries() throws IOException {
        JSONObject object = new JSONObject(this.readFile(this.pathToJson));
        ArrayList<KeyboardInputTime> inputs = new ArrayList<>();
        JSONArray inputsAsJson = object.getJSONArray("inputs");
        for (int i = 0; i < inputsAsJson.length(); i++) {
            inputs.add(this.readKbInput(inputsAsJson.getJSONObject(i)));
        }
        long startTime = object.getLong("startTime");
        return new TimeSeries<>(inputs, startTime);
    }

    // REQUIRES: the json were reading is actually a representation of TimeSeries<minputtimes>.
    // EFFECTS: reads json as MouseTimeSeries
    private TimeSeries<MouseInputTime> readMouseTimeSeries() throws IOException {
        JSONObject object = new JSONObject(this.readFile(this.pathToJson));
        ArrayList<MouseInputTime> inputs = new ArrayList<>();
        JSONArray inputsAsJson = object.getJSONArray("inputs");
        for (int i = 0; i < inputsAsJson.length(); i++) {
            inputs.add(this.readMouseInput(inputsAsJson.getJSONObject(i)));
        }
        long startTime = object.getLong("startTime");
        return new TimeSeries<>(inputs, startTime);
    }

    // EFFECTS: returns timeseries at JSON
    // TODO: make java shut up about unchecked casts?
    public TimeSeries<? extends InputTime> readTimeSeries(Class<? extends InputTime> c) throws IOException {
//        assert (InputTime.class.isAssignableFrom(c));
//        assert (KeyboardInputTime.class == c || MouseInputTime.class == c);
        if (KeyboardInputTime.class == c) {
            return this.readKeyboardTimeSeries();
        } else {
            return this.readMouseTimeSeries();
        }
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }
}
