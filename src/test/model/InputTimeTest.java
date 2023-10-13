package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}