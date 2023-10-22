package model;

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
        return keyId;
    }


    public KeyPress getKeyPress() {
        return keyPress;
    }


    // EFFECTS: true if the object given has the same params as this.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyboardInputTime that = (KeyboardInputTime) o;
        return nsRecordedTimeStamp == that.nsRecordedTimeStamp & keyId == that.keyId & keyPress == that.keyPress;
    }
}
