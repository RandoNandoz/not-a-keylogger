package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Objects;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

class KeyboardInputTimeTest {
    private KeyboardInputTime kbInput1;
    private KeyboardInputTime kbInput2;
    private KeyboardInputTime kbInput3;
    private KeyboardInputTime kbInput4;

    @BeforeEach
    void setUp() {
        this.kbInput1 = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, KeyPress.UP, 30);
        this.kbInput2 = new KeyboardInputTime(NativeKeyEvent.VC_F4, KeyPress.UP, 29);
        this.kbInput3 = new KeyboardInputTime(NativeKeyEvent.VC_TAB, KeyPress.UP, 31);
        this.kbInput4 = new KeyboardInputTime(NativeKeyEvent.VC_ALT, KeyPress.UP, 30);
    }

    @Test
    void testConstructor() {
        assertEquals(NativeKeyEvent.VC_ESCAPE, kbInput1.getKeyId());
        assertEquals(30, kbInput1.getNsRecordedTimeStamp());
    }

    @Test
    void testGreaterThan() {
        assertEquals(1, kbInput1.compareTo(kbInput2));
        assertEquals(1, kbInput3.compareTo(kbInput1));
        assertEquals(1, kbInput3.compareTo(kbInput2));
    }

    @Test
    void testCompareEquals() {
        // test reflexive property
        //noinspection EqualsWithItself
        assertEquals(0, kbInput1.compareTo(kbInput1));

        // note that !kbInput1.getKeyId().equals(kbInput4.getKeyId())
        assertEquals(0, kbInput1.compareTo(kbInput4));
        assertEquals(0, kbInput4.compareTo(kbInput1));
    }

    @Test
    void testLessThan() {
        assertEquals(-1, kbInput2.compareTo(kbInput1));
        assertEquals(-1, kbInput2.compareTo(kbInput3));
        assertEquals(-1, kbInput2.compareTo(kbInput4));

        assertEquals(-1, kbInput1.compareTo(kbInput3));
        assertEquals(-1, kbInput4.compareTo(kbInput3));
    }

    @Test
    void testEquals() {
        // random object
        TreeSet<String> friends = new TreeSet<>();
        friends.add("Bob");
        friends.add("Joe");
        friends.add("Your mom");

        assertNotEquals(kbInput1, friends);

        // same reference
        KeyboardInputTime sameRef = kbInput1;
        assertEquals(this.kbInput1, sameRef);

        // different reference, same object
        KeyboardInputTime sameValue = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, KeyPress.UP, 30);
        assertEquals(this.kbInput1, sameValue);

        // comparison with null
        assertNotEquals(kbInput1, null);

        // consider timestamp != timestamp, keyId != keyId, and keyPress != keyPress
        var keyboardInputTime1 = new KeyboardInputTime(
                1, KeyPress.UP, 1
        );
        var keyboardInputTime2 = new KeyboardInputTime(
                2, KeyPress.UP, 1
        );

        assertNotEquals(keyboardInputTime1, keyboardInputTime2);

        var keyboardInputTime3 = new KeyboardInputTime(
                0, KeyPress.UP, 1
        );
        var keyboardInputTime4 = new KeyboardInputTime(
                0, KeyPress.DOWN, 1
        );

        assertNotEquals(keyboardInputTime3, keyboardInputTime4);

        var keyboardInputTime5 = new KeyboardInputTime(
                0, KeyPress.DOWN, 2
        );
        var keyboardInputTime6 = new KeyboardInputTime(
                0, KeyPress.DOWN, 1
        );

        assertNotEquals(keyboardInputTime5, keyboardInputTime6);
    }

    @Test
    void testHashCode() {
        final KeyPress kp = KeyPress.DOWN;
        final int keyId = 20;
        final long nsRecordedTs = 300;
        int expectedHash = Objects.hash(kp, keyId, nsRecordedTs);

        var keyIn = new KeyboardInputTime(keyId, kp, nsRecordedTs);

        assertEquals(expectedHash, keyIn.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Keyboard Capture{keyPress=UP, keyId=0}", new KeyboardInputTime(0,KeyPress.UP,0).toString());
    }
}