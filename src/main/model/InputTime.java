package model;

import org.json.JSONObject;

import persistence.Writeable;

/**
 * Abstract class that represents a pair of inputs/time.
 **/
public abstract class InputTime implements Comparable<InputTime>, Writeable, TypeInfoRevealable {
    EventLog log = EventLog.getInstance();
    protected long nsRecordedTimeStamp;

    // REQUIRES:  startTime < nsRecordedTimeStamp, that is, the recording must have started
    // before event was captured.
    // EFFECTS: computes the difference between the start of the recording, and the timestamp recorded.
    // this is useful as we want to calculate when to send.
    public long getDeltaTime(long startTime) {
        return this.nsRecordedTimeStamp - startTime;
    }


    // trivial getter
    public long getNsRecordedTimeStamp() {
        return this.nsRecordedTimeStamp;
    }

    // EFFECTS: returns -1 if o's timestamp < this's timestamp
    //          returns 0 if they are equal
    //          returns 1 if o's timestamp > this's timestamp.
    @Override
    public int compareTo(InputTime o) {
        return Long.compare(this.nsRecordedTimeStamp, o.nsRecordedTimeStamp);
    }

    // EFFECTS: returns jsonObject representation of class
    public abstract JSONObject toJson();

    @Override
    protected void finalize() {
        this.log.logEvent(new Event("Deleted input!"));
    }
}
