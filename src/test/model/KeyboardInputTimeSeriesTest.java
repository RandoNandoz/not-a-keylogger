package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class KeyboardInputTimeSeriesTest {
    private KeyboardInputTimeSeries series;

    @BeforeEach
    void setUp() {
        this.series = new KeyboardInputTimeSeries();
    }

    @Test
    void testConstructor() {
        assertEquals(0, series.getInputs().size());
    }

    @Test
    void testAddOneKey() {
        int spaceKey = NativeKeyEvent.VC_SPACE;

        series.addKey(new KeyboardInputTime(spaceKey, 30));

        assertEquals(1, series.getInputs().size());
        assertEquals(new KeyboardInputTime(spaceKey, 30), series.getInputs().get(0));
    }

    @Test
    void protectSeriesFromMod() {
        KeyboardInputTime key1 = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, 20);
        KeyboardInputTime key2 = new KeyboardInputTime(NativeKeyEvent.VC_ALT, 30);
        KeyboardInputTime key3 = new KeyboardInputTime(NativeKeyEvent.VC_F3, 40);

        series.addKey(key1);
        series.addKey(key2);
        series.addKey(key3);

        List<KeyboardInputTime> actions = series.getInputs();

        actions.clear();

        assertFalse(series.getInputs().isEmpty());
        assertEquals(key1, series.getInputs().get(0));
        assertEquals(key2, series.getInputs().get(1));
        assertEquals(key3, series.getInputs().get(2));
    }

    @Test
    void testAddManyKeys() {
        KeyboardInputTime key1 = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, 20);
        KeyboardInputTime key2 = new KeyboardInputTime(NativeKeyEvent.VC_ALT, 30);
        KeyboardInputTime key3 = new KeyboardInputTime(NativeKeyEvent.VC_F3, 40);
        KeyboardInputTime key4 = new KeyboardInputTime(NativeKeyEvent.VC_E, 41);
        KeyboardInputTime key5 = new KeyboardInputTime(NativeKeyEvent.VC_A, 42);
        KeyboardInputTime key6 = new KeyboardInputTime(NativeKeyEvent.VC_I, 345345);
        var keys = new KeyboardInputTime[]{key1, key2, key3, key4, key5, key6};

        for (KeyboardInputTime key : keys) {
            series.addKey(key);
        }

        assertEquals(6, series.getInputs().size());
    }

    @Test
    void testAddManyKeysOutOfOrder() {
        KeyboardInputTime[] keys = new KeyboardInputTime[]{
                new KeyboardInputTime(NativeKeyEvent.VC_X, 34),
                new KeyboardInputTime(NativeKeyEvent.VC_Q, 348957),
                new KeyboardInputTime(NativeKeyEvent.VC_W, 12),
                new KeyboardInputTime(NativeKeyEvent.VC_W, 29),
                new KeyboardInputTime(NativeKeyEvent.VC_W, 20),
                new KeyboardInputTime(NativeKeyEvent.VC_W, 0),
                new KeyboardInputTime(NativeKeyEvent.VC_W, 23598),
        };

        KeyboardInputTime[] expectedKeys = keys.clone();
        Arrays.sort(expectedKeys);

        for (KeyboardInputTime keyboardInputTime : keys) {
            series.addKey(keyboardInputTime);
        }

        assertEquals(Arrays.asList(expectedKeys), series.getInputs());
    }
}