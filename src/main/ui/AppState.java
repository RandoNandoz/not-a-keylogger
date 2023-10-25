package ui;

public class AppState {
    private static AppState singleton;
    private boolean recording;

    // EFFECTS: creates a new UNIQUE state of our app's state
    private AppState() {
    }

    // MODIFIES: this
    // EFFECTS: returns the unique instance of app state, otherwise, create a new one.
    public static AppState getInstance() {
        if (null == AppState.singleton) {
            singleton = new AppState();
        }
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
