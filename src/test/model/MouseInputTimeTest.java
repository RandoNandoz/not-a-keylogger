package model;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    }
}