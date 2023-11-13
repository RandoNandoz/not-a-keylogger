package ui.tools.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ui.AppState;
import ui.GuiApp;
import ui.tools.RecordingController;

public class ChangeRecordingModeListener implements ActionListener {
    private final JButton clientButton;
    private final RecordingController rc;

    public ChangeRecordingModeListener(JButton b) {
        this.clientButton = b;
        rc = RecordingController.getInstance();
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
            rc.addNewCapture();
            this.clientButton.setText("Stop Recording");
            System.out.println("Setting label to \"Stop Recording\"");
        } else {

            this.clientButton.setText("Start Recording");
            System.out.println("Setting label to \"Start Recording\"");
        }
    }


}
