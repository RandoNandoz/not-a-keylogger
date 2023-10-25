package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TimeSeriesTest {
    private Random rd;
    private TimeSeries<KeyboardInputTime> series;

    @BeforeEach
    void setUp() {
        this.series = new TimeSeries<>();
        this.rd = new Random();
    }

    @Test
    void testConstructor() throws IllegalAccessException {
        assertEquals(0, series.getInputs().size());

        // weird reflection hack
        Field f;
        try {
            f = TimeSeries.class.getDeclaredField("startTime");
        } catch (NoSuchFieldException e) {
            // this should never happen
            throw new RuntimeException(e);
        }
        f.setAccessible(true);


        long startTimeInClass = (long) f.get(series);

        assertEquals(startTimeInClass, series.getStartTime());
    }

    @Test
    void testEditErrorThrowing() {
        assertThrows(IllegalArgumentException.class, () -> {
            series.editKey(-1, new KeyboardInputTime(
                    0,
                    KeyPress.DOWN,
                    0
            ));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            series.editKey(series.getInputs().size() + 40, new KeyboardInputTime(
                    0,
                    KeyPress.DOWN,
                    0
            ));
        });
    }

    @Test
    void testAddOneKey() {
        final int spaceKey = NativeKeyEvent.VC_SPACE;

        series.addKey(new KeyboardInputTime(spaceKey, KeyPress.UP, 30));

        assertEquals(1, series.getInputs().size());
        assertEquals(new KeyboardInputTime(spaceKey, KeyPress.UP, 30), series.getInputs().get(0));
    }

    @Disabled
    @Test
    void protectSeriesFromMod() {
        KeyboardInputTime key1 = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, KeyPress.UP, 20);
        KeyboardInputTime key2 = new KeyboardInputTime(NativeKeyEvent.VC_ALT, KeyPress.UP, 30);
        KeyboardInputTime key3 = new KeyboardInputTime(NativeKeyEvent.VC_F3, KeyPress.UP, 40);

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
        KeyboardInputTime key1 = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, KeyPress.UP, 20);
        KeyboardInputTime key2 = new KeyboardInputTime(NativeKeyEvent.VC_ALT, KeyPress.UP, 30);
        KeyboardInputTime key3 = new KeyboardInputTime(NativeKeyEvent.VC_F3, KeyPress.UP, 40);
        KeyboardInputTime key4 = new KeyboardInputTime(NativeKeyEvent.VC_E, KeyPress.UP, 41);
        KeyboardInputTime key5 = new KeyboardInputTime(NativeKeyEvent.VC_A, KeyPress.UP, 42);
        KeyboardInputTime key6 = new KeyboardInputTime(NativeKeyEvent.VC_I, KeyPress.UP, 345345);
        var keys = new KeyboardInputTime[]{key1, key2, key3, key4, key5, key6};

        for (KeyboardInputTime key : keys) {
            series.addKey(key);
        }

        assertEquals(6, series.getInputs().size());
    }

    @Test
    void testAddManyKeysOutOfOrder() {
        KeyboardInputTime[] keys = {
                new KeyboardInputTime(NativeKeyEvent.VC_X, KeyPress.UP, 34),
                new KeyboardInputTime(NativeKeyEvent.VC_Q, KeyPress.UP, 348957),
                new KeyboardInputTime(NativeKeyEvent.VC_W, KeyPress.UP, 12),
                new KeyboardInputTime(NativeKeyEvent.VC_W, KeyPress.UP, 29),
                new KeyboardInputTime(NativeKeyEvent.VC_W, KeyPress.UP, 20),
                new KeyboardInputTime(NativeKeyEvent.VC_W, KeyPress.UP, 0),
                new KeyboardInputTime(NativeKeyEvent.VC_W, KeyPress.UP, 23598),
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
        KeyboardInputTime[] keys = {
                new KeyboardInputTime(NativeKeyEvent.VC_X, KeyPress.UP, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_Q, KeyPress.UP, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_W, KeyPress.UP, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_L, KeyPress.UP, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_D, KeyPress.UP, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_H, KeyPress.UP, rd.nextLong()),
                new KeyboardInputTime(NativeKeyEvent.VC_P, KeyPress.UP, rd.nextLong()),
        };

        KeyboardInputTime[] expectedKeys = keys.clone();
        Arrays.sort(expectedKeys);

        for (KeyboardInputTime keyboardInputTime : keys) {
            series.addKey(keyboardInputTime);
        }

        assertEquals(Arrays.asList(expectedKeys), series.getInputs());
    }

    @Test
    void testEditKeyOneKeyInOrder() {
        KeyboardInputTime[] keys = {
                new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, KeyPress.UP, 20),
                new KeyboardInputTime(NativeKeyEvent.VC_ALT, KeyPress.UP, 30),
                new KeyboardInputTime(NativeKeyEvent.VC_F3, KeyPress.UP, 40),
                new KeyboardInputTime(NativeKeyEvent.VC_E, KeyPress.UP, 41),
                new KeyboardInputTime(NativeKeyEvent.VC_A, KeyPress.UP, 42),
                new KeyboardInputTime(NativeKeyEvent.VC_I, KeyPress.UP, 345345)
        };

        for (KeyboardInputTime key : keys) {
            series.addKey(key);
        }

        int newPos = series.editKey(2, new KeyboardInputTime(NativeKeyEvent.VC_F4, KeyPress.DOWN, 40));

        assertEquals(2, newPos);

        assertEquals(NativeKeyEvent.VC_F4, series.getInputs().get(2).getKeyId());
        assertEquals(KeyPress.DOWN, series.getInputs().get(2).getKeyPress());
        assertEquals(40, series.getInputs().get(2).getNsRecordedTimeStamp());
    }

    @Test
    void testEditManyKeysInOrder() {
        KeyboardInputTime[] keys = {
                new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, KeyPress.UP, 20),
                new KeyboardInputTime(NativeKeyEvent.VC_ALT, KeyPress.UP, 30),
                new KeyboardInputTime(NativeKeyEvent.VC_F3, KeyPress.UP, 40),
                new KeyboardInputTime(NativeKeyEvent.VC_E, KeyPress.UP, 41),
                new KeyboardInputTime(NativeKeyEvent.VC_A, KeyPress.UP, 42),
                new KeyboardInputTime(NativeKeyEvent.VC_I, KeyPress.UP, 345345)
        };

        for (KeyboardInputTime key : keys) {
            series.addKey(key);
        }

        int newPosOf0 = series.editKey(0, new KeyboardInputTime(NativeKeyEvent.VC_DELETE, KeyPress.UP, 20));
        int newPosOf5 = series.editKey(5, new KeyboardInputTime(NativeKeyEvent.VC_L, KeyPress.UP, 345344));

        assertEquals(0, newPosOf0);
        assertEquals(5, newPosOf5);
    }

    @Test
    void testEditKeySortedOrder() {
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_K, KeyPress.UP, 1));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_E, KeyPress.UP, 2));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_V, KeyPress.UP, 3));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_I, KeyPress.UP, 4));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_N, KeyPress.UP, 5));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, KeyPress.UP, 25));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_H, KeyPress.UP, 50));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, KeyPress.UP, 51));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_I, KeyPress.UP, 52));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_S, KeyPress.UP, 53));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, KeyPress.UP, 54));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_C, KeyPress.UP, 55));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_R, KeyPress.UP, 56));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_A, KeyPress.UP, 59));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_C, KeyPress.UP, 62));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_K, KeyPress.UP, 74));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_E, KeyPress.UP, 90));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_D, KeyPress.UP, 99));

        int oldSeriesSize = series.getInputs().size();

        assertEquals(series.getInputs().size() - 1,
                series.editKey(1, new KeyboardInputTime(NativeKeyEvent.VC_E, KeyPress.UP, 100)));

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_E, KeyPress.UP, 100),
                series.getInputs().get(series.getInputs().size() - 1));

        assertEquals(14, series.editKey(4, new KeyboardInputTime(NativeKeyEvent.VC_SPACE, KeyPress.UP, 80)));

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_SPACE, KeyPress.UP, 80), series.getInputs().get(
                14
        ));

        assertEquals(oldSeriesSize, series.getInputs().size());
    }

    @Test
    void testDelete() {
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_T, KeyPress.UP, 1));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_R, KeyPress.UP, 2));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_U, KeyPress.UP, 3));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_S, KeyPress.UP, 4));
        series.addKey(new KeyboardInputTime(NativeKeyEvent.VC_T, KeyPress.UP, 5));

        int oldSize = series.getInputs().size();

        series.deleteIndex(0);
        --oldSize;
        assertEquals(oldSize, series.getInputs().size());

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_R, KeyPress.UP, 2), series.getInputs().get(0));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_U, KeyPress.UP, 3), series.getInputs().get(1));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_S, KeyPress.UP, 4), series.getInputs().get(2));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_T, KeyPress.UP, 5), series.getInputs().get(3));

        series.deleteIndex(2);

        --oldSize;
        assertEquals(oldSize, series.getInputs().size());

        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_R, KeyPress.UP, 2), series.getInputs().get(0));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_U, KeyPress.UP, 3), series.getInputs().get(1));
        assertEquals(new KeyboardInputTime(NativeKeyEvent.VC_T, KeyPress.UP, 5), series.getInputs().get(2));
    }

    @Test
    void testEquals() throws Exception {
        // same object
        TimeSeries<KeyboardInputTime> inputs = new TimeSeries<>();
        assertEquals(inputs, inputs);

        // same values, different refs
        TimeSeries<KeyboardInputTime> definitelyDifferent = new TimeSeries<>();
        // we have to mutate private field to be the same
        // bad practice but wtv
        // https://www.geeksforgeeks.org/how-to-access-private-field-and-method-using-reflection-in-java/
        Field startTime = TimeSeries.class.getDeclaredField("startTime");
        startTime.setAccessible(true);
        startTime.setLong(definitelyDifferent, inputs.getStartTime());

        assertEquals(inputs, definitelyDifferent);

        // compare with null
        assertNotEquals(null, inputs);

        // different classes
        TreeSet<Integer> i = new TreeSet<>();
//        assertNotEquals(i, inputs);
        assertNotEquals(inputs, i);

        // different start time, same inputs
        TimeSeries<KeyboardInputTime> inputs1 = new TimeSeries<>();
        assertNotEquals(inputs1, inputs);

        // same start time, diff inputs
        inputs.addKey(new KeyboardInputTime(10, KeyPress.DOWN, 3000));

        assertNotEquals(inputs, definitelyDifferent);
    }

    @Test
    void testHashCode() {
        int expectedHash = Objects.hash(
                this.series.getInputs(),
                this.series.getStartTime()
        );

        assertEquals(expectedHash, this.series.hashCode());
    }
}