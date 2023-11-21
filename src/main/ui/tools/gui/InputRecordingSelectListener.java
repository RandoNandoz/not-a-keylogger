package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import ui.tools.RecordingController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InputRecordingSelectListener implements ListSelectionListener {
    private final JList<InputTime> detailsList;
    private final JList<InputRecording<? extends InputTime>> parent;
    private final RecordingController rc;

    public InputRecordingSelectListener(JList<InputRecording<? extends InputTime>> parent,
                                        JList<InputTime> detailsList, RecordingController rc) {
        this.parent = parent;
        this.detailsList = detailsList;
        this.rc = rc;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
//        assert(e.getFirstIndex() == e.getLastIndex());

        int selectedIndex = this.parent.getSelectedIndex();

        System.out.println(selectedIndex);

        if (selectedIndex == -1) {
            return;
        }

        DefaultListModel<InputTime> model = new DefaultListModel<>();
        for (var x : rc.getUiList().get(selectedIndex).getInputs()) {
            model.addElement(x);
        }
        detailsList.setModel(model);
        detailsList.updateUI();
    }
}
