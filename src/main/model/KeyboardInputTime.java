package model;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Class to represent unit of data that stores keyId from a NativeKeyEvent, and when it was
 * recorded in nanoseconds since start.
 *
 * @implNote This class has a natural ordering that is inconsistent with equals.
 */
public class KeyboardInputTime extends InputTime {
    private final KeyPress keyPress;
    private final int keyId;

    // EFFECTS: instantiates a new KeyBoardInputTime object with given Key ID and time since start of recording
    public KeyboardInputTime(int keyId, KeyPress keyPress, long nsSinceStart) {
        this.keyId = keyId;
        this.keyPress = keyPress;
        this.nsRecordedTimeStamp = nsSinceStart;
    }

    // getters and setters
    public int getKeyId() {
        return this.keyId;
    }


    public KeyPress getKeyPress() {
        return this.keyPress;
    }


    // EFFECTS: true if the object given has the same params as this.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((null == o) || (getClass() != o.getClass())) {
            return false;
        }
        KeyboardInputTime that = (KeyboardInputTime) o;
        return (this.nsRecordedTimeStamp == that.nsRecordedTimeStamp)
                && (this.keyId == that.keyId)
                && (this.keyPress == that.keyPress);
    }

    // EFFECTS: returns hash code
    @Override
    public int hashCode() {
        return Objects.hash(this.keyPress, this.keyId, this.nsRecordedTimeStamp);
    }

    // EFFECTS: returns jsonObject representation of class
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("keyPress", this.keyPress);
        json.put("keyId", this.keyId);
        json.put("nsRecordedTimeStamp", this.nsRecordedTimeStamp);
        return json;
    }

    // EFFECTS: reveals type of recording
    @Override
    public String getType() {
        return "Keyboard";
    }

    // EFFECTS: creates an instance of this from a json object
    public static KeyboardInputTime fromJson(JSONObject jsonObject) {
        var keyPress = KeyPress.valueOf(jsonObject.getString("keyPress"));
        var keyId = jsonObject.getInt("keyId");
        var nsRecordedTimeStamp = jsonObject.getLong("nsRecordedTimeStamp");
        return new KeyboardInputTime(keyId, keyPress, nsRecordedTimeStamp);
    }

    // EFFECTS: Returns capture unit as string
    @Override
    public String toString() {
        return "Keyboard Capture{" + "keyPress=" + keyPress + ", keyId=" + keyId + '}';
    }
}
