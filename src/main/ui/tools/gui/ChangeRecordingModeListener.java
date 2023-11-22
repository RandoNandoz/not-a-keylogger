package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import ui.AppState;
import ui.GuiApp;
import ui.tools.RecordingController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Changes recording mode
 */
public class ChangeRecordingModeListener implements ActionListener {
    private final JButton clientButton;
    private final RecordingController rc;

    // EFFECTS: Creates listener to enable/disable recording status, with button that has this listener, and
    // app's recording controller
    public ChangeRecordingModeListener(JButton b, RecordingController rc) {
        this.clientButton = b;
        this.rc = rc;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        toggleButtonLabel();
    }

    // MODIFIES: this
    // EFFECTS: toggles state of app to record user inputs
    // switches button label to "Stop Recording" if current state is recording,
    // switches button label to "Start Recording" if current state is not recording.
    private void toggleButtonLabel() {
        boolean oldRecordingState = AppState.getInstance().isRecording();
        // invert the recording state
        boolean newRecordingState = !oldRecordingState;
        AppState.getInstance().setRecording(newRecordingState);

        String infoString = String.format("Previous app state was recording=%s, new state is recording=%s.",
                oldRecordingState, newRecordingState);

        System.out.println(infoString);

        if (newRecordingState) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new recording to current recordings
    // sets status of button
    private void startRecording() {
        rc.addNewCapture();
        this.clientButton.setText("Stop Recording");
        System.out.println("Setting label to \"Stop Recording\"");
    }

    // MODIFIES: this
    // EFFECTS: sets status of button, refreshes list view
    private void stopRecording() {
        rc.refreshListView();
        this.clientButton.setText("Start Recording");
        System.out.println("Setting label to \"Start Recording\"");
    }
}
