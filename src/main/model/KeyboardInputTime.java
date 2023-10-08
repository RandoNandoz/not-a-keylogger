package model;

/**
 * Class to represent unit of data that stores keyId from a NativeKeyEvent, and when it was
 * recorded in nanoseconds since start.
 * @implNote This class has a natural ordering that is inconsistent with equals.
 */
public class KeyboardInputTime implements Comparable<KeyboardInputTime> {
    private final long nsSinceStart;
    private final int keyId;

    // EFFECTS: instantiates a new KeyBoardInputTime object with given Key ID and time since start of recording
    public KeyboardInputTime(int keyId, long nsSinceStart) {
        this.keyId = keyId;
        this.nsSinceStart = nsSinceStart;
    }

    // getters and setters
    public int getKeyId() {
        return this.keyId;
    }


    public long getNsSinceStart() {
        return nsSinceStart;
    }

    @Override
    public int compareTo(KeyboardInputTime o) {
        return Long.compare(nsSinceStart, o.getNsSinceStart());
    }
}
