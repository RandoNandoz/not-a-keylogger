package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import persistence.JsonReader;
import ui.GuiApp;
import ui.tools.RecordingController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Concrete implementation for IO Listener, in order to load minput/kbinputs
 */
public class LoadListener extends IOListener implements ActionListener {
    private final GuiApp app;
    private final RecordingController rc;

    // EFFECTS: creates new loading file listener, with given app and recording controller from app
    public LoadListener(JList<InputRecording<? extends InputTime>> parentList,
                        Component parent, GuiApp app, RecordingController rc) {
        super(parentList, parent);
        this.app = app;
        this.rc = rc;
    }

    // MODIFIES: this
    // EFFECTS: loads given file from user choice
    @Override
    public void actionPerformed(ActionEvent e) {
        JsonReader reader = new JsonReader(promptGetPath("Load").getAbsolutePath());
        try {
            reader.readMouseInputRecording();
            // if it works...
            this.rc.addMouseRecording(reader.readMouseInputRecording());
        } catch (Exception e1) {
            try {
                reader.readKeyboardInputRecording();
                // if it works...
                this.rc.addKbRecording(reader.readKeyboardInputRecording());
            } catch (Exception e2) {
                app.popUpError("Invalid file format", "Error!");
            }
        }
    }
}
