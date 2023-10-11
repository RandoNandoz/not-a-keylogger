/*
 * THIS IS NOT MY CODE
 * SAMPLE CODE FROM DOCUMENTATION TO PLAY AROUND WITH NATIVEMOUSEVENTS
 * */

package model;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import java.util.Objects;

public class MouseInputTime extends InputTime {
    private final NativeMouseEvent event;

    // EFFECTS: creates new class that wraps a MouseEvent and time since recording
    public MouseInputTime(NativeMouseEvent event, long nsSinceStart) {
        this.event = event;
        this.nsSinceStart = nsSinceStart;
    }

    public NativeMouseEvent getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MouseInputTime that = (MouseInputTime) o;
        return this.event.getID() == that.event.getID()
                && this.event.getModifiers() == that.event.getModifiers()
                && this.event.getX() == that.event.getX()
                && this.event.getY() == that.event.getY()
                && this.event.getClickCount() == that.event.getClickCount()
                && this.event.getButton() == that.event.getButton()
                && this.nsSinceStart == that.nsSinceStart;
    }
}
