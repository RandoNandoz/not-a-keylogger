package ui.tools;

/**
 * Singleton to hold app's state of whether recording or not
 */
public class AppState {
    private static final AppState singleton = new AppState();
    private boolean recording;

    // EFFECTS: creates a new UNIQUE state of our app's state
    private AppState() {
        this.recording = false;
    }

    // MODIFIES: this
    // EFFECTS: returns the unique instance of app state, otherwise, create a new one.
    public static AppState getInstance() {
        return singleton;
    }

    // trivial getters & setters
    public boolean isRecording() {
        return this.recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }
}
