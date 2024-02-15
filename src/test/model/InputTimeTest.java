package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

class InputTimeTest {
    long startTime;
    private InputTime inputTime;

    @BeforeEach
    void setUp() {
        startTime = System.nanoTime();
        inputTime = new KeyboardInputTime(NativeKeyEvent.VC_ESCAPE, KeyPress.UP, startTime + 400);
    }

    @Test
    void testGetDeltaTime() {
        assertEquals(400, inputTime.getDeltaTime(startTime));
    }

    @Test
    void testGetType() {
        assertEquals("Mouse", new MouseInputTime(new NativeMouseEvent(1, 2, 3, 4, 5, 6), 10).getType());
        assertEquals("Keyboard", new KeyboardInputTime(1,KeyPress.UP,30).getType());
    }
}