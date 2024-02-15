package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import persistence.JsonWriter;
import persistence.Writeable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Concrete implementation of I/O listener to save minput/kbinputs to disk.
 */
public class SaveListener extends IOListener implements ActionListener {

    // EFFECTS: see superclass, creates new IOListener with parent window and parent list
    public SaveListener(JList<InputRecording<? extends InputTime>> parentList, Component parent) {
        super(parentList, parent);
    }

    // MODIFIES: this
    // EFFECTS: saves selected element in list to path
    @Override
    public void actionPerformed(ActionEvent e) {
        var selected = getSelected();

        if (selected != null) {
            JsonWriter<InputRecording<? extends InputTime>> writer;
            writer = new JsonWriter<>(promptGetPath("Save").getAbsolutePath());
            try {
                writer.open();
            } catch (IOException ex) {
                System.out.println("error");
            }
            writer.write(selected);
            writer.close();
        }
    }
}
