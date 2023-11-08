package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;
import model.TimeSeries;

class JsonReaderTest {
    JsonReader keyboardInputTimeReader;
    JsonReader mouseInputTimeReader;

    @BeforeEach
    void setUp() {
        this.keyboardInputTimeReader = new JsonReader("./data/testJsonKbInputs.kbinput");
        this.mouseInputTimeReader = new JsonReader("./data/testJsonMouseInputs.minput");
    }

    @Test
    void testReadKeyIn() throws IOException {
        TimeSeries<KeyboardInputTime> k = (TimeSeries<KeyboardInputTime>) keyboardInputTimeReader
                .readTimeSeries(KeyboardInputTime.class);
        KeyboardInputTime kbInputTime = new KeyboardInputTime(1, KeyPress.UP, 5);
        final long startTime = 3;
        ArrayList<KeyboardInputTime> inputs = new ArrayList<>();
        inputs.add(kbInputTime);

        assertEquals(new TimeSeries<>(inputs, startTime), k);
    }

    @Test
    void testReadMouseIn() throws IOException {
        TimeSeries<MouseInputTime> m = (TimeSeries<MouseInputTime>) mouseInputTimeReader
                .readTimeSeries(MouseInputTime.class);
        MouseInputTime mi = new MouseInputTime(new NativeMouseEvent(
                1, 2, 3, 4, 1, 3), 20);
        ArrayList<MouseInputTime> inputs = new ArrayList<>();
        inputs.add(mi);
        TimeSeries<MouseInputTime> expected = new TimeSeries<>(
                inputs,
                10);

        assertEquals(expected, m);
    }

    @Test
    void testConstructIllegalReader() { // this is a dumb test
        assertThrows(IllegalArgumentException.class, () -> {
            mouseInputTimeReader.readTimeSeries(FakeClass.class);
        });
    }
}