package model;

import java.util.LinkedList;
import java.util.List;

public class KeyboardInputTimeSeries {
    /* TODO: determine whether ArrayList or LinkedList is best for this task, for now use LinkedList,
        as it seems most suited for the stack/queue style we're treating this as */
    private final LinkedList<KeyboardInputTime> inputs;


    // EFFECTS: creates a new keyboard input times series capture with an empty list of captures
    public KeyboardInputTimeSeries() {
        this.inputs = new LinkedList<>();
    }

    // REQUIRES: inputTime.getNsSinceStart() >= 0,
    // MODIFIES: this
    // EFFECTS: adds keyEvent to the internal list that holds the keyEvent in
    //          temporal order (that is, sorted from s, guarantees that getInputs() will be in sorted order after call
    public void addKey(KeyboardInputTime inputTime) {

    }

    // REQUIRES: 0 <= index < this.getInputs().size(), inputTime.getNsSinceStart() >= 0
    // MODIFIES: this
    // EFFECTS: modifies the NativeKeyEvent at this index
    //          preserves sorted order of data
    //          returns the new index of the key
    public int editKey(int index, KeyboardInputTime inputTime) {
        return 0; // stub
    }

    // REQUIRES: 0 <= index < this.getInputs().size()
    // MODIFIES: this
    // EFFECTS: deletes the given index, preserving sorted order
    public void deleteIndex(int index) {

    }

    public List<KeyboardInputTime> getInputs() {
        return inputs;
    }
}
