package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import persistence.Writeable;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Abstract class skeleton for listeners that read/write to disk
 */
public abstract class IOListener implements ActionListener {
    private final JList<InputRecording<? extends InputTime>> parentList;
    private final Component parent;

    protected InputRecording<? extends InputTime> getSelected() {
        return parentList.getSelectedValue();
    }

    // EFFECTS: prompts of path, of either save or load, with given minput/kbinput as constraint
    protected File promptGetPath(String text) {
        JFileChooser chooser = new JFileChooser();
        // https://stackoverflow.com/questions/19302029/filter-file-types-with-jfilechooser
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String name = f.getName().toLowerCase();
                    return name.endsWith(".minput") || name.endsWith(".kbinput");
                }
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        chooser.showDialog(parent, text);
        return chooser.getSelectedFile();
    }

    // EFFECTS: creates new abstract I/O listener. Reserved for subclass calls
    protected IOListener(JList<InputRecording<? extends InputTime>> parentList, Component parent) {
        this.parent = parent;
        this.parentList = parentList;
    }
}
