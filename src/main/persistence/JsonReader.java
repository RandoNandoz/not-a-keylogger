package persistence;

import model.InputRecording;
import model.KeyboardInputTime;
import model.MouseInputTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * From Dr. Carter's JSON Example <a href="https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo">here</a>
 * Class to read either keyboard/mouse input times from JSON
 */
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
    // EFFECTS: reads given json path as a InputRecording<KeyboardInputTime>
    public InputRecording<KeyboardInputTime> readKeyboardInputRecording() throws IOException {
        JSONObject object = new JSONObject(this.readFile(this.pathToJson));
        ArrayList<KeyboardInputTime> inputs = new ArrayList<>();
        JSONArray inputsAsJson = object.getJSONArray("inputs");
        for (int i = 0; i < inputsAsJson.length(); i++) {
            inputs.add(this.readKbInput(inputsAsJson.getJSONObject(i)));
        }
        long startTime = object.getLong("startTime");
        return new InputRecording<>(inputs, startTime);
    }

    // REQUIRES: the json were reading is actually a representation of InputRecording<minputtimes>.
    // EFFECTS: reads json as MouseTimeSeries
    public InputRecording<MouseInputTime> readMouseInputRecording() throws IOException {
        JSONObject object = new JSONObject(this.readFile(this.pathToJson));
        ArrayList<MouseInputTime> inputs = new ArrayList<>();
        JSONArray inputsAsJson = object.getJSONArray("inputs");
        for (int i = 0; i < inputsAsJson.length(); i++) {
            inputs.add(this.readMouseInput(inputsAsJson.getJSONObject(i)));
        }
        long startTime = object.getLong("startTime");
        return new InputRecording<>(inputs, startTime);
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
