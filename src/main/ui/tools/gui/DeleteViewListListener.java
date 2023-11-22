package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import ui.tools.RecordingController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implementation for class to delete elements from list
 */
public class DeleteViewListListener implements ActionListener {
    private final JList<InputRecording<? extends InputTime>> parentList;
    private final RecordingController rc;

    // EFFECTS: creates listener for button to delete elements of list
    public DeleteViewListListener(JList<InputRecording<? extends InputTime>> parentList, RecordingController rc) {
        this.rc = rc;
        this.parentList = parentList;
    }

    // MODIFIES: this
    // EFFECTS: deletes element from list when clicked if element is selected
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = this.parentList.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }

        rc.removeFromDataList(selectedIndex);
        rc.refreshListView();
    }
}
