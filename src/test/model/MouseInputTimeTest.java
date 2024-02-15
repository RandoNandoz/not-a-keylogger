package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

class MouseInputTimeTest {
    MouseInputTime mouseInputTime;

    @BeforeEach
    void setUp() {
        mouseInputTime = new MouseInputTime(
                new NativeMouseEvent(NativeMouseEvent.NATIVE_MOUSE_PRESSED, 0, 123, 456, 1),
                300
        );
    }

    @Test
    void testConstructor() {
        assertEquals(
                NativeMouseEvent.NATIVE_MOUSE_PRESSED,
                this.mouseInputTime.getEvent().getID()
        );

        assertEquals(300, this.mouseInputTime.getNsRecordedTimeStamp());
    }

    @Test
    void testEquals() {
        // random object
        TreeSet<String> friends = new TreeSet<>();
        friends.add("Bob");
        friends.add("Joe");
        friends.add("Your mom");

        assertNotEquals(mouseInputTime, friends);

        // same reference
        MouseInputTime sameRef = mouseInputTime;
        assertEquals(this.mouseInputTime, sameRef);

        // different reference, same object
        MouseInputTime sameValue = new MouseInputTime(
                new NativeMouseEvent(NativeMouseEvent.NATIVE_MOUSE_PRESSED, 0, 123, 456, 1),
                300
        );
        assertEquals(this.mouseInputTime, sameValue);


        // id != id, x!=x, modifiers != modifiers, etc.
        // sigh...
        {
            var m1 = new MouseInputTime(new NativeMouseEvent(
                    1, 2, 2, 2, 2, 2
            ), 2);
            var m2 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 2);
            assertNotEquals(m1, m2);
        }
        {
            var m1 = new MouseInputTime(new NativeMouseEvent(
                    2, 1, 2, 2, 2, 2
            ), 2);
            var m2 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 2);
            assertNotEquals(m1, m2);
        }
        {
            var m1 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 1, 2, 2, 2
            ), 2);
            var m2 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 2);
            assertNotEquals(m1, m2);
        }
        {
            var m1 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 1, 2, 2
            ), 2);
            var m2 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 2);
            assertNotEquals(m1, m2);
        }
        {
            var m1 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 1, 2
            ), 2);
            var m2 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 2);
            assertNotEquals(m1, m2);
        }
        {
            var m1 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 1
            ), 2);
            var m2 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 2);
            assertNotEquals(m1, m2);
        }
        {
            var m1 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 1);
            var m2 = new MouseInputTime(new NativeMouseEvent(
                    2, 2, 2, 2, 2, 2
            ), 2);
            assertNotEquals(m1, m2);
        }

        // compare with null
        assertNotEquals(mouseInputTime, null);
    }

    @Test
    void testToString() {
        assertEquals("x: 0, y: 0, btn: 0", new MouseInputTime(new NativeMouseEvent(0, 0,0,0,0,0), 0).toString());
    }
}