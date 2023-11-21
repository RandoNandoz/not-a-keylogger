package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import ui.tools.RecordingController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteViewListListener implements ActionListener {
    private final JList<InputRecording<? extends InputTime>> parentList;
    private final RecordingController rc;

    public DeleteViewListListener(JList<InputRecording<? extends InputTime>> parentList, RecordingController rc) {
        this.rc = rc;
        this.parentList = parentList;
    }


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
