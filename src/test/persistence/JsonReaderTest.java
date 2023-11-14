package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import model.InputRecording;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import model.KeyPress;
import model.KeyboardInputTime;
import model.MouseInputTime;

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
        InputRecording<KeyboardInputTime> k = keyboardInputTimeReader.readKeyboardTimeSeries();
        KeyboardInputTime kbInputTime = new KeyboardInputTime(1, KeyPress.UP, 5);
        final long startTime = 3;
        ArrayList<KeyboardInputTime> inputs = new ArrayList<>();
        inputs.add(kbInputTime);

        assertEquals(new InputRecording<>(inputs, startTime), k);
    }

    @Test
    void testReadMouseIn() throws IOException {
        InputRecording<MouseInputTime> m = mouseInputTimeReader.readMouseTimeSeries();
        MouseInputTime mi = new MouseInputTime(new NativeMouseEvent(
                1, 2, 3, 4, 1, 3), 20);
        ArrayList<MouseInputTime> inputs = new ArrayList<>();
        inputs.add(mi);
        InputRecording<MouseInputTime> expected = new InputRecording<>(
                inputs,
                10);

        assertEquals(expected, m);
    }
}