package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class KeyboardInputTime {
    private final double timeSinceStart;
    private NativeKeyEvent event;

    public KeyboardInputTime(NativeKeyEvent event, double timeSinceStart) {
        this.event = event;
        this.timeSinceStart = timeSinceStart;
    }

    public NativeKeyEvent getEvent() {
        return event;
    }

    public void setEvent(NativeKeyEvent event) {
        this.event = event;
    }

    public double getTimeSinceStart() {
        return timeSinceStart;
    }
}
