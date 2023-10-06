package model;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class KeyboardInputTimeSeries {
    /* TODO: determine whether ArrayList or LinkedList is best for this task, for now use LinkedList,
        as it seems most suited for the stack/queue style we're treating this as */
    LinkedList<KeyboardInputTime> inputs;

    // EFFECTS: creates a new keyboard input times series capture with an empty list of captures
    public KeyboardInputTimeSeries() {

    }

    // EFFECTS: plays back a list of inputs on the computer by their time order
    public void playback() {
        // stub
    }

    // REQUIRES: duration.toNanos() >= 0
    // MODIFIES: this
    // EFFECTS: adds keyEvent to the internal list that holds the keyEvent in
    // temporal order (that is, sorted from s, guarantees that getInputs() will be in sorted order after call
    public void addKey(NativeKeyEvent keyEvent, Duration duration) {

    }

    // REQUIRES: index >= 0
    // MODIFIES: this
    // EFFECTS: modifies the NativeKeyEvent at this time
    public void editKey(int index, NativeKeyEvent newKeyEvent) {

    }

    public List<KeyboardInputTime> getInputs() {
        return inputs;
    }
}
