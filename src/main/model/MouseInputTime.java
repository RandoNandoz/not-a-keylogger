/*
* THIS IS NOT MY CODE
* SAMPLE CODE FROM DOCUMENTATION TO PLAY AROUND WITH NATIVEMOUSEVENTS
* */

package model;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

public class MouseInputTime {
    private final double timeSinceStart;
    private NativeMouseEvent event;

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
