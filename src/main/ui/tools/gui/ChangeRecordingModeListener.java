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

public class ChangeRecordingModeListener implements ActionListener {
    private final JButton clientButton;
    private final RecordingController rc;
    private final GuiApp app;

    public ChangeRecordingModeListener(JButton b, RecordingController rc, GuiApp app) {
        this.app = app;
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
            rc.addNewCapture();
            this.clientButton.setText("Stop Recording");
            System.out.println("Setting label to \"Stop Recording\"");
        } else {
            ArrayList<InputRecording<? extends InputTime>> concatenated = new ArrayList<>();
            concatenated.addAll(rc.getMouseCaptures());
            concatenated.addAll(rc.getKbCaptures());
            this.app.refreshList(concatenated);
            this.clientButton.setText("Start Recording");
            System.out.println("Setting label to \"Start Recording\"");
        }
    }
}
