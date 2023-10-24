package persistence;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonWriter<T extends Writeable> {
    // 4 spaces/tab in JSON
    private static final int TAB_SIZE = 4;
    private final String filePath;
    private PrintWriter printWriter;

    public JsonWriter(String filePath) {
        this.filePath = filePath;
    }

    public void open() throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        printWriter = new PrintWriter(file);
    }

    public void write(T object) {
        JSONObject objectAsJson = object.toJson();
        printWriter.write(objectAsJson.toString(TAB_SIZE));
    }

    public void close() {
        printWriter.close();
    }
}
