package model;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

public class MouseInputTime {
    private NativeMouseEvent event;
    private double timeSinceStart;

    // EFFECTS: creates new class that wraps a MouseEvent and time since recording
    public MouseInputTime(NativeMouseEvent event, double timeSinceStart) {
        this.event = event;
        this.timeSinceStart = timeSinceStart;
    }

    public NativeMouseEvent getEvent() {
        return event;
    }

    public void setEvent(NativeMouseEvent event) {
        this.event = event;
    }

    public double getTimeSinceStart() {
        return timeSinceStart;
    }
}
