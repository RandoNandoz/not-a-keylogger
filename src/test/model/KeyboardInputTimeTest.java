package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    }
}