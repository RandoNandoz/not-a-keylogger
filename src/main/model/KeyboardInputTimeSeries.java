package model;

import java.util.LinkedList;
import java.util.List;

public class KeyboardInputTimeSeries {
    /* TODO: determine whether ArrayList or LinkedList is best for this task, for now use LinkedList,
        as it seems most suited for the stack/queue style we're treating this as */
    LinkedList<KeyboardInputTime> inputTimes;

    // EFFECTS: creates a new keyboard input times series capture with an empty list of captures
    public KeyboardInputTimeSeries() {

    }

    // EFFECTS: plays back a list of inputs on the computer by their time order
    public void playback() {

    }

    public List<KeyboardInputTime> getInputTimes() {
        return inputTimes;
    }
}
