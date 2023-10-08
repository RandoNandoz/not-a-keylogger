package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class KeyboardInputTimeSeriesTest {
    private Random rd;
    private KeyboardInputTimeSeries series;

    @BeforeEach
    void setUp() {
        this.series = new KeyboardInputTimeSeries();
        this.rd = new Random();
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

    @Test
    void testAddManyKeysRandomizedOrder() {
        KeyboardInputTime[] keys = new KeyboardInputTime[]{
                new KeyboardInputTime(NativeKeyEvent.VC_X, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_Q, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_W, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_L, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_D, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_H, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_P, rd.nextLong()),
        };
    }

    @Test
    void testEditKeyOneKeyInOrder() {
        KeyboardInputTime[] keys = new KeyboardInputTime[]{
                new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, 20),
                new KeyboardInputTime(NativeKeyEvent.VC_ALT, 30),
                new KeyboardInputTime(NativeKeyEvent.VC_F3, 40),
                new KeyboardInputTime(NativeKeyEvent.VC_E, 41),
                new KeyboardInputTime(NativeKeyEvent.VC_A, 42),
                new KeyboardInputTime(NativeKeyEvent.VC_I, 345345)
        };

        for (KeyboardInputTime key : keys) {
            series.addKey(key);
        }

        int newPos = series.editKey(2, new KeyboardInputTime(NativeKeyEvent.VC_F4, 40));

        assertEquals(2, newPos);

        assertEquals(NativeKeyEvent.VC_F4, series.getInputs().get(2).getKeyId());
        assertEquals(40, series.getInputs().get(2).getNsSinceStart());
    }

    @Test
    void testEditManyKeysInOrder() {
        KeyboardInputTime[] keys = new KeyboardInputTime[]{
                new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, 20),
                new KeyboardInputTime(NativeKeyEvent.VC_ALT, 30),
                new KeyboardInputTime(NativeKeyEvent.VC_F3, 40),
                new KeyboardInputTime(NativeKeyEvent.VC_E, 41),
                new KeyboardInputTime(NativeKeyEvent.VC_A, 42),
                new KeyboardInputTime(NativeKeyEvent.VC_I, 345345)
        };

        for (KeyboardInputTime key : keys) {
            series.addKey(key);
        }

        int newPosOf0 = series.editKey(0, new KeyboardInputTime(NativeKeyEvent.VC_DELETE, 20));
        int newPosOf5 = series.editKey(5, new KeyboardInputTime(NativeKeyEvent.VC_L, 345344));

        assertEquals(0, newPosOf0);
        assertEquals(5, newPosOf5);
    }

    @Test
    void testEditKeySortedOrder() {
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_K, 1));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_E, 2));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_V, 3));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_I, 4));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_N, 5));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, 25));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_H, 50));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, 51));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_I, 52));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_S, 53));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, 54));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_C, 55));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_R, 56));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_A, 59));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_C, 62));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_K, 74));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_E, 90));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_D, 99));

        int oldSeriesSize = series.getInputs().size();

        assertEquals(series.getInputs().size() - 1,
                series.editKey(1, new KeyboardInputTime(NativeKeyEvent.VC_E, 100)));

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_E, 100),
                series.getInputs().get(series.getInputs().size() - 1));

        assertEquals(13, series.editKey(4, new KeyboardInputTime(NativeKeyEvent.VC_SPACE, 80)));

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, 80), series.getInputs().get(
                13
        ));

        assertEquals(oldSeriesSize, series.getInputs().size());
    }

    @Test
    void testDelete() {
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_T, 1));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_R, 2));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_U, 3));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_S, 4));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_T, 5));

        int oldSize = series.getInputs().size();

        series.deleteIndex(0);
        assertEquals(--oldSize, series.getInputs().size());

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_R, 2), series.getInputs().get(0));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_U, 2), series.getInputs().get(1));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_S, 2), series.getInputs().get(2));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_T, 2), series.getInputs().get(3));

        series.deleteIndex(2);

        assertEquals(--oldSize, series.getInputs().size());

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_R, 2), series.getInputs().get(0));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_U, 2), series.getInputs().get(1));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_T, 2), series.getInputs().get(2));
    }
}