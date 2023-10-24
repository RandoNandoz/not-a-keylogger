package persistence;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

// inspired by dr. carter's json serialization demo https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
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
        File file = new File(filePath);
        file.createNewFile();
        printWriter = new PrintWriter(file);
    }

    // REQUIRES: this.open() has been called once.
    // MODIFIES: this
    // EFFECTS: writes to file
    public void write(T object) {
        JSONObject objectAsJson = object.toJson();
        printWriter.write(objectAsJson.toString(TAB_SIZE));
    }

    // REQUIRES: this.open() has been called
    // MODIFIES: this
    // EFFECTS: closes file handle
    public void close() {
        printWriter.close();
    }
}
