package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;

class JsonWriterTest {
    JsonWriter<MouseInputTime> mouseUnitWriter;
    JsonWriter<KeyboardInputTime> keyboardUnitWriter;
    JsonWriter<TimeSeries<KeyboardInputTime>> kbWriter;
    JsonWriter<TimeSeries<MouseInputTime>> mouseWriter;

    @BeforeEach
    void setUp() {
        mouseUnitWriter = new JsonWriter<>("./data/testMouseWrite.json");
        keyboardUnitWriter = new JsonWriter<>("./data/testKbWrite.json");

        kbWriter = new JsonWriter<>("./data/testWriteKbInputs.kbinput");
        mouseWriter = new JsonWriter<>("./data/testWriteMouseInputs.minput");
    }

    @Test
    void testWriteMouseInput() throws IOException {
        MouseInputTime mouseInputTime = new MouseInputTime(new NativeMouseEvent(1,2,3,4,5,6), 20);
        try {
            mouseUnitWriter.open();
        } catch (FileNotFoundException e) {
            fail("File should be found");
        }

        mouseUnitWriter.write(mouseInputTime);

        mouseUnitWriter.close();

        MouseInputTime fromJson = MouseInputTime.fromJson(new JSONObject(readFile("./data/testMouseWrite.json")));
        assertEquals(mouseInputTime, fromJson);
    }

    @Test
    void testWriteKeyboardInput() throws IOException {
        KeyboardInputTime kbInputTime = new KeyboardInputTime(1, KeyPress.UP, 20);
        try {
            keyboardUnitWriter.open();
        } catch (IOException e) {
            fail("file should be found");
        }

        keyboardUnitWriter.write(kbInputTime);
        keyboardUnitWriter.close();

        KeyboardInputTime fromJson = KeyboardInputTime.fromJson(new JSONObject(readFile("./data/testKbWrite.json")));
        assertEquals(kbInputTime, fromJson);
    }

    @Test
    void testWriteKeyboardSeries() throws IOException {
        TimeSeries<KeyboardInputTime> kbInputs = new TimeSeries<>();
        kbInputs.addKey(new KeyboardInputTime(1, KeyPress.DOWN, 30));
        kbInputs.addKey(new KeyboardInputTime(1, KeyPress.DOWN, 23));

        try {
            kbWriter.open();
        } catch (IOException e) {
            fail("should be allowed to open file");
        }

        kbWriter.write(kbInputs);
        kbWriter.close();

        JsonReader reader = new JsonReader("./data/testWriteKbInputs.kbinput");
        TimeSeries<KeyboardInputTime> fromJson = reader.readKeyboardTimeSeries();
        assertEquals(kbInputs, fromJson);
    }

    @Test
    void testWriteMouseSeries() throws IOException {
        TimeSeries<MouseInputTime> mouseInputs = new TimeSeries<>();
        mouseInputs.addKey(new MouseInputTime(new NativeMouseEvent(1, 2,3,4,5,6), 3000));
        mouseInputs.addKey(new MouseInputTime(new NativeMouseEvent(7, 8,9,10,11,12), 283));

        try {
            mouseWriter.open();
        } catch (IOException e) {
            fail("should be allowed to open file");
        }

        mouseWriter.write(mouseInputs);
        mouseWriter.close();

        JsonReader reader = new JsonReader("./data/testWriteMouseInputs.minput");
        TimeSeries<MouseInputTime> fromJson = reader.readMouseTimeSeries();
        assertEquals(mouseInputs, fromJson);
    }

    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }
}