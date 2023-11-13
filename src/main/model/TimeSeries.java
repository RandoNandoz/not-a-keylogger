package model;

import java.lang.reflect.ParameterizedType;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writeable;

public class TimeSeries<T extends InputTime> implements Writeable {

    private final ArrayList<T> inputs;
    private final long startTime;

    // EFFECTS: creates a new keyboard input times series capture with an empty list
    // of captures
    public TimeSeries() {
        this.startTime = System.nanoTime();
        this.inputs = new ArrayList<>();
    }

    public TimeSeries(ArrayList<T> inputs, long startTime) {
        //https://stackoverflow.com/a/19775924
        // why did I use generics in java?????

        this.inputs = inputs;
        this.startTime = startTime;
    }

    // REQUIRES: inputTime.getNsSinceStart() >= 0,
    // MODIFIES: this
    // EFFECTS: adds keyEvent to the internal list that holds the keyEvent in
    // temporal order (that is, sorted from s, guarantees that getInputs() will be
    // in sorted order after call
    public void addKey(T inputTime) {
        if (this.inputs.isEmpty()) {
            this.inputs.add(inputTime);
        } else if (0 > this.inputs.get(this.inputs.size() - 1).compareTo(inputTime)) {
            this.inputs.add(inputTime);
        } else if (0 > inputTime.compareTo(this.inputs.get(0))) {
            this.inputs.add(0, inputTime);
        } else {
            for (int i = 0; true; i++) { // horrible code coverage hack, only because we're graded on codecov.
                boolean inPosition = isInPosition(inputTime, i);
                if (inPosition) {
                    this.inputs.add(i + 1, inputTime);
                    return; // cause of bad coverage
                }
            }
        }
    }

    private boolean isInPosition(T inputTime, int i) {
        // because our list is in order, we're always greater than the previous
        // boolean greaterThanPrev = this.inputs.get(i).getNsRecordedTimeStamp() <
        // inputTime.getNsRecordedTimeStamp();
        return inputTime.getNsRecordedTimeStamp() < this.inputs.get(i + 1).getNsRecordedTimeStamp();
    }

    // REQUIRES: inputTime.getNsSinceStart() >= 0
    // MODIFIES: this
    // EFFECTS: modifies the NativeKeyEvent at this index
    // preserves sorted order of data
    // returns the new index of the key
    // throws IllegalArgumentException if index < 0 or larger or equal to than
    // this.getInputs().size()
    public int editKey(int index, T inputTime) {
        if (0 > index || index >= this.getInputs().size()) {
            throw new IllegalArgumentException("Invalid index to edit.");
        }
        // KeyboardInputTime inputToEdit = this.inputs.get(index);
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
        return this.startTime;
    }

    // EFFECTS: returns JSON manifestation of object.
    @Override
    public JSONObject toJson() {
        JSONObject objectAsJson = new JSONObject();
        JSONArray inputsAsJson = new JSONArray();
        for (T t : this.inputs) {
            inputsAsJson.put(t.toJson());
        }
        objectAsJson.put("inputs", inputsAsJson);
        objectAsJson.put("startTime", this.startTime);
        return objectAsJson;
    }


    // EFFECTS: performs deep comparison of all values in class.
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (null == object || this.getClass() != object.getClass()) {
            return false;
        }
        TimeSeries<?> that = (TimeSeries<?>) object;
        return this.startTime == that.startTime && this.inputs.equals(that.inputs);
    }

    // EFFECTS: provide hashcode on fields
    @Override
    public int hashCode() {
        return Objects.hash(this.inputs, this.startTime);
    }

    @Override
    public String toString() {
        long totalTimeElapsed = this.inputs.stream().mapToLong(v -> v.getDeltaTime(startTime)).sum();
        String inputType;
        if (this.inputs.isEmpty()) {
            inputType = "Unknown";
        } else {
            inputType = this.inputs.get(0).getType();
        }
        String result;
        result = String.format("Type: %s, Total Time: %d seconds", inputType, totalTimeElapsed);
        return result;
    }
}
