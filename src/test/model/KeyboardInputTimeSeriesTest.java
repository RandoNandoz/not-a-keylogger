package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyboardInputTimeSeriesTest {
    private KeyboardInputTimeSeries series;

    @BeforeEach
    void setUp() {
        this.series = new KeyboardInputTimeSeries();
    }

    @Test
    void testConstructor() {
        assertEquals(0, this.series.size());
    }

    @Test
    void testAddOneKey() {
//        KeyboardInputTime = new KeyboardInputTime(
//                new NativeKeyEvent()
//        )
    }
}