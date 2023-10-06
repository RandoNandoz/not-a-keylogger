package model;

import java.util.LinkedList;
import java.util.List;

public class KeyboardInputTimeSeries {
    /* TODO: determine whether ArrayList or LinkedList is best for this task, for now use LinkedList,
        as it seems most suited for the stack/queue style we're treating this as */
    List<KeyboardInputTime> inputs;


    // EFFECTS: creates a new keyboard input times series capture with an empty list of captures
    public KeyboardInputTimeSeries() {
        this.inputs = new LinkedList<>();
    }

    // REQUIRES: duration.toNanos() >= 0
    // MODIFIES: this
    // EFFECTS: adds keyEvent to the internal list that holds the keyEvent in
    // temporal order (that is, sorted from s, guarantees that getInputs() will be in sorted order after call
    public void addKey(KeyboardInputTime inputTime) {

    }

    // REQUIRES: 0 <= index < inputs.size();
    // MODIFIES: this
    // EFFECTS: modifies the NativeKeyEvent at this index
    //          preserves sorted order of data
    //          returns the new index of the key
    public int editKey(int index, KeyboardInputTime inputTime) {
        return 0; // stub
    }

    // REQUIRES: 0 <= index < inputs.size()
    // MODIFIES: this
    // EFFECTS: deletes the given index, preserves sorted order
    public void deleteIndex(int index) {

    }

    public int size() {
        return this.inputs.size();
    }

    public List<KeyboardInputTime> getInputs() {
        return inputs;
    }
}
