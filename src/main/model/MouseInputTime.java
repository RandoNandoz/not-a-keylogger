package model;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

public class MouseInputTime extends InputTime {
    private final NativeMouseEvent event;

    // REQUIRES: nsSinceStart >= 0
    // EFFECTS: creates new class that wraps a MouseEvent and time since recording
    public MouseInputTime(NativeMouseEvent event, long nsSinceStart) {
        this.event = event;
        this.nsRecordedTimeStamp = nsSinceStart;
    }

    // trivial getter
    public NativeMouseEvent getEvent() {
        return event;
    }

    // EFFECTS: does deep comparison between given object and if they have the same params, return true, false otherwise
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
                & this.event.getModifiers() == that.event.getModifiers()
                & this.event.getX() == that.event.getX()
                & this.event.getY() == that.event.getY()
                & this.event.getClickCount() == that.event.getClickCount()
                & this.event.getButton() == that.event.getButton()
                & this.nsRecordedTimeStamp == that.nsRecordedTimeStamp;
    }
}
