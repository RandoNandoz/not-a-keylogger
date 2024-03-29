package persistence;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * inspired by dr. carter's json serialization demo <a href="https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo">here</a>
 * Writes the writeable class param to disk
 * @param <T> Class that must extend writeable class.
 */
public class JsonWriter<T extends Writeable> {
    // 4 spaces/tab in JSON
    private static final int TAB_SIZE = 4;
    private final String filePath;
    private PrintWriter printWriter;

    // EFFECTS: creates a new json writer to write to this path, path can be rel or absolute
    public JsonWriter(String filePath) {
        this.filePath = filePath;
    }

    // MODIFIES: this
    // EFFECTS: opens file for writing
    public void open() throws IOException {
        File file = new File(this.filePath);
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        this.printWriter = new PrintWriter(file, StandardCharsets.UTF_8);
    }

    // REQUIRES: this.open() has been called once.
    // MODIFIES: this
    // EFFECTS: writes to file
    public void write(T object) {
        JSONObject objectAsJson = object.toJson();
        this.printWriter.write(objectAsJson.toString(TAB_SIZE));
    }

    // REQUIRES: this.open() has been called
    // MODIFIES: this
    // EFFECTS: closes file handle
    public void close() {
        this.printWriter.close();
    }
}
