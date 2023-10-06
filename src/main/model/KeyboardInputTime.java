package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.time.Duration;

public class KeyboardInputTime {
    private final Duration timeSinceStart;
    private NativeKeyEvent event;

    public KeyboardInputTime(NativeKeyEvent event, Duration timeSinceStart) {
        this.event = event;
        this.timeSinceStart = timeSinceStart;
    }

    // getters and setters
    public NativeKeyEvent getEvent() {
        return event;
    }

    public void setEvent(NativeKeyEvent event) {
        this.event = event;
    }

    public Duration getTimeSinceStart() {
        return timeSinceStart;
    }
}
