package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeSeries<T extends InputTime> {
    private final ArrayList<T> inputs;
    private final long startTime;

    // EFFECTS: creates a new keyboard input times series capture with an empty list of captures
    public TimeSeries() {
        startTime = System.nanoTime();
        this.inputs = new ArrayList<>();
    }

    // REQUIRES: inputTime.getNsSinceStart() >= 0,
    // MODIFIES: this
    // EFFECTS: adds keyEvent to the internal list that holds the keyEvent in
    //          temporal order (that is, sorted from s, guarantees that getInputs() will be in sorted order after call
    public void addKey(T inputTime) {
        if (this.inputs.isEmpty()) {
            this.inputs.add(inputTime);
        } else if (this.inputs.get(this.inputs.size() - 1).compareTo(inputTime) < 0) {
            this.inputs.add(inputTime);
        } else if (inputTime.compareTo(this.inputs.get(0)) < 0) {
            this.inputs.add(0, inputTime);
        } else {
            for (int i = 0; i < this.inputs.size() - 1; i++) {
                boolean inBetween = this.inputs.get(i).compareTo(inputTime) <= 0
                        && inputTime.compareTo(this.inputs.get(i + 1)) <= 0;
                if (inBetween) {
                    this.inputs.add(i + 1, inputTime);
                    return;
                }
            }
        }
    }

    // REQUIRES: inputTime.getNsSinceStart() >= 0
    // MODIFIES: this
    // EFFECTS: modifies the NativeKeyEvent at this index
    //          preserves sorted order of data
    //          returns the new index of the key
    //          throws IllegalArgumentException if index < 0 or larger or equal to than this.getInputs().size()
    public int editKey(int index, T inputTime) {
        if (index < 0 || index >= this.getInputs().size()) {
            throw new IllegalArgumentException("Invalid index to edit.");
        }
//        KeyboardInputTime inputToEdit = this.inputs.get(index);
        this.inputs.remove(index);
        this.inputs.add(inputTime);

        Collections.sort(this.inputs);
        return this.inputs.indexOf(inputTime);
    }

    // REQUIRES: 0 <= index < this.getInputs().size()
    // MODIFIES: this
    // EFFECTS: deletes the given index, preserving sorted order
    public void deleteIndex(int index) {
        this.inputs.remove(index);
    }

    public List<T> getInputs() {
        return this.inputs;
    }

    public long getStartTime() {
        return startTime;
    }
}
