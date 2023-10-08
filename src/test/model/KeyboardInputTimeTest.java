package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyboardInputTimeTest {
    private KeyboardInputTime kbInput1;
    private KeyboardInputTime kbInput2;
    private KeyboardInputTime kbInput3;
    private KeyboardInputTime kbInput4;

    @BeforeEach
    void setUp() {
        this.kbInput1 = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, 30);
        this.kbInput2 = new KeyboardInputTime(NativeKeyEvent.VC_F4, 29);
        this.kbInput3 = new KeyboardInputTime(NativeKeyEvent.VC_TAB, 31);
        this.kbInput4 = new KeyboardInputTime(NativeKeyEvent.VC_ALT, 30);
    }

    @Test
    void testConstructor() {
        assertEquals(NativeKeyEvent.VC_ESCAPE, kbInput1.getKeyId());
        assertEquals(30, kbInput1.getNsSinceStart());
    }

    @Test
    void testGreaterThan() {
        assertEquals(1, kbInput1.compareTo(kbInput2));
        assertEquals(1, kbInput3.compareTo(kbInput1));
        assertEquals(1, kbInput3.compareTo(kbInput2));
    }

    @Test
    void testEquals() {
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
}