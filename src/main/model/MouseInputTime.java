package model;

import org.json.JSONObject;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

/**
 * Concrete implementation of input/time pair for mouse inputs
 */
public class MouseInputTime extends InputTime {
    private final NativeMouseEvent event;

    // REQUIRES: nsSinceStart >= 0
    // EFFECTS: creates new class that wraps a MouseEvent and time since recording
    public MouseInputTime(NativeMouseEvent event, long nsSinceStart) {
        this.event = event;
        this.nsRecordedTimeStamp = nsSinceStart;
    }

    // EFFECTS: reates an instance of this from a json object
    public static MouseInputTime fromJson(JSONObject jsonObject) {
        int id = jsonObject.getInt("id");
        int modifiers = jsonObject.getInt("modifiers");
        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");
        int clickCount = jsonObject.getInt("clickCount");
        int button = jsonObject.getInt("button");
        long nsRecordedTimeStamp = jsonObject.getLong("nsRecordedTimeStamp");
        NativeMouseEvent mouseEvent = new NativeMouseEvent(id, modifiers, x, y, clickCount, button);
        return new MouseInputTime(mouseEvent, nsRecordedTimeStamp);
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
        if (null == o || this.getClass() != o.getClass()) {
            return false;
        }
        MouseInputTime that = (MouseInputTime) o;
        return this.event.getID() == that.event.getID() && this.event.getModifiers() == that.event.getModifiers()
                && this.event.getX() == that.event.getX() && this.event.getY() == that.event.getY()
                && this.event.getClickCount() == that.event.getClickCount()
                && this.event.getButton() == that.event.getButton()
                && this.nsRecordedTimeStamp == that.nsRecordedTimeStamp;
    }

    @Override
    public String getType() {
        return "Mouse";
    }

    // EFFECTS: returns jsonObject representation of class
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", this.event.getID());
        json.put("modifiers", this.event.getModifiers());
        json.put("x", this.event.getX());
        json.put("y", this.event.getY());
        json.put("clickCount", this.event.getClickCount());
        json.put("button", this.event.getButton());
        json.put("nsRecordedTimeStamp", this.nsRecordedTimeStamp);
        return json;
    }

    // EFFECTS: Returns capture unit as string
    @Override
    public String toString() {
        return String.format("x: %d, y: %d, btn: %d",
                this.event.getX(), this.event.getY(), this.event.getButton());
    }
}
